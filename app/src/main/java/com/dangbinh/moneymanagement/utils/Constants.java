package com.dangbinh.moneymanagement.utils;

import android.graphics.drawable.Drawable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class Constants {

    public static String FIREBASE_URL = "https://prm391-demo.firebaseio.com/";
    public final static FirebaseAuth AUTH_INSTANCE = FirebaseAuth.getInstance();
    public final static FirebaseUser CURRENT_USER = AUTH_INSTANCE.getCurrentUser();
    public final static String EXIT = "exit";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            Pattern.CASE_INSENSITIVE);

    public static final Locale LOC_USA = Locale.US;

    // ic_health
    // ic_friends
    // ic_education
    // ic_debt
    Map<String, Drawable> cateImages = new HashMap<>();

}
