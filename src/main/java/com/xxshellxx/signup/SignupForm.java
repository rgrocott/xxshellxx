package com.xxshellxx.signup;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import com.xxshellxx.account.Account;

@Component
public class SignupForm {

	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
	private static final String EMAIL_MESSAGE = "{email.message}";
	private static final String PASSWORD_MIN_MESSAGE = "{password.min.message}";
	private static final String PASSWORD_COMPLEX_MESSAGE = "{password.complex.message}";

	@NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	@Email(message = SignupForm.EMAIL_MESSAGE)
	private String email;

	@NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\\!\\?\\.\\,])(?=\\S+$).{8,}", message = SignupForm.PASSWORD_COMPLEX_MESSAGE)
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Account createAccount() {
		String email = getEmail();
		Account account = new Account(email, getPassword(), "ROLE_USER");
		account.setApproved(false);
		account.setLoginAttempts(0);
		return account;

	}
}
