package com.levi9.nativelambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(NativeLambdaApplication.NativeRuntimeHints.class)
@Import(FunnyNameFunctionImplementor.class)
public class NativeLambdaApplication {

    private static Logger logger = LoggerFactory.getLogger(NativeLambdaApplication.class);

    public static void main(String[] args) {
        logger.info("starting app");
        new SpringApplicationBuilder(NativeLambdaApplication.class).run(args);
        logger.info("started app");
    }

    static class NativeRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            // Register serialization
            hints.serialization().registerType(GenerateFunnyNameDto.class);
            hints.serialization().registerType(FunnyNameRequestDto.class);
        }
    }
}