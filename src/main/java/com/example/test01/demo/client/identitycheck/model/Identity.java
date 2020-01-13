package com.example.test01.demo.client.identitycheck.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Identity {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String age;
}
