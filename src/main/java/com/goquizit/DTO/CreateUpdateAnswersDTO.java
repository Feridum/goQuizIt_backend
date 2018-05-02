package com.goquizit.DTO;

import javax.validation.constraints.NotNull;

public class CreateUpdateAnswersDTO {
    @NotNull
    private String value;

    @NotNull
    private boolean isPositive;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }
}
