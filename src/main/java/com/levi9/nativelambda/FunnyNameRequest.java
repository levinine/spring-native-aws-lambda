package com.levi9.nativelambda;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FunnyNameRequest implements Serializable {
    String name;
}
