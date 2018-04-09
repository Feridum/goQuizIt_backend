package com.goquizit.beta.repository;

import com.goquizit.beta.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
