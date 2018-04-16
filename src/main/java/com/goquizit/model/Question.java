package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "questions")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String questionId;

  @NotNull
  private String value;

  @NotNull
  @Enumerated(EnumType.STRING)
  private QuestionState type;

  @NotNull
  @Min(1)
  private int duration;

  @NotNull
  private String quizIdQuiz;


  public String getQuestionId() { return questionId; }

  public void setQuestionId(String questionId) { this.questionId = questionId; }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public QuestionState getType() {
    return type;
  }

  public void setType(QuestionState type) {
    this.type = type;
  }


  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }


  public String getQuizIdQuiz() {
    return quizIdQuiz;
  }

  public void setQuizIdQuiz(String quizIdQuiz) {
    this.quizIdQuiz = quizIdQuiz;
  }

}
