package com.levi9.nativelambda;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FunnyNameRequestDto implements Serializable {
    String id;
}
