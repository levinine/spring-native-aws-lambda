package com.levi9.nativelambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LambdaHandler implements RequestStreamHandler {
    private static Logger logger = LoggerFactory.getLogger(LambdaHandler.class);

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            logger.info("handler started");
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(NativeLambdaApplication.class);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        logger.info("Request recieved");

        AwsProxyRequest request = mapper.readValue(inputStream, AwsProxyRequest.class);
        logger.info(request.getPath());
        logger.info(request.getBody());
        logger.info(request.getResource());

        AwsProxyResponse resp = handler.proxy(request, context);
        mapper.writeValue(outputStream, resp);
        // just in case it wasn't closed by the mapper
        outputStream.close();
    }
}