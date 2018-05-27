package com.goquizit.DTO;

import javax.validation.constraints.NotNull;

public class CreateUpdateAnswersDTO {
    @NotNull
    private String value;

    private boolean isPositive;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(boolean positive) {
        isPositive = positive;
    }
}
