package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "ANSWER")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"})
public class Answer {

  @Id
  @Type(type = "pg-uuid")
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "uuid")
  private UUID answerId;

  //@NotNull
  private UUID questionId;

  @NotNull
  private String value;

  @NotNull
  private boolean isPositive;

  // What is this for?
  private String questionIdQuestion;


  public UUID getAnswerId() {
    return answerId;
  }

  public void setAnswerId(UUID answerId) {
    this.answerId = answerId;
  }


  public UUID getQuestionId() {
    return questionId;
  }

  public void setQuestionId(UUID questionId) {
    this.questionId = questionId;
  }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public boolean getIsPositive() {
    return isPositive;
  }

  public void setIsPositive(boolean isPositive) {
    this.isPositive = isPositive;
  }


  public String getQuestionIdQuestion() {
    return questionIdQuestion;
  }

  public void setQuestionIdQuestion(String questionIdQuestion) {
    this.questionIdQuestion = questionIdQuestion;
  }

}
