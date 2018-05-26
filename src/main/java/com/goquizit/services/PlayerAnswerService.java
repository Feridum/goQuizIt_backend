package com.goquizit.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.CreateUpdatePlayerAnswerDTO;
import com.goquizit.DTO.QuestionWithAnswersAndPlayerIdDTO;
import com.goquizit.DTO.outputDTO.AnswerToPlayerOutputDTO;
import com.goquizit.DTO.outputDTO.FinishedQuiz;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.exception.ResponseException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.Player;
import com.goquizit.model.PlayerAnswer;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import org.hibernate.result.NoMoreReturnsException;
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

    public Object createPlayerAnswer(UUID player_id, UUID question_id, @Valid @JsonProperty("answers") CreateUpdatePlayerAnswerDTO playerAnswerDTOS) {
        try {
            Player player = playerService.getOne(player_id);
            Question question = questionService.getOne(question_id);
            this.createPlayerAnswerByDTO(playerAnswerDTOS, question, player);

            return this.prepareNextQuestionWithAnswers(player_id, question);
        } catch (NoMoreReturnsException e) {
            return new FinishedQuiz();
        }catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private QuestionWithAnswersAndPlayerIdDTO prepareNextQuestionWithAnswers(UUID player_id, Question question) {
        int actualIndex = question.getIndex();
        checkQuestionIndex(question, actualIndex);
        QuestionOutputDTO nextQuestionDTO = quizService.getQuestionByQuizIdByIndex(question.getQuizId(), ++actualIndex);
        Question nextQuestion = questionService.getOne(nextQuestionDTO.getQuestionId());
        List<AnswerToPlayerOutputDTO> answers = answerService.mapAnswersToPlayerToOutput(nextQuestion.getAnswers());
        if (answers.isEmpty())
            throw new ResponseException("Question have not any answers");
        return new QuestionWithAnswersAndPlayerIdDTO(player_id, nextQuestionDTO, answers);
    }

    private void checkQuestionIndex(Question question, int actualIndex) {
        Quiz quiz = quizService.getOne(question.getQuizId());
        if (actualIndex == quiz.getQuestions().size() - 1)
            throw new NoMoreReturnsException();
    }

    public void createPlayerAnswerByDTO(CreateUpdatePlayerAnswerDTO createUpdatePlayerAnswerDTOs, Question question, Player player) {
        createUpdatePlayerAnswerDTOs.getId().forEach(playerAnswerId -> {
            if (!answerService.checkAnswerIdInQuestion(playerAnswerId, question.getQuestionId()))
                throw new ResponseException("There is no answer with that id: " + playerAnswerId);
            PlayerAnswer playerAnswer = new PlayerAnswer();
            playerAnswer.setQuestion(question);
            playerAnswer.setAnswerID(playerAnswerId);
            playerAnswer.setValue("answer");
            player.getPlayerAnswers().add(playerAnswer);
            playerService.save(player);
        });
    }
}
