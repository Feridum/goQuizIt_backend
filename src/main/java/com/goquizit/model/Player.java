package com.goquizit.model;


public class Player {

  private String playerId;
  private String quizId;
  private String quizIdQuiz;


  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }


  public String getQuizId() {
    return quizId;
  }

  public void setQuizId(String quizId) {
    this.quizId = quizId;
  }


  public String getQuizIdQuiz() {
    return quizIdQuiz;
  }

  public void setQuizIdQuiz(String quizIdQuiz) {
    this.quizIdQuiz = quizIdQuiz;
  }

}
