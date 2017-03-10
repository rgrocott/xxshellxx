package com.xxshellxx.dashboard;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xxshellxx.account.Account;
import com.xxshellxx.account.AccountRepository;

@Controller
public class DashboardController {

    final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Principal principal, Model model) {

        if (principal == null) {
            return "home/homeNotSignedIn";
        }

        logger.info("User logged in: " + principal.getName());

        Account account = getAccount(principal);
        model.addAttribute("user", principal.getName());

        logger.debug("User Role : " + account.getRole());

        return "dashboard/dashboard";
    }

    private Account getAccount(Principal principal) {
        if (principal == null) {
            return null;
        }

        return accountRepository.findByEmail(principal.getName());
    }

}