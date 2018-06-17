package com.goquizit.services;

import com.goquizit.DTO.PlayerDTO;
import com.goquizit.DTO.QuestionWithAnswersAndPlayerIdDTO;
import com.goquizit.DTO.outputDTO.AnswerToPlayerOutputDTO;
import com.goquizit.DTO.outputDTO.PlayerOutputDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.exception.ResponseException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.*;
import com.goquizit.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    QuizService quizService;

    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerService answerService;


    public QuestionWithAnswersAndPlayerIdDTO create(@Valid PlayerDTO player, @Valid UUID quizId) {
        try {
            Player newPlayer = this.mapCreatePlayerDTOToPlayer(player);
            Quiz quiz = quizService.getOne(quizId);
            quiz.getPlayers().add(newPlayer);
            checkRequiredField(player, quiz);
            checkNumberOfQuestionAndQuizState(quiz);
            Question question = quiz.getQuestionSet().get(0);

            QuestionOutputDTO outputQuestionDTO = questionService.mapQuestionToOutput(question, quizId);
            List<AnswerToPlayerOutputDTO> answers = answerService.mapAnswersToPlayerToOutput(question.getAnswers());

            if (answers.isEmpty() && (question.getType().equals(QuestionState.SINGLE_CHOICE) ||
                    question.getType().equals(QuestionState.MULTIPLE_CHOICE))) {
                throw new ResponseException("Question have not any answers");
            }

            Quiz tempQuiz = quizService.save(quiz);
            UUID playerId = tempQuiz.getPlayers().get(tempQuiz.getPlayers().size() - 1).getPlayerId();

            int numberOfQuestions = quiz.getQuestions().size();

            QuestionWithAnswersAndPlayerIdDTO outputDTO = new QuestionWithAnswersAndPlayerIdDTO(playerId, outputQuestionDTO, answers, 1, numberOfQuestions);
            return outputDTO;
        } catch (EntityNotFoundException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private void checkRequiredField(@Valid PlayerDTO player, Quiz quiz) {
        if (quiz.isMailRequired() && player.getMail() == null)
            throw new ResponseException("Mail is required.");

        if (quiz.isTelephoneNumberRequired() && player.getTelephoneNumber() == null)
            throw new ResponseException("Telephone number is required.");
    }

    private void checkNumberOfQuestionAndQuizState(Quiz quiz) {
        int numberOfQuestions = quiz.getQuestions().size();
        if (numberOfQuestions == 0 || quiz.getState() == QuizState.INACTIVE)
            throw new ResponseException("Quiz is inactive or have no question");
    }

    private Player mapCreatePlayerDTOToPlayer(@Valid PlayerDTO player) {
        Player newPlayer = new Player();
        newPlayer.setMail(player.getMail());
        newPlayer.setName(player.getName());
        newPlayer.setSurname(player.getSurname());
        newPlayer.setTelephoneNumber(player.getTelephoneNumber());
        return newPlayer;
    }

    public Player getOne(UUID playerId) {
        try {
            return playerRepository.getOne(playerId);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public List<PlayerOutputDTO> getPlayersByQuizId(UUID quizId) {
        try {
            Quiz quiz = quizService.getOne(quizId);
            List<Player> players = quiz.getPlayers();
            List<PlayerOutputDTO> playerOutputDTOS = new ArrayList<>();
            players.forEach(player -> playerOutputDTOS.add(this.mapPlayerToOutputDTO(player)));
            return playerOutputDTOS;
        } catch (EntityNotFoundException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public PlayerOutputDTO mapPlayerToOutputDTO(Player player) {
        PlayerOutputDTO playerOutputDTO = new PlayerOutputDTO();
        playerOutputDTO.setName(player.getName());
        playerOutputDTO.setSurname(player.getSurname());
        if (player.getMail() != null)
            playerOutputDTO.setMail(player.getMail());
        if (player.getTelephoneNumber() != null)
            playerOutputDTO.setTelephoneNumber(player.getTelephoneNumber());
        return playerOutputDTO;
    }
}
