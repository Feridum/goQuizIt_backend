package com.goquizit.DTO;

        import com.goquizit.model.Answer;
        import com.goquizit.model.Question;

        import java.util.List;

public class QuestionWithAnswersInputDTO {
    private CreateUpdateQuestionDTO question;
    private List<CreateUpdateAnswersDTO> answers;

    public CreateUpdateQuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(CreateUpdateQuestionDTO question) {
        this.question = question;
    }

    public List<CreateUpdateAnswersDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<CreateUpdateAnswersDTO> answers) {
        this.answers = answers;
    }
}
