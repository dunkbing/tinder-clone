package com.dangbinh.moneymanagement.models;

import com.dangbinh.moneymanagement.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class Transaction {
    private double amount;
    private String category;
    private String note;
    private String date;
    private Map<String, String> location;
    SimpleDateFormat sdf;
    private Date current_date;
    private String user_name;

    public static String FIREBASE_URL = "https://managemymoney.firebaseio.com/";
    // static Firebase ref = new Firebase(Constants.FIREBASE_URL);

    public Transaction() {
    }


    public Transaction(double amount, String category, String note, String date, Map location) {
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
        this.location = location;
        this.user_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public boolean postTransaction() {
        // Firebase trans_ref = ref.child("Transactions/" + ref.getAuth().getUid());
        // open request
        // send data
        // trans_ref.push().setValue(this);
        // uid = trans_ref.getKey();
        return true;
    }

    public ArrayList<Transaction> getAllTransactions(Person p) {
        // Populate to recyclerview
        ArrayList<Transaction> all_trans = new ArrayList<>();

        return all_trans;
    }

    public ArrayList<Transaction> getAllTransByCategory(Person p) {
        // Populate to graph
        ArrayList<Transaction> all_trans = new ArrayList<>();

        return all_trans;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, String> getLocation() {
        return location;
    }

    public void setLocation(Map<String, String> location) {
        this.location = location;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Date getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(Date current_date) {
        this.current_date = current_date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public static String getFirebaseUrl() {
        return FIREBASE_URL;
    }

    public static void setFirebaseUrl(String firebaseUrl) {
        FIREBASE_URL = firebaseUrl;
    }

    /*public static Firebase getRef() {
        return ref;
    }*/

    /*public static void setRef(Firebase ref) {
        com.dangbinh.moneymanagement.Application.Transaction.ref = ref;
    }*/

    /*public void modify(String s) {
        if (!s.equals(null)) {
            Firebase trans_ref = new Firebase(s);

            trans_ref.setValue(this);
        }
    }*/

    /*public void remove(String s) {
        if (!s.equals(null)) {
            Firebase trans_ref = new Firebase(s);

            trans_ref.removeValue();
        }
    }*/
}