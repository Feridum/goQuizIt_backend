package com.goquizit.controller;

import com.goquizit.DTO.PasswordForgotDTO;
import com.goquizit.DTO.PasswordResetDTO;
import com.goquizit.model.User;
import com.goquizit.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @RequestMapping("/api/forgot-password")
    @PostMapping
    public PasswordForgotDTO processForgotPasswordForm(@Valid @RequestBody PasswordForgotDTO form, HttpServletRequest request) {
        return passwordService.processForgotPasswordForm(form, request);
    }

    @RequestMapping("/api/reset-password")
    @PostMapping
    public User handlePasswordReset(@Valid @RequestBody PasswordResetDTO form) {
        return passwordService.handlePasswordReset(form);
    }
}
