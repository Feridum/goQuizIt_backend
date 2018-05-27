package com.goquizit.repository;

import com.goquizit.model.PlayerAnswer;
import com.goquizit.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer, UUID> {

    @Query(value = "select * from playersanswers where player = :playerId AND question = :questionId", nativeQuery = true)
    List<PlayerAnswer> getPlayerAnswersByByPlayerAndQuestion(@Param("questionId") UUID questionId, @Param("playerId") UUID playerId);

    List<PlayerAnswer> getByQuestion(Question question);
}
