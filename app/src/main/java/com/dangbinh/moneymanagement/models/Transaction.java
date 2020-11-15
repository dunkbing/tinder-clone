package com.dangbinh.moneymanagement.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;

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
    private Type type;
    private static double balance = 0;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private SimpleDateFormat sdf;
    private Date currentDate;
    private String username;

    public static String FIREBASE_URL = "https://managemymoney.firebaseio.com/";
    static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public Transaction() {
        transId = null;
    }

    public Transaction(String transId, double amount, String category, String note, String date) {
        this.transId = transId;
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
        this.username = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        return FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("category", category);
        result.put("date", date);
        result.put("note", note);
        result.put("username", username);
        result.put("type", type.toString());

        return result;
    }

    public static void adjustBalance(String balance) {
        try {
            Transaction.balance = Double.parseDouble(balance);
            getTransRef().child("wallet").setValue(balance);
        } catch (Exception e) {
            Transaction.balance = 0;
            getTransRef().child("wallet").setValue(0);
        }
    }

    public static double getBalance() { return balance; }

    public boolean postTransaction() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.child("transactions").push().runTransaction(new com.google.firebase.database.Transaction.Handler() {
            @NonNull
            @Override
            public com.google.firebase.database.Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Transaction transaction = currentData.getValue(Transaction.class);
                if (transaction == null) {
                    currentData.setValue(toMap());
                }
                return com.google.firebase.database.Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
        return true;
    }

    public boolean setWalletBalance(double balance) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.child("wallet").setValue(balance);
        return true;
    }

    public boolean modify(String transId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    public enum Type {
        INCOME,
        EXPENSE,
        DEBT,
        LOAN
    }
}