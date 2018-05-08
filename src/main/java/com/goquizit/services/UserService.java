package com.goquizit.services;

import com.goquizit.DTO.CreateUserDTO;
import com.goquizit.model.User;
import com.goquizit.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@Validated
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public User create(@Valid CreateUserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
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

    public Iterable<User> findAll() {
        return repository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user with username '" + username + "' found!");
        }

        return user;
    }


}
