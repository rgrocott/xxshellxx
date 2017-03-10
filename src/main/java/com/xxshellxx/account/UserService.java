package com.xxshellxx.account;

import java.security.Principal;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.xxshellxx.email.EmailService;

@Service
public class UserService implements UserDetailsService {
	public static String Test = "false";
	final static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private EmailService emailService;

	@PostConstruct
	protected void initialize() {
		if (Test.equals("true")) {

			Account user1 = new Account("user", "demo", "ROLE_USER", true);
			Account user2 = new Account("customer1@bank1.com", "demo", "ROLE_USER", true);
			Account user3 = new Account("customer2@bank1.com", "demo", "ROLE_USER", false);
			Account user4 = new Account("customer3@bank2.com", "demo", "ROLE_USER", false);
			Account user5 = new Account("customer4@bank2.com", "demo", "ROLE_USER", true);
			Account user6 = new Account("customer5@bank3.com", "demo", "ROLE_USER", true);
			Account user7 = new Account("admin", "admin", "ROLE_ADMIN", true);
			Account user8 = new Account("super", "admin", "ROLE_ADMIN", true);

			accountRepository.save(user1);
			accountRepository.save(user2);
			accountRepository.save(user3);
			accountRepository.save(user4);
			accountRepository.save(user5);
			accountRepository.save(user6);
			accountRepository.save(user7);
			accountRepository.save(user8);
		}

		if (!Test.equals("true")) {
			printBanner2();
		}

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username);

		if (account == null) {
			throw new UsernameNotFoundException("user not found");
		}

		if (!account.isApproved()) {
			throw new RuntimeException("Not approved");
		}

		// up to 3 login attempts
		if (account.getLoginAttempts() >= 3) {
			try {
				emailService.sendInternalNotification("Client account locked out",
						String.format("Client %s has been locked out after too many failed login attempts.", account.getEmail()));
			} catch (Exception e) {
				logger.error("Failed to send notification email for client account lockout", e);
			}
			throw new RuntimeException("Too many failed login attempts - please contact support for a password reset");
		}

		return createUser(account);
	}

	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}

	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));
	}

	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

	public void loginFailed(String name) {
		Account account = accountRepository.findByEmail(name);
		if (account != null) {
			account.setLoginAttempts(account.getLoginAttempts() + 1);
			accountRepository.update(account);
		}
	}

	public void loginSucceeded(String name) {
		Account account = accountRepository.findByEmail(name);
		account.setLoginAttempts(0);
		accountRepository.update(account);
	}

	private static void printBanner2() {
		logger.info("\n\n"
				+ "   ____    ___                               ____                                                              \n"
				+ "  /\\  _`\\ /\\_ \\                             /\\  _`\\                                                            \n"
				+ "  \\ \\ \\/\\_\\//\\ \\      __     __     _ __    \\ \\ \\/\\_\\    ___     ___ ___   _____   _ __    __    ____    ____  \n"
				+ "   \\ \\ \\/_/_\\ \\ \\   /'__`\\ /'__`\\  /\\`'__\\   \\ \\ \\/_/_  / __`\\ /' __` __`\\/\\ '__`\\/\\`'__\\/'__`\\ /',__\\  /',__\\ \n"
				+ "    \\ \\ \\L\\ \\\\_\\ \\_/\\  __//\\ \\L\\.\\_\\ \\ \\/     \\ \\ \\L\\ \\/\\ \\L\\ \\/\\ \\/\\ \\/\\ \\ \\ \\L\\ \\ \\ \\//\\  __//\\__, `\\/\\__, `\\\n"
				+ "     \\ \\____//\\____\\ \\____\\ \\__/.\\_\\\\ \\_\\      \\ \\____/\\ \\____/\\ \\_\\ \\_\\ \\_\\ \\ ,__/\\ \\_\\\\ \\____\\/\\____/\\/\\____/\n"
				+ "      \\/___/ \\/____/\\/____/\\/__/\\/_/ \\/_/       \\/___/  \\/___/  \\/_/\\/_/\\/_/\\ \\ \\/  \\/_/ \\/____/\\/___/  \\/___/ \n"
				+ "                                                                             \\ \\_\\                             \n"
				+ "                                                                              \\/_/                             \n\n");
	}

}
