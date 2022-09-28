package com.levi9.nativelambda;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class FunnyNameController {

    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
    Random randomizer = new Random();
    @Autowired
    NameRepository repository;

    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public void generateFunnyName(@RequestBody FunnyNameDto dto) {
        System.out.println("generateFunnyName");

        repository.saveName(
                FunnyNameEntity.builder()
                        .name(namePrefixes.get(randomizer.nextInt(namePrefixes.size())) + " " + dto.getName())
                        .generatedAt(Instant.now())
                        .build()

        );
    }
}
