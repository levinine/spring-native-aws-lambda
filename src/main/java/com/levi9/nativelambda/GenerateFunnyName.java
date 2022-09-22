package com.levi9.nativelambda;

import com.levi9.nativelambda.dto.FunnyName;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GenerateFunnyName implements Function<FunnyNameRequest, FunnyName> {
    @Override
    public FunnyName apply(FunnyNameRequest funnyNameRequest) {
        return FunnyName.builder()
                .name("Huckleberry " + funnyNameRequest.getName())
                .generatedAt(new Date())
                .build();
    }
}
