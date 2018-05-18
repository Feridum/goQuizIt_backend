package com.goquizit.repository;

import com.goquizit.model.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer, UUID> {
}
