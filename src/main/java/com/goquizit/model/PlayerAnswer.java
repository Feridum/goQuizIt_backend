package com.goquizit.model;


public class PlayerAnswer {

  private String answerId;
  private String questionId;
  private String value;
  private String playerIdPlayer;
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


  public String getPlayerIdPlayer() {
    return playerIdPlayer;
  }

  public void setPlayerIdPlayer(String playerIdPlayer) {
    this.playerIdPlayer = playerIdPlayer;
  }


  public String getQuestionIdQuestion() {
    return questionIdQuestion;
  }

  public void setQuestionIdQuestion(String questionIdQuestion) {
    this.questionIdQuestion = questionIdQuestion;
  }

}
