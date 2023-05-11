package com.example.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.example.myapp.repositories.UserRepository;
import com.example.myapp.security.LoginAttemptService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener implements 
  ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            loginAttemptService.loginFailed(request.getRemoteAddr(), userRepository.findByUsername(e.getAuthentication().getName()));
        } else {
            loginAttemptService.loginFailed(xfHeader.split(",")[0], userRepository.findByName(e.getAuthentication().getName()));
        }
    }
}
