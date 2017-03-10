package com.xxshellxx.account;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
@NamedQueries({
    @NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where LOWER(a.email) = LOWER(:email)"),
	@NamedQuery(name = Account.FIND_BY_ID, query = "select a from Account a where a.id = :id"),
    @NamedQuery(name = Account.FIND_ALL, query = "select a from Account a"),
	@NamedQuery(name = Account.FIND_ALL_CUSTOMERS, query = "select a from Account a where a.role = 'ROLE_USER'")
})
public class Account implements java.io.Serializable, Comparable<Account> {

	public static final String FIND_BY_EMAIL = "Account.findByEmail";
	public static final String FIND_BY_ID = "Account.findById";
    public static final String FIND_ALL = "Account.findAll";
	public static final String FIND_ALL_CUSTOMERS = "Account.findAllCustomers";

	@Override
	public int compareTo(Account a) {
		if (this.email.equals(a.email)) {
			return 0;
		}

		if (this.role.compareTo(a.role) == 0) {
			return this.email.compareTo(a.email);
		}
		else {
			return this.role.compareTo(a.role);
		}
	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	private String password;

	private boolean approved;

	private int loginAttempts = 0;

	private String role = "ROLE_USER";


	@OneToMany(fetch = FetchType.EAGER)
    @Cascade(value = CascadeType.ALL)
	private List<AccountPasswordHistory> accountPasswordHistories = new ArrayList<>();

    protected Account() {

	}
	
	public Account(String email, String password, String role) {
		this.setEmail(email);
		this.setPassword(password);
		this.setRole(role);
		this.setApproved(false);
	}

	public Account(String email, String password, String role, boolean approved) {
		this.setEmail(email);
		this.setPassword(password);
		this.setRole(role);
		this.setApproved(approved);
	}

	public Long getId() {
		return id;
	}

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public List<AccountPasswordHistory> getAccountPasswordHistories() {
		return accountPasswordHistories;
	}

	public void setAccountPasswordHistory(List<AccountPasswordHistory> accountPasswordHistories) {
		this.accountPasswordHistories = accountPasswordHistories;
	}
}
