package com.goquizit.services;

import com.goquizit.DTO.PlayerDTO;
import com.goquizit.DTO.QuestionWithPlayerIdDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.exception.ResponseException;
import com.goquizit.model.Player;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.model.QuizState;
import com.goquizit.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    QuizService quizService;

    @Autowired
    QuestionService questionService;


    public QuestionWithPlayerIdDTO create(@Valid PlayerDTO player, @Valid UUID quizId) {
        Player newPlayer = this.mapCreatePlayerDTOToPlayer(player);
        Quiz quiz = quizService.getOne(quizId);
        quiz.getPlayers().add(newPlayer);
        checkRequiredField(player, quiz);
        checkNumberOfQuestionAndQuizState(quiz);
        Quiz tempQuiz = quizService.save(quiz);
        Question question = quiz.getQuestionSet().get(0);

        QuestionOutputDTO outputQuestionDTO = questionService.mapQuestionToOutput(question, quizId);
        UUID playerId = tempQuiz.getPlayers().get(tempQuiz.getPlayers().size() - 1).getPlayerId();

        QuestionWithPlayerIdDTO outputDTO = new QuestionWithPlayerIdDTO(playerId, outputQuestionDTO);
        return outputDTO;
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
}
