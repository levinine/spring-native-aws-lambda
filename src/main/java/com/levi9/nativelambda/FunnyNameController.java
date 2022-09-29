package com.levi9.nativelambda;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@ResponseBody
@EnableWebMvc
public class FunnyNameController {

    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
    Random randomizer = new Random();
    @Autowired
    NameRepository repository;

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public FunnyNameEntity getById(@PathVariable String id) {
        System.out.println("getById");
        return repository.getName(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Not found"));
    }

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
