package com.xxshellxx.signup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxshellxx.account.*;
import com.xxshellxx.email.EmailService;
import com.xxshellxx.support.web.*;

@Controller
public class SignupController {

	private static final String SIGNUP_VIEW_NAME = "signup/signup";
	private static final String NEW_BUSINESS_FORM = "signup/newBusiness";
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private EmailService emailService;


	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute(new SignupForm());
		return SIGNUP_VIEW_NAME;
	}

	@RequestMapping(value = "/signup/admin", method = RequestMethod.GET)
	public String signupAdmin(Model model) {
		model.addAttribute(new SignupForm());
		return "signup/signupAdmin";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			return SIGNUP_VIEW_NAME;
		}
		Account account = signupForm.createAccount();
		if (account == null) {
			MessageHelper.addErrorAttribute(ra, "signup.bademail");
			errors.reject("signup.bademail");
			errors.rejectValue("email", "signup.bademail", "Email must belong to a registered Counterparty");
			return SIGNUP_VIEW_NAME;
		}
		Account status = accountRepository.save(account);
		try {
			LocalDateTime signupTime = LocalDateTime.now();
			emailService.sendInternalNotification("New client signup",
					String.format(
							"New client account created. Email address %s registered at %s. Please log in to xxshellxx to authorise the account.",
							status.getEmail(), signupTime.format(dateTimeFormatter)));
		} catch (Exception e) {
			logger.error("Error occurred sending signup email notification", e);
		}

		// see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
		MessageHelper.addSuccessAttribute(ra, "signup.success");
		return "redirect:/dashboard";
	}

	@RequestMapping(value = "/signup/admin", method = RequestMethod.POST)
	public String signupAdmin(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		String email = signupForm.getEmail();
		String[] emailParts = email.split("@");
		if (emailParts.length < 2) {
			errors.rejectValue("email", "AdminForm.email");
		}
		
		if (errors.hasErrors()) {
			return "signup/signupAdmin";
		}
		Account account = new Account(signupForm.getEmail(), signupForm.getPassword(), "ROLE_ADMIN");
		Account status = accountRepository.save(account);
		// see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
		MessageHelper.addSuccessAttribute(ra, "signup.success");
		return "redirect:/dashboard";
	}

}
