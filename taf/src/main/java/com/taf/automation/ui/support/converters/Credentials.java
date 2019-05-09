package com.taf.automation.ui.support.converters;

public class Credentials {
    private String emailOrName;
    private String password;

    public Credentials(String credentials) {
        String[] cred = credentials.split(":");
        emailOrName = cred[0].trim();
        password = cred[1].trim();
    }

    public String getPassword() {
        return password;
    }

    public String getEmailOrName() {
        return emailOrName;
    }

    @Override
    public String toString() {
        return emailOrName + ":" + password;
    }

}
