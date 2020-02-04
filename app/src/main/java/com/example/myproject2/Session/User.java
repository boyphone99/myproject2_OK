package com.example.myproject2.Session;

import java.util.Date;

public class User {
    public String username;
    Date sessionExpiryDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUsername() {
        return username;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
