package com.goquizit.repository;

import com.goquizit.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @Query(value = "select * from question where quiz = :quizId and index = :index", nativeQuery = true)
    Question findByQuizAndIndex(@Param("quizId") UUID quizId, @Param("index") int index);

    @Query(value = "select count(*) from question where quiz = :quizId", nativeQuery = true)
    int getNumberOfQuestionsByQuizId(@Param("quizId") UUID quizId);
}
