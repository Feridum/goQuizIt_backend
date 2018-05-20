package com.goquizit.services;

import com.goquizit.DTO.CreateUpdatePlayerAnswerDTO;
import com.goquizit.DTO.PlayerAnswerDTO;
import com.goquizit.DTO.PlayerDTO;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;
import com.goquizit.DTO.outputDTO.PlayerAnswerOutputDTO;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.*;
import com.goquizit.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
                                                                    @Valid UUID questionId) throws ResourceNotFoundException{
        Player player = playerRepository.findById(playerId).orElseThrow( () -> new ResourceNotFoundException("Player", "id", playerId));
        Question question = questionService.getOne(questionId);
        List<Answer> answerList = question.getAnswers();
        List<PlayerAnswer> playerAnswerList = player.getPlayerAnswers();
        List<PlayerAnswerOutputDTO> playerAnswerOutputDTOList = new ArrayList<>();

        for (Answer answer : answerList) {
            PlayerAnswer playerAnswerTemp = null;

            for (PlayerAnswer playerAnswer : playerAnswerList) {
                if (playerAnswer.getId().equals(answer.getAnswerId()) ||
                        playerAnswer.getId() == (answer.getAnswerId())) {
                    playerAnswerTemp = playerAnswer;
                    playerAnswerOutputDTOList.add(mapPlayerAnswerToPlayerAnswerOutputDTO(playerAnswerTemp));
                }
            }
        }

        return playerAnswerOutputDTOList;
    }

    public List<PlayerAnswerOutputDTO> updatePlayerAnswersByQuestionId(@Valid UUID playerId,
                                                                       @Valid UUID questionId,
                                                                       @Valid @RequestBody CreateUpdatePlayerAnswerDTO createUpdatePlayerAnswerDTO ) {
        Player player = playerRepository.findById(playerId).orElseThrow( () -> new ResourceNotFoundException("Player", "id", playerId));
        Question question = questionService.getOne(questionId);
        List<PlayerAnswerDTO> playerAnswerDTOList = createUpdatePlayerAnswerDTO.getAnswersGivenByUser();
        List<PlayerAnswer> playerAnswerList = player.getPlayerAnswers();
        List<PlayerAnswerOutputDTO> playerAnswerOutputDTOS = new ArrayList<>();


        for (PlayerAnswerDTO playerAnswerDTO : playerAnswerDTOList) {
            PlayerAnswer playerAnswerTemp = null;

            for (PlayerAnswer playerAnswer : playerAnswerList) {
                if (playerAnswer.getId().equals(playerAnswerDTO.getId()) ||
                        playerAnswer.getId() == (playerAnswerDTO.getId())) {
                    playerAnswerTemp = playerAnswer;
                }
            }

            if (playerAnswerTemp == null) {
                playerAnswerTemp = new PlayerAnswer();
                playerAnswerTemp.setId(playerAnswerDTO.getId());
                playerAnswerTemp.setValue("Yes");
                playerAnswerTemp.setPlayer(player);
            }

            playerAnswerTemp.setValue("Yes");
        }

        return playerAnswerOutputDTOS;
    }

    private PlayerAnswerDTO mapPlayerAnswerToPlayerAnswerDTO(@Valid PlayerAnswer playerAnswer) {
        PlayerAnswerDTO playerAnswerDTO = new PlayerAnswerDTO();
        playerAnswerDTO.setId(playerAnswer.getId());
        return playerAnswerDTO;
    }

    private PlayerAnswerDTO mapAnswerToPlayerAnswerDTO(@Valid Answer answer) {
        PlayerAnswerDTO playerAnswerDTO = new PlayerAnswerDTO();
        playerAnswerDTO.setId(answer.getAnswerId());
        return playerAnswerDTO;
    }

    private PlayerAnswerOutputDTO mapPlayerAnswerToPlayerAnswerOutputDTO(@Valid PlayerAnswer playerAnswer) {
        PlayerAnswerOutputDTO playerAnswerOutputDTO = new PlayerAnswerOutputDTO();
        playerAnswerOutputDTO.setId(playerAnswer.getId());
        playerAnswerOutputDTO.setPlayer(playerAnswer.getPlayer());
        playerAnswerOutputDTO.setValue(playerAnswer.getValue());
        return playerAnswerOutputDTO;
    }

}
