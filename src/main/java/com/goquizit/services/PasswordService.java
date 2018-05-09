package com.goquizit.services;

import com.goquizit.DTO.PasswordForgotDTO;
import com.goquizit.DTO.PasswordResetDTO;
import com.goquizit.model.User;
import com.goquizit.repository.PasswordResetTokenRepository;
import com.goquizit.utils.Mail;
import com.goquizit.utils.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordService {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(PasswordService.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public PasswordForgotDTO processForgotPasswordForm(@Valid PasswordForgotDTO form, HttpServletRequest request) throws UsernameNotFoundException {

        User user = userService.findByEmail(form.getEmail());
        if (user == null) {
            log.info("No user found for given email address");
            throw new UsernameNotFoundException("No user with email '" + form.getEmail() + "' found!");
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);

        Mail mail = new Mail();
        mail.setFrom("GoQuizIt.NoReply@gmail.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "GoQuizIt");
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);

        return form;
    }

    @Transactional
    public User handlePasswordReset(@Valid PasswordResetDTO form) {

        PasswordResetToken token = tokenRepository.findByToken(form.getToken());
        User user = token.getUser();
        String updatedPassword = passwordEncoder.encode(form.getPassword());
        userService.changeUserPassword(updatedPassword, user);
        tokenRepository.delete(token);
        return user;
    }
}
