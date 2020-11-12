package com.dangbinh.moneymanagement.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dangbinh on 9/11/2020.
 */
@Entity(tableName = "transactions")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    private String transId;
    @ColumnInfo(name = "amount", typeAffinity = ColumnInfo.REAL)
    private double amount;
    @ColumnInfo(name = "category", typeAffinity = ColumnInfo.TEXT)
    private String category;
    @ColumnInfo(name = "note", typeAffinity = ColumnInfo.TEXT)
    private String note;
    @ColumnInfo(name = "date", typeAffinity = ColumnInfo.TEXT)
    private String date;
    private SimpleDateFormat sdf;
    private Date currentDate;
    private String username;

    public static String FIREBASE_URL = "https://managemymoney.firebaseio.com/";
    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public Transaction() {
    }

    public Transaction(String transId, double amount, String category, String note, String date) {
        this.transId = transId;
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
        this.username = Account.CURRENT_USER.getDisplayName();
    }

    public ArrayList<Transaction> getAllTransactions(Account account) {
        // Populate to recyclerview
        ArrayList<Transaction> allTrans = new ArrayList<>();
        return allTrans;
    }

    public ArrayList<Transaction> getAllTransByCategory(Account account) {
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

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static String getFirebaseUrl() {
        return FIREBASE_URL;
    }

    public static void setFirebaseUrl(String firebaseUrl) {
        FIREBASE_URL = firebaseUrl;
    }

    public static DatabaseReference getTransRef() {
        return FirebaseDatabase.getInstance().getReference(Account.CURRENT_USER.getUid());
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("category", category);
        result.put("date", date);
        result.put("note", note);
        result.put("username", username);

        return result;
    }

    public boolean postTransaction() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Account.CURRENT_USER.getUid());
        ref.child("transactions").push().setValue(this);
        return true;
    }

    public boolean modify(String transId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Account.CURRENT_USER.getUid());
        Map<String, Object> trans = this.toMap();
        Map<String, Object> transUpdate = new HashMap<>();
        transUpdate.put("/"+transId+"/", trans);
        ref.child("transactions").updateChildren(transUpdate);
        return true;
    }

    public void remove(String s) {
        if (!s.equals(null)) {
            getTransRef().child("transactions").child(s).removeValue();
        }
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}