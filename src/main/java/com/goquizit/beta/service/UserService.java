package com.goquizit.beta.service;

import com.goquizit.beta.dto.UserCreateDTO;
import com.goquizit.beta.dto.UserListDTO;
import com.goquizit.beta.entity.User;
import com.goquizit.beta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User create(@Valid UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        repository.save(user);
        return user;
    }

    public User findById(@Valid UserListDTO dto) {
        Optional<User> user = repository.findById(dto.getId());
        return user.isPresent() ? user.get() : null;
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }
}
