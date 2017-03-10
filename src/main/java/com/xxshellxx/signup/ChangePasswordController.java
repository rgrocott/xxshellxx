package com.xxshellxx.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxshellxx.account.Account;
import com.xxshellxx.account.AccountPasswordHistory;
import com.xxshellxx.account.AccountRepository;
import com.xxshellxx.account.UserService;
import com.xxshellxx.support.web.MessageHelper;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by vassilischazapis on 02/07/15.
 */
@Controller
public class ChangePasswordController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/change-password")
    public String changePassword(Principal principal, Model model) {
        model.addAttribute(new ChangePasswordForm());
        model.addAttribute("user", principal.getName());

        return "signin/changePassword";
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String processChangePassword(Principal principal, @Valid @ModelAttribute ChangePasswordForm changePasswordForm, Errors errors, RedirectAttributes ra) {
        if (errors.hasErrors()) {
            return "signin/changePassword";
        }

        if (changePasswordForm.getPassword().equals(changePasswordForm.getNewPassword1())) {
            errors.rejectValue("newPassword1", "changepassword.same");
            return "signin/changePassword";
        }

        if (!changePasswordForm.getNewPassword1().equals(changePasswordForm.getNewPassword2())) {
            errors.rejectValue("newPassword2", "changepassword.notsame");
            return "signin/changePassword";
        }

        Account account = accountRepository.findByEmail(principal.getName());

        account = accountRepository.changePassword(account, changePasswordForm.getPassword(), changePasswordForm.getNewPassword1());

        if (account == null) {
            return "signin/changePassword";
        }

        MessageHelper.addSuccessAttribute(ra, "changepassword.success");
        return "redirect:/dashboard";
    }

}
