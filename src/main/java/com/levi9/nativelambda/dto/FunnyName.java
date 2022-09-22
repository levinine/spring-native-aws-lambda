package com.levi9.nativelambda.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
@Value
@Builder
public class FunnyName implements Serializable {
    String name;
    Date generatedAt;
}
