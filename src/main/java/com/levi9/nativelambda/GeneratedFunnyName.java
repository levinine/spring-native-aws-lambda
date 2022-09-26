package com.levi9.nativelambda;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@Builder
public class GeneratedFunnyName implements Serializable {
    String id;
    String name;
    Date generatedAt;
}