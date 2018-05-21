package com.goquizit.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.CreateUpdatePlayerAnswerDTO;
import com.goquizit.DTO.QuestionWithAnswersAndPlayerIdDTO;
import com.goquizit.DTO.outputDTO.AnswerToPlayerOutputDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.exception.ResponseException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.Player;
import com.goquizit.model.PlayerAnswer;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@Service
public class PlayerAnswerService {
    @Autowired
    private QuizService quizService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    public QuestionWithAnswersAndPlayerIdDTO createPlayerAnswer(UUID player_id, UUID question_id, @Valid @JsonProperty("answers") List<CreateUpdatePlayerAnswerDTO> playerAnswerDTOS) {
        try {
            Player player = playerService.getOne(player_id);
            Question question = questionService.getOne(question_id);
            playerAnswerDTOS.forEach(playerAnswer ->
                    this.createPlayerAnswerByDTO(playerAnswer, question, player));

            return prepareNextQuestionWithAnswers(player_id, question);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private QuestionWithAnswersAndPlayerIdDTO prepareNextQuestionWithAnswers(UUID player_id, Question question) {
        int actualIndex = question.getIndex();
        checkQuestionIndex(question, actualIndex);
        QuestionOutputDTO nextQuestion = quizService.getQuestionByQuizIdByIndex(question.getQuizId(), ++actualIndex);
        List<AnswerToPlayerOutputDTO> answers = answerService.mapAnswersToPlayerToOutput(question.getAnswers());
        if (answers.isEmpty())
            throw new ResponseException("Question have not any answers");
        return new QuestionWithAnswersAndPlayerIdDTO(player_id, nextQuestion, answers);
    }

    private void checkQuestionIndex(Question question, int actualIndex) {
        Quiz quiz = quizService.getOne(question.getQuizId());
        if (actualIndex == quiz.getQuestions().size() - 1)
            throw new ResponseException("Quiz have no more questions.");
    }

    public PlayerAnswer createPlayerAnswerByDTO(CreateUpdatePlayerAnswerDTO createUpdatePlayerAnswerDTO, Question question, Player player) {
        PlayerAnswer playerAnswer = new PlayerAnswer();
        playerAnswer.setQuestion(question);
        playerAnswer.setAnswerID(createUpdatePlayerAnswerDTO.getId());
        playerAnswer.setValue("answer");
        player.getPlayerAnswers().add(playerAnswer);
        Player updatePlayer = playerService.save(player);
        return updatePlayer.getLastPlayerAnswer();
    }
}
