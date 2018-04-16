package com.goquizit.model;

public enum QuizState {
    Started("Started"), Ended("Ended");

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
