package com.dangbinh.dinter.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class UserDataGrabberUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    private static final String GOOGLE_ACCOUNT_TYPE = "com.google";

    public static ArrayAdapter<String> getUserDetails(Context ctx) {
        Account[] accounts = AccountManager.get(ctx).getAccountsByType(GOOGLE_ACCOUNT_TYPE);
//        Set<String> emailSet = new HashSet<String>();
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account: accounts) {
            if (EMAIL_PATTERN.matcher(account.name).matches()) {
                possibleEmails.add(account.name);
            }
        }
        return new ArrayAdapter<>(ctx, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(possibleEmails));
    }

}