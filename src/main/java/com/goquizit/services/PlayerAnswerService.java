package com.goquizit.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.CreateUpdatePlayerAnswerDTO;
import com.goquizit.DTO.CreateUpdatePlayerAnswerOpenDTO;
import com.goquizit.DTO.QuestionWithAnswersAndPlayerIdDTO;
import com.goquizit.DTO.outputDTO.AnswerToPlayerOutputDTO;
import com.goquizit.DTO.outputDTO.FinishedQuiz;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.exception.ResponseException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.*;
import com.goquizit.repository.PlayerAnswerRepository;
import org.hibernate.result.NoMoreReturnsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


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

    @Autowired
    private PlayerAnswerRepository playerAnswerRepository;

    public Object createPlayerAnswerToSingleMultipleQuestion(UUID player_id, UUID question_id, @Valid @JsonProperty("answers") CreateUpdatePlayerAnswerDTO playerAnswerDTOS) {
        try {
            Player player = playerService.getOne(player_id);
            Question question = questionService.getOne(question_id);

            this.createPlayerAnswerToSingleMultipleQuestionByDTO(playerAnswerDTOS, question, player);

            return this.prepareNextQuestionWithAnswers(player_id, question);
        } catch (NoMoreReturnsException e) {
            return new FinishedQuiz();
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private void createPlayerAnswerToSingleMultipleQuestionByDTO(CreateUpdatePlayerAnswerDTO createUpdatePlayerAnswerDTOs, Question question, Player player) {
        // TODO: fix with next and previous question version
        // removeOldPlayerAnswers(question, player);
        createUpdatePlayerAnswerDTOs.getId().forEach(playerAnswerId -> {
            if (!answerService.checkAnswerIdInQuestion(playerAnswerId, question.getQuestionId()))
                throw new ResponseException("createPlayerAnswerByDTO: There is no answer with that id: " + playerAnswerId);

            PlayerAnswer playerAnswer = new PlayerAnswer();
            playerAnswer.setQuestion(question);
            playerAnswer.setAnswerID(playerAnswerId);
            playerAnswer.setValue(answerService.getOne(playerAnswerId).getValue());
            player.getPlayerAnswers().add(playerAnswer);
        });

        if ((checkIfIncrementPoints(createUpdatePlayerAnswerDTOs, question.getQuestionId()))) {
            player.incrementPoints();
        }

        playerService.save(player);
    }

    private QuestionWithAnswersAndPlayerIdDTO prepareNextQuestionWithAnswers(UUID player_id, Question question) {
        int actualIndex = question.getIndex();
        this.checkQuestionIndex(question, actualIndex);
        QuestionOutputDTO nextQuestionDTO = quizService.getQuestionByQuizIdByIndex(question.getQuizId(), ++actualIndex);
        Question nextQuestion = questionService.getOne(nextQuestionDTO.getQuestionId());
        List<AnswerToPlayerOutputDTO> answers = answerService.mapAnswersToPlayerToOutput(nextQuestion.getAnswers());

        int numberOfQuestions = getNumberOfQuestions(question);

        if (answers.isEmpty() && (nextQuestion.getType().equals(QuestionState.SINGLE_CHOICE) ||
                nextQuestion.getType().equals(QuestionState.MULTIPLE_CHOICE))) {
            throw new ResponseException("prepareNextQuestionWithAnswers: Question have not any answers");
        }

        return new QuestionWithAnswersAndPlayerIdDTO(player_id, nextQuestionDTO, answers,++actualIndex,numberOfQuestions);
    }

    private int getNumberOfQuestions(Question question) {
        Quiz quiz = quizService.getOne(question.getQuizId());
        return quiz.getQuestions().size();
    }

    public Object createPlayerAnswerToOpenQuestion(UUID player_id, UUID question_id, @Valid @JsonProperty("answers") CreateUpdatePlayerAnswerOpenDTO playerAnswerOpenDTO) {
        try {
            Player player = playerService.getOne(player_id);
            Question question = questionService.getOne(question_id);

            this.createPlayerAnswerToOpenQuestionByDTO(playerAnswerOpenDTO, question, player);

            return this.prepareNextQuestionWithAnswers(player_id, question);
        } catch (NoMoreReturnsException e) {
            return new FinishedQuiz();
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public void createPlayerAnswerToOpenQuestionByDTO(CreateUpdatePlayerAnswerOpenDTO createUpdatePlayerAnswerOpenDTO, Question question, Player player) {
        boolean didPlayerGiveAnswer = false;

        for(PlayerAnswer temp : question.getPlayerAnswers()) {
            if (temp.getPlayer().equals(player)) {
                didPlayerGiveAnswer = true;
            }
        }

        if (!didPlayerGiveAnswer) {
            PlayerAnswer playerAnswer = new PlayerAnswer();
            playerAnswer.setQuestion(question);
            playerAnswer.setValue(createUpdatePlayerAnswerOpenDTO.getValue());
            player.getPlayerAnswers().add(playerAnswer);
        }

        playerService.save(player);
    }

    private void checkQuestionIndex(Question question, int actualIndex) {
        Quiz quiz = quizService.getOne(question.getQuizId());
        if (actualIndex == quiz.getQuestions().size() - 1)
            throw new NoMoreReturnsException();
    }

    private void removeOldPlayerAnswers(Question question, Player player) {
        List<PlayerAnswer> playerOldAnswers = this.getByQuestion(question);
        playerOldAnswers.forEach(playerOldAnswer -> player.getPlayerAnswers().remove(playerOldAnswer));

        AtomicBoolean correctAnswer = new AtomicBoolean(true);
        List<UUID> correctAnswers = answerService.getCorrectAnswers(question.getQuestionId());
        if (correctAnswers.size() == playerOldAnswers.size()) {
            correctAnswers.forEach(answer -> {
                if (!playerOldAnswers.contains(answer))
                    correctAnswer.set(false);
            });
        }
        else {
            correctAnswer.set(false);
        }

        if (!correctAnswer.get() == false) {
            player.decrementPoints();

            if (player.getResult() < 0) {
                player.setResult(0);
            }
        }
        playerService.save(player);
    }

    private boolean checkIfIncrementPoints(CreateUpdatePlayerAnswerDTO createUpdatePlayerAnswerDTOs, UUID questionId) {
        AtomicBoolean correctAnswer = new AtomicBoolean(true);
        List<UUID> correctAnswers = answerService.getCorrectAnswers(questionId);
        Question question = questionService.getOne(questionId);

        switch (question.getType()) {
            case SINGLE_CHOICE:
            case MULTIPLE_CHOICE:
                if (correctAnswers.size() == createUpdatePlayerAnswerDTOs.getId().size()) {
                    for (UUID playerAnswerUUID : createUpdatePlayerAnswerDTOs.getId()) {
                        if (!correctAnswers.contains(playerAnswerUUID)) {
                            correctAnswer.set(false);
                        }
                    }
                }
                else {
                    correctAnswer.set(false);
                }
                break;
            case OPEN:
            default:
                break;
        }

        return correctAnswer.get();
    }

    public List<PlayerAnswer> getPlayerAnswersByPlayerAndQuestion(UUID questionId, UUID playerId) {
        return playerAnswerRepository.getPlayerAnswersByByPlayerAndQuestion(questionId, playerId);
    }


    private List<PlayerAnswer> getByQuestion(Question question) {
        try {
            return playerAnswerRepository.getByQuestion(question);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }
}
