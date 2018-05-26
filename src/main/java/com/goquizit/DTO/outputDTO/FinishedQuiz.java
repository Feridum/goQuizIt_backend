package com.goquizit.DTO.outputDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

//TODO chane to utils static methods!!!
public class FinishedQuiz {
    private boolean isFinish;

    @JsonProperty("isFinish")
    public boolean isFinish() {
        return isFinish;
    }

    public FinishedQuiz() {
        this.isFinish = true;
    }
}
