package com.levi9.nativelambda;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.nativex.hint.SerializationHint;

@SpringBootApplication
@SerializationHint(types = {FunnyNameDto.class, GeneratedFunnyName.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, // No JPA
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import(FunnyNameController.class)
public class NativeLambdaApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NativeLambdaApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
