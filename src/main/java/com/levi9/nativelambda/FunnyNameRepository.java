package com.levi9.nativelambda;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface FunnyNameRepository extends CrudRepository<FunnyNameEntity, String> {
}
