package com.xxshellxx.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private UserService userService;

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        userService.loginFailed(e.getAuthentication().getName());
    }
}