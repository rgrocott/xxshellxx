package com.xxshellxx.signup;

import javax.validation.constraints.NotNull;

/**
 * Created by vassilischazapis on 06/07/15.
 */
public class BusinessForm {
    @NotNull(message="BusinessForm.name.validation")
    private String name;

    @NotNull(message="BusinessForm.emailExtension.validation")
    private String emailExtension;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailExtension() {
        return emailExtension;
    }

    public void setEmailExtension(String emailExtension) {
        this.emailExtension = emailExtension;
    }
}
