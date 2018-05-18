package com.goquizit.services;

import com.goquizit.DTO.CreateUserDTO;
import com.goquizit.model.Quiz;
import com.goquizit.model.User;
import com.goquizit.repository.UserRepository;
import com.goquizit.utils.BootStrap;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Null;

@Service
@Validated
public class UserService implements UserDetailsService {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;

    public User addUser(@Valid CreateUserDTO dto) {
        if (checkIfAlreadyExists(dto)) {
            log.info("Added new user '" + dto.getUsername() + "'.");
            return create(dto);
        } else {
            log.info("Creating new user skipped. User '" + dto.getUsername() + "' already exists.");
            return null;
        }
    }

    public boolean checkIfAlreadyExists(@Valid CreateUserDTO dto) {
        if (findByEmail(dto.getEmail()) != null || findByUsername(dto.getUsername()) != null) {
            return false;
        } else {
            return true;
        }
    }

    public User create(@Valid CreateUserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setRegistrationDate(new Date());
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        user.setPassword(encodedPassword);
        repository.save(user);
        return user;
    }

    public void changeUserPassword(String updatedPassword, User user) {
        user.setPassword(updatedPassword);
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user with username '" + username + "' found!");
        }

        return user;
    }

    public Quiz saveQuiz(Quiz quiz)
    {
        UUID userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        User user = repository.getOne(userId);
        user.getQuizList().add(quiz);
        User updatedUser = repository.save(user);
        int index = updatedUser.getQuizList().size() -1;
        return updatedUser.getQuizList().get(index);
    }
}
