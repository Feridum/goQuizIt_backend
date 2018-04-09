package com.goquizit.beta.utils;

import com.goquizit.beta.dto.UserCreateDTO;
import com.goquizit.beta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BootStrap {


    @Autowired
    UserService userService;
    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        UserCreateDTO defUser = new UserCreateDTO("John","Nowicki","john.nowicki@itsilesia.com");
        userService.create(defUser);
    }

}