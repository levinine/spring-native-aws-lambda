package com.levi9.nativelambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.nativex.hint.SerializationHint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@SerializationHint(types = {FunnyNameDto.class, GeneratedFunnyName.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, // No JPA
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import(FunnyNameController.class)
public class NativeLambdaApplication implements RequestStreamHandler {

    private static SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;
    private static Logger logger = LoggerFactory.getLogger(NativeLambdaApplication.class);

    static {
        try {
            logger.info("StreamLambdaHandler static init start");
            //handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(SamplebootApplication.class);

            handler = new SpringBootProxyHandlerBuilder<HttpApiV2ProxyRequest>()
                    .defaultHttpApiV2Proxy().asyncInit()
                    .springBootApplication(NativeLambdaApplication.class).buildAndInitialize();

            logger.info("StreamLambdaHandler init -async init call ");

            logger.info("StreamLambdaHandler init call invoked");

        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(NativeLambdaApplication.class, args);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Instant start = Instant.now();
        logger.info("StreamLambdaHandler handler method begin");
        handler.proxyStream(inputStream, outputStream, context);
        logger.info(
                "StreamLambdaHandler handler method end in millisecs:" + start.until(Instant.now(), ChronoUnit.MILLIS));

    }
}
