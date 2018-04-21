package com.goquizit.model;

public enum QuizState {
    ACTIVE("active"), INACTIVE("inactive"), FINISHED("finished");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    QuizState(String value)
    {
        this.value = value;
    }

}
