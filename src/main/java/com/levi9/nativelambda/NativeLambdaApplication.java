package com.levi9.nativelambda;

import com.levi9.nativelambda.dto.FunnyName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.SerializationHint;

@SpringBootApplication
@SerializationHint(types = {FunnyName.class, FunnyNameRequest.class, GenerateFunnyName.class})
public class NativeLambdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NativeLambdaApplication.class, args);
	}

}
