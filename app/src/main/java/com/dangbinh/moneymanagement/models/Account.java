package com.dangbinh.moneymanagement.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account {
    private String uid;
    private String email;
    private String password;
    public final static FirebaseAuth AUTH_INSTANCE = FirebaseAuth.getInstance();
    public final static FirebaseUser CURRENT_USER = AUTH_INSTANCE.getCurrentUser();

    public Account() {}
    public Account(String uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
