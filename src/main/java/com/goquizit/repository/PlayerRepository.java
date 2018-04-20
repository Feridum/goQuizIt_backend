package com.goquizit.repository;

import com.goquizit.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Answer, Long> {
}

