package com.levi9.nativelambda;

import com.levi9.nativelambda.dto.FunnyName;
import org.springframework.cglib.core.internal.Function;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AotProxyHint(targetClass = com.levi9.nativelambda.GenerateFunnyName.class, interfaces = {java.util.function.Function.class}, proxyFeatures = ProxyBits.IS_STATIC)
public class GenerateFunnyName implements Function<FunnyNameRequest, FunnyName> {
    @Override
    public FunnyName apply(FunnyNameRequest funnyNameRequest) {
        return FunnyName.builder()
                .name("Huckleberry " + funnyNameRequest.getName())
                .generatedAt(new Date())
                .build();
    }
}
