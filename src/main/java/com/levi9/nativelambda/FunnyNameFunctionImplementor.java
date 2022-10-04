package com.levi9.nativelambda;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Configuration
public class FunnyNameFunctionImplementor {

    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
    Random randomizer = new Random();
    @Autowired
    NameRepository repository;

    private static Logger logger = LoggerFactory.getLogger(FunnyNameFunctionImplementor.class);



    @Bean
    public Function<String, FunnyNameEntity> getFunnyNameById() {
        logger.info("getFunnyNameById");
        return id -> repository.getName(id).orElseThrow();
//        return id -> repository.getName(id).orElseThrow(() -> new Exception("Not found"));
    }

    @Bean
    public Function<FunnyNameDto, String> generateFunnyName() {
        logger.info("generateFunnyName");
        return dto -> {
            repository.saveName(
                    FunnyNameEntity.builder()
                            .name(namePrefixes.get(randomizer.nextInt(namePrefixes.size())) + " " + dto.getName())
                            .generatedAt(Instant.now())
                            .build()

            );
            return ""; //todo
        };
    }
}
