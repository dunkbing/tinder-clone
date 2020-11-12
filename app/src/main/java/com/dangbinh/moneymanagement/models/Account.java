package com.dangbinh.moneymanagement.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@Entity(tableName = "accounts")
public class Account {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    private String uid;
    @NonNull
    @ColumnInfo(name = "email", typeAffinity = ColumnInfo.TEXT)
    private String email;
    @NonNull
    @ColumnInfo(name = "password", typeAffinity = ColumnInfo.TEXT)
    private String password;

    // Constant
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

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
