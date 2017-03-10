package com.xxshellxx.dashboard;

import java.util.List;

import com.xxshellxx.account.Account;

/**
 * Created by vassilischazapis on 02/07/15.
 */
public class DashboardForm {
    private List<Account> accountList;

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
