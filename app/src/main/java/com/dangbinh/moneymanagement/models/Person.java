package com.dangbinh.moneymanagement.models;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class Person {
    private String Email;
    private String pass;
    private String dob;

    public Person() {

    }

    public Person(String e, String p) {
        setEmail(e);
        setPass(p);
    }

    public Person(String e, String p, String d) {
        setEmail(e);
        setPass(p);
        setDob(d);
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}