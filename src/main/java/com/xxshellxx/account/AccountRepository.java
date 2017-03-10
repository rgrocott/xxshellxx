package com.xxshellxx.account;

import javax.persistence.*;
import javax.inject.Inject;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AccountRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));

        AccountPasswordHistory accountPasswordHistory = new AccountPasswordHistory();
        accountPasswordHistory.setPassword(account.getPassword());
        accountPasswordHistory.setLastUsed(LocalDateTime.now());

        account.getAccountPasswordHistories().add(accountPasswordHistory);

        entityManager.persist(accountPasswordHistory);
        entityManager.persist(account);
		return account;
	}



	@Transactional
	public Account update(Account account) {
        entityManager.merge(account);
		return account;
	}

	@Transactional
	public void remove(Account account) {
		entityManager.remove(account);
	}
	
	public Account findByEmail(String email) {
		try {
			return entityManager.createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
					.setParameter("email", email)
					.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}

    @Transactional
    public boolean forceChangePassword(Account account, String password) {
        account.setPassword(passwordEncoder.encode(password));
        this.update(account);
        return true;
    }


	@Transactional
    public Account changePassword(Account account, String oldPassword, String newPassword) {
        if (passwordEncoder.matches(oldPassword, account.getPassword())) {

            // check if using old password
            boolean match = account.getAccountPasswordHistories().stream()
                    // sorted in reverse order
                    .sorted((a, b) -> b.getLastUsed().compareTo(a.getLastUsed()))
                    // take top 10
                    .limit(10)
                    // find password
                    .anyMatch(a -> a.getPassword().equals(passwordEncoder.encode(newPassword)));

            if (match) {
                return null;
            }

            account.setPassword(passwordEncoder.encode(newPassword));

            AccountPasswordHistory accountPasswordHistory = new AccountPasswordHistory();
			accountPasswordHistory.setPassword(passwordEncoder.encode(newPassword));
			accountPasswordHistory.setLastUsed(LocalDateTime.now());

			account.getAccountPasswordHistories().add(accountPasswordHistory);

            // get rid of older passwords
            if (account.getAccountPasswordHistories().size() > 10) {
                account.getAccountPasswordHistories().stream()
                        // sorted in reverse order
                        .sorted((a, b) -> b.getLastUsed().compareTo(a.getLastUsed()))
                        // skip top 10
                        .skip(10)
                        // remove password from list and database
                        .forEach( a -> {
                            account.getAccountPasswordHistories().remove(a);
                            entityManager.remove(a);
                        } );
            }

            entityManager.persist(accountPasswordHistory);
            this.update(account);
            return account;
        }
        else {
            return null;
        }
    }

	public Account findById(Long id) {
		try {
			return entityManager.createNamedQuery(Account.FIND_BY_ID, Account.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException e) {
			return null;
		}
	}

	public List<Account> findAll() {
		try {
			return entityManager.createNamedQuery(Account.FIND_ALL, Account.class)
					.getResultList();
		} catch (PersistenceException e) {
			return null;
		}
	}
}
