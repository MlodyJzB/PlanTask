package com.app.loginapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Arrays;
import java.util.List;


public class User {
    static User user = new User();

    private String username, password;
    private boolean dayMode;
    private String zipCode;

    public boolean isDayMode() {
        return dayMode;
    }

    public void setDayMode(boolean dayMode) {
        this.dayMode = dayMode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getChangedUsername() {
        return changedUsername;
    }

    public void setChangedUsername(String changedUsername) {
        this.changedUsername = changedUsername;
    }

    public String getChangedPassword() {
        return changedPassword;
    }

    public void setChangedPassword(String changedPassword) {
        this.changedPassword = changedPassword;
    }

    private String changedUsername, changedPassword;
    private final BooleanProperty usernameAvailable = new SimpleBooleanProperty(false);
    private final BooleanProperty usernameLongEnough = new SimpleBooleanProperty(false);
    private final BooleanProperty passwordMeetsConditions = new SimpleBooleanProperty(false);
    private final BooleanProperty passwordsMatch = new SimpleBooleanProperty(false);

    public boolean isPasswordChanged() {
        return passwordChanged.get();
    }

    public BooleanProperty passwordChangedProperty() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged.set(passwordChanged);
    }

    public boolean isUsernameChanged() {
        return usernameChanged.get();
    }

    public BooleanProperty usernameChangedProperty() {
        return usernameChanged;
    }

    public void setUsernameChanged(boolean usernameChanged) {
        this.usernameChanged.set(usernameChanged);
    }

    private final BooleanProperty passwordChanged = new SimpleBooleanProperty(false);

    private final BooleanProperty usernameChanged = new SimpleBooleanProperty(false);


    private User() {
        username = "";
        password = "";
    }

    public static User getInstance() {
        return user;
    }


    public boolean isUsernameLongEnough() { return usernameLongEnough.get(); }

    public BooleanProperty usernameLongEnoughProperty() { return usernameLongEnough; }

    public void setUsernameLongEnough(boolean usernameLongEnough) { this.usernameLongEnough.set(usernameLongEnough); }

    public boolean isUsernameAvailable() {
        return usernameAvailable.get();
    }

    public BooleanProperty usernameAvailableProperty() {
        return usernameAvailable;
    }

    public void setUsernameAvailable(boolean usernameAvailable) {
        this.usernameAvailable.set(usernameAvailable);
    }

    public boolean isPasswordMeetsConditions() {
        return passwordMeetsConditions.get();
    }

    public BooleanProperty passwordMeetsConditionsProperty() {
        return passwordMeetsConditions;
    }

    public void setPasswordMeetsConditions(boolean passwordMeetsConditions) { this.passwordMeetsConditions.set(passwordMeetsConditions); }

    public boolean isPasswordsMatch() {
        return passwordsMatch.get();
    }

    public BooleanProperty passwordsMatchProperty() {
        return passwordsMatch;
    }

    public void setPasswordsMatch(boolean passwordsMatch) {
        this.passwordsMatch.set(passwordsMatch);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void Exit() {
        System.exit(0);
    }

    //Password should contain at least 8 characters, one capital letter and one number
    public static List<Boolean> checkPassword(String str, int minChar) {
        char ch;
        boolean minCharFlag = str.length() >= minChar;
        boolean capitalFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch))
                capitalFlag = true;
            else if (Character.isDigit(ch))
                numberFlag = true;
        }
        return Arrays.asList(minCharFlag, capitalFlag, numberFlag);
    }

}
