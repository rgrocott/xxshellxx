package com.xxshellxx.signup;

import org.hibernate.validator.constraints.NotBlank;

public class ChangePasswordForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";


    @NotBlank(message = ChangePasswordForm.NOT_BLANK_MESSAGE)
    private String password;

    @NotBlank(message = ChangePasswordForm.NOT_BLANK_MESSAGE)
    private String newPassword1;

    @NotBlank(message = ChangePasswordForm.NOT_BLANK_MESSAGE)
    private String newPassword2;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
}
