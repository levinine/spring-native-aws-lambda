package com.levi9.nativelambda;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

@Configuration
public class FunnyNameFunctionImplementor {

    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
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
