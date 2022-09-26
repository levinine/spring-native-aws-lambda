package com.levi9.nativelambda;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class FunnyNameController {

    final static List<String> namePrefixes = Arrays.asList("Huckleberry", "Sizzle Pants", "Itsy Bitsy", "Speedy", "Mighty", "Legendary", "Jumpy");
    Random randomizer = new Random();
    @Autowired
    FunnyNameRepository repository;

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public FunnyNameEntity getById(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Not found"));
    }

    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    public GeneratedFunnyName generateFunnyName(@RequestBody FunnyNameDto dto) {
        FunnyNameEntity savedName = repository.save(
                FunnyNameEntity.builder()
                        .name(namePrefixes.get(randomizer.nextInt(namePrefixes.size())) + " " + dto.getName())
                        .generatedAt(new Date())
                        .build()

        );
        return GeneratedFunnyName.builder()
                .id(savedName.getId())
                .name(savedName.getName())
                .generatedAt(savedName.getGeneratedAt())
                .build();
    }
}
