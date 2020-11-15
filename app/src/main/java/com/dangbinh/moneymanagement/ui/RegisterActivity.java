package com.dangbinh.moneymanagement.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.models.Account;
import com.dangbinh.moneymanagement.models.Transaction;
import com.dangbinh.moneymanagement.utils.TaskRunner;
import com.dangbinh.moneymanagement.utils.UiUtils;
import com.dangbinh.moneymanagement.utils.UserDataGrabberUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A Register screen that offers Register via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    FirebaseAuth mAuth;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressBar progressBar;
    private View mRegisterFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        setupActionBar();
        // Set up the Register form.
        mEmailView = findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_NULL) {
                attemptRegister();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = findViewById(R.id.button_signin);
        mEmailSignInButton.setOnClickListener(view -> attemptRegister());

        mRegisterFormView = findViewById(R.id.Register_form);
        progressBar = findViewById(R.id.progress_bar_register);

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        LoaderManager.getInstance(this).initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                return new CursorLoader(RegisterActivity.this,
                        // Retrieve data rows for the device user's 'profile' contact.
                        Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                                ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                        // Select only email addresses.
                        ContactsContract.Contacts.Data.MIMETYPE +
                                " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                .CONTENT_ITEM_TYPE},

                        // Show primary email addresses first. Note that there won't be
                        // a primary email address if the user hasn't specified one.
                        ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                List<String> emails = new ArrayList<>();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    emails.add(cursor.getString(ProfileQuery.ADDRESS));
                    cursor.moveToNext();
                }
                emails.add("test@vip.c");
                addEmailsToAutoComplete(emails);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
        mEmailView.setAdapter(UserDataGrabberUtils.getUserDetails(this));
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> {
                        requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or Register the account specified by the Register form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual Register attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the Register attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt Register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Register attempt.
            UiUtils.showProgress(mRegisterFormView, progressBar, true, getResources().getInteger(android.R.integer.config_shortAnimTime));
            TaskRunner.run(new UserRegisterTask(email, password));
        }
    }

    private boolean isEmailValid(String email) {
        // TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        // TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the Register form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * Represents an asynchronous Register/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask implements Callable<Void> {
        Account account;

        UserRegisterTask(String email, String password) {
            account = new Account();
            account.setEmail(email);
            account.setPassword(password);
        }

        @Override
        public Void call() {
            mAuth.createUserWithEmailAndPassword(account.getEmail(), account.getPassword())
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        // showProgress(false);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User registered Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    });
            new Transaction().setWalletBalance(0);
            UiUtils.startActivity(RegisterActivity.this, LoginActivity.class);
            return null;
        }
    }
}