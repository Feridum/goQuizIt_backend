package com.goquizit.model;


public class Answer {

  private String answerId;
  private String questionId;
  private String value;
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
