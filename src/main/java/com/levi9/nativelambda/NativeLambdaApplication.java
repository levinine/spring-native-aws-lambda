package com.levi9.nativelambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.nativex.hint.SerializationHint;

@SpringBootApplication
@SerializationHint(types = {FunnyNameDto.class, GeneratedFunnyName.class})
@Import(FunnyNameController.class)
public class NativeLambdaApplication {

    private static Logger logger = LoggerFactory.getLogger(NativeLambdaApplication.class);

    public static void main(String[] args) {
        logger.info("starting app");
        SpringApplication.run(NativeLambdaApplication.class, args);
        logger.info("started app");
    }
}