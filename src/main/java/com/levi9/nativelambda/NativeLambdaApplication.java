package com.levi9.nativelambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
@SerializationHint(types = {FunnyNameDto.class, GeneratedFunnyName.class})
@Import(FunnyNameController.class)
public class NativeLambdaApplication {

    private static Logger logger = LoggerFactory.getLogger(NativeLambdaApplication.class);

    public static void main(String[] args) {
        logger.info("starting app");
        new SpringApplicationBuilder(NativeLambdaApplication.class).run(args);
        logger.info("started app");
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

}