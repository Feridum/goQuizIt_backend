package com.goquizit.DTO;

import com.goquizit.model.Answer;
import com.goquizit.model.Question;

import java.util.List;

public class QuestionWithAnswersOutputDTO {
    private Question question;
    private List<Answer> answers;

    public QuestionWithAnswersOutputDTO(Question question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
