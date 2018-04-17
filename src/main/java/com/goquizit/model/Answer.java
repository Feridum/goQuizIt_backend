package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "answers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String answerId;

  @NotNull
  private String questionId;

  @NotNull
  private String value;

  @NotNull
  private String isPositive;

  private String questionIdQuestion;


  public String getAnswerId() {
    return answerId;
  }

  public void setAnswerId(String answerId) {
    this.answerId = answerId;
  }


  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public String getIsPositive() {
    return isPositive;
  }

  public void setIsPositive(String isPositive) {
    this.isPositive = isPositive;
  }


  public String getQuestionIdQuestion() {
    return questionIdQuestion;
  }

  public void setQuestionIdQuestion(String questionIdQuestion) {
    this.questionIdQuestion = questionIdQuestion;
  }

}
