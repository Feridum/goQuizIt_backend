package com.goquizit.repository;

import com.goquizit.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    @Query(value = "select count(*) from answer where answer_id = :answerId AND question = :questionId", nativeQuery = true)
    int checkAnswerIdInQuestion(@Param("answerId") UUID answerId, @Param("questionId") UUID questionId);
}
