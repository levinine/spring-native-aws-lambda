package com.levi9.nativelambda;


import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Configuration
public class FunnyNameFunctionImplementor {

    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
    public static final APIGatewayV2HTTPResponse.APIGatewayV2HTTPResponseBuilder RESPONSE_BUILDER = APIGatewayV2HTTPResponse.builder()
            .withStatusCode(200)
            .withIsBase64Encoded(false)
            .withHeaders(Map.of(
                    "Content-Type", "application/json"
            ));
    Random randomizer = new Random();
    @Autowired
    NameRepository repository;

    private static Logger logger = LoggerFactory.getLogger(FunnyNameFunctionImplementor.class);


    @Bean
    public Function<Message<FunnyNameRequestDto>, FunnyNameEntity> getFunnyNameById() {
        return request -> {
            var dto = request.getPayload();
            logger.info("getFunnyNameById executed with request " + request);

            return repository.getName(dto.getId()).orElseThrow();
        };
    }

    @Bean
    public Function<Message<GenerateFunnyNameDto>, String> generateFunnyName() {
        return request -> {
            GenerateFunnyNameDto dto = request.getPayload();

            logger.info("generateFunnyName executed with request " + dto);
            repository.saveName(
                    FunnyNameEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .name(namePrefixes.get(randomizer.nextInt(namePrefixes.size())) + " " + dto.getName())
                            .generatedAt(Instant.now())
                            .build()

            );
            return "";
        };
    }
}
