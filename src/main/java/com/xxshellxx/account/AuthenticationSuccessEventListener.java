package com.xxshellxx.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserService userService;

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                e.getAuthentication().getDetails();

        userService.loginSucceeded(e.getAuthentication().getName());
    }
}
