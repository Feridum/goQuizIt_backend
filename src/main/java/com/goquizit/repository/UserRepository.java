package com.goquizit.repository;

import com.goquizit.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Quiz, Long> {
}
