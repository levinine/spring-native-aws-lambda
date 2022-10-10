package com.levi9.nativelambda;


import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Bean
    public Function<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> getFunnyNameById() {
        return request -> {
            try {
                var body = request.getBody();
                var dto = objectMapper.readValue(body, FunnyNameRequestDto.class);
                logger.info("getFunnyNameById executed with request " + request);
                FunnyNameEntity funnyNameEntity = repository.getName(dto.getId()).orElseThrow();

                return RESPONSE_BUILDER
                        .withBody(objectMapper.writeValueAsString(funnyNameEntity))
                        .build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    public Function<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> generateFunnyName() {
        return request -> {
            try {
                var body = request.getBody();
                GenerateFunnyNameDto dto = objectMapper.readValue(body, GenerateFunnyNameDto.class);

                logger.info("generateFunnyName executed with request " + dto);
                repository.saveName(
                        FunnyNameEntity.builder()
                                .id(UUID.randomUUID().toString())
                                .name(namePrefixes.get(randomizer.nextInt(namePrefixes.size())) + " " + dto.getName())
                                .generatedAt(Instant.now())
                                .build()

                );
                return RESPONSE_BUILDER
                        .withBody("") //todo
                        .build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
