package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "QUESTION")
//@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"})
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long questionId;

  private Long sampleQuizId;

  @NotNull
  private String value;

  @NotNull
  @Enumerated(EnumType.STRING)
  private QuestionState type;

  @NotNull
  @Min(1)
  private int duration;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quiz")
  private Quiz quiz;


  public Long getSampleQuizId() {
    return sampleQuizId;
  }

  public void setSampleQuizId(Long sampleQuizId) {
    this.sampleQuizId = sampleQuizId;
  }

  public String getValue() {
    return value;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
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


  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }
}
