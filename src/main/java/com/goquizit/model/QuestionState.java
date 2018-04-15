package com.goquizit.model;

public enum QuestionState {
    SingleChoice("SingleChoice"), MultipleChoice("MultipleChoice"), Open("Open"), Axis("Axis"), Date("Date");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    QuestionState(String value)
    {
        this.value = value;
    }

}
