package com.goquizit.beta.dto;


import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
public class UserListDTO {

    @Min(1)
    @NotNull
    @Digits(integer = 6, fraction = 0)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;

    }
}

