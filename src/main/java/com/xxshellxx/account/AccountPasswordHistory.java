package com.xxshellxx.account;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NamedQueries({
        @NamedQuery(name = AccountPasswordHistory.OLD_PASSWORD_LIST, query =
                "select aph from Account a join a.accountPasswordHistories aph " +
                "where a.id = :id " +
                "order by aph.lastUsed")
})
public class AccountPasswordHistory {
    public static final String OLD_PASSWORD_LIST = "AccountPasswordHistory.exists";

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @JsonIgnore
    private String password;

    private LocalDateTime lastUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}
