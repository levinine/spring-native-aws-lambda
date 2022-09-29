package com.levi9.nativelambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SpringBootApplication
@SerializationHint(types = {FunnyNameDto.class, GeneratedFunnyName.class})
@Import(FunnyNameController.class)
public class NativeLambdaApplication extends SpringBootServletInitializer implements RequestStreamHandler {

    private static Logger logger = LoggerFactory.getLogger(NativeLambdaApplication.class);

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    public static void main(String[] args) {
        logger.info("creating handler");
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);

            // For applications that take longer than 10 seconds to start, use the async builder:
            // handler = new SpringBootProxyHandlerBuilder<AwsProxyRequest>()
            //                    .defaultProxy()
            //                    .asyncInit()
            //                    .springBootApplication(Application.class)
            //                    .buildAndInitialize();

            // we use the onStartup method of the handler to register our custom filter
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
        logger.info("created handler");
    }

    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    /*
     * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created
     */
    @Bean
    public HandlerAdapter handlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}