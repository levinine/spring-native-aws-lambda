package com.levi9.nativelambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@SerializationHint(types = {FunnyNameDto.class, GeneratedFunnyName.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, // No JPA
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableWebMvc
public class NativeLambdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NativeLambdaApplication.class, args);
    }

}
