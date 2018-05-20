package com.goquizit.services;

import com.goquizit.DTO.PlayerDTO;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;
import com.goquizit.DTO.outputDTO.PlayerAnswerOutputDTO;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Player;
import com.goquizit.model.PlayerAnswer;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlayerAnswerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    QuizService quizService;

    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerService answerService;

    public List<PlayerAnswerOutputDTO> getPlayerAnswersByQuestionId(@Valid UUID playerId,
                                                                    @Valid UUID quizId,
                                                                    @Valid UUID questionId) throws ResourceNotFoundException{
        Quiz quiz = quizService.getOne(quizId);
        List<PlayerDTO> playerList = playerService.getPlayersByQuizId(quizId);
        Player player = playerRepository.findById(playerId).orElseThrow( () -> new ResourceNotFoundException("Player", "id", playerId));

        if (!quiz.getPlayers().contains(player)) {
            throw new ResourceNotFoundException("Player", "id", playerId);
        }

        Question question = questionService.getOne(questionId);

        if (!quiz.getQuestions().contains(question)) {
            throw new ResourceNotFoundException("Question", "id", question);
        }

        List<PlayerAnswer> playerAnswerList = player.getPlayerAnswers();

        List<PlayerAnswerOutputDTO> playerAnswerOutputDTOS = new ArrayList<>();

        // TODO: map player answers to DTO

        return playerAnswerOutputDTOS;
    }

    public List<PlayerAnswerOutputDTO> updatePlayerAnswersByQuestionId(@Valid UUID playerId,
                                                @Valid UUID quizId,
                                                @Valid UUID questionId) {
        List<PlayerAnswerOutputDTO> playerAnswerOutputDTOS = new ArrayList<>();

        // TODO: map player answers to DTO

        return playerAnswerOutputDTOS;
    }

}
