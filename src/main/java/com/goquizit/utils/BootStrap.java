package com.goquizit.utils;

import com.goquizit.DTO.CreateUserDTO;
import com.goquizit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;

@Component
public class BootStrap {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(BootStrap.class);

    @Autowired
    UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        CreateUserDTO defUser = new CreateUserDTO("paczki@lozaaei.slack.com", "Paczek", "paczki123");
        try {
            userService.loadUserByUsername(defUser.getUsername());
            log.info("Creating new bootstrap user skipped. User " + defUser.getUsername() + " already exists.");
        } catch (UsernameNotFoundException e) {
            userService.create(defUser);
            log.info(defUser);
        }
    }
}