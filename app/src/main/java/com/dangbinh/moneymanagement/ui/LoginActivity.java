package com.dangbinh.moneymanagement.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.models.Account;
import com.dangbinh.moneymanagement.utils.Constants;
import com.dangbinh.moneymanagement.utils.TaskRunner;
import com.dangbinh.moneymanagement.utils.UiUtils;
import com.dangbinh.moneymanagement.utils.UserDataGrabberUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView autoCompleteEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private ScrollView loginFormView;
    private Button buttonRegister;
    private TextView textViewForgotPass;
    private Button buttonEmailSignIn;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the login form.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i = getIntent();
        if (i.getBooleanExtra(Constants.EXIT, false)) {
            finish();
            return;
        }
        cbRememberMe = findViewById(R.id.checkbox_remember_me);

        // Email Field
        autoCompleteEmail = findViewById(R.id.edit_text_email);

        // Register Button
        buttonRegister = findViewById(R.id.button_register);

        // Password Field
        editTextPassword = findViewById(R.id.edit_text_password);

        editTextPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                // Call the sign in function alternative to clicking the button
                attemptLogin();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == EditorInfo.IME_ACTION_NEXT) {

            }
            return false;
        });

        buttonEmailSignIn = findViewById(R.id.button_signin);
        buttonEmailSignIn.setOnClickListener(this);

        loginFormView = findViewById(R.id.login_form);
        progressBar = findViewById(R.id.login_progress);
        buttonRegister.setOnClickListener(this);
        textViewForgotPass = findViewById(R.id.text_view_forgotpass);
        textViewForgotPass.setOnClickListener(this);

        populateAutoComplete();
        if (!i.getBooleanExtra(Account.LOGGED_OUT, false)) {
            autoLogin();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Register Button
            case R.id.button_register:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.button_signin:
                attemptLogin();
                break;
            case R.id.text_view_forgotpass:
                i = new Intent(this, ForgotPasswordActivity.class);
                startActivity(i);
                break;
        }
    }

    private boolean mayRequestContacts() {
        // Check for permission if the version is loew than Android M or check if the permission already accepted or ask now
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(autoCompleteEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS));
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return true;
    }

    private void populateAutoComplete() {
        /*if (!mayRequestContacts()) {
            return;
        }*/
        autoCompleteEmail.setAdapter(UserDataGrabberUtils.getUserDetails(com.dangbinh.moneymanagement.ui.LoginActivity.this));
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Use UserDataGrabberUtility to populate Emails in drop down.
                autoCompleteEmail.setAdapter(UserDataGrabberUtils.getUserDetails(LoginActivity.this));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        autoCompleteEmail.setError(null);
        editTextPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = autoCompleteEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            autoCompleteEmail.setError(getString(R.string.error_field_required));
            focusView = autoCompleteEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            autoCompleteEmail.setError(getString(R.string.error_invalid_email));
            focusView = autoCompleteEmail;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE).edit();
            if (cbRememberMe.isChecked()) {
                editor.putString(Account.EMAIL, email);
                editor.putString(Account.PASSWORD, password);
            } else {
                editor.clear();
            }
            editor.commit();
            TaskRunner.run(new UserLoginTask(email, password));
        }
    }

    private boolean autoLogin() {
        SharedPreferences preferences = getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        String email = preferences.getString(Account.EMAIL, "");
        String password = preferences.getString(Account.PASSWORD, "");
        return TaskRunner.run(new UserLoginTask(email, password));
    }

    private boolean isEmailValid(String email) {
        // Check if Email id is valid and return result
        return email.equals("null") || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        // Check if the password is > 4 chars.
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask implements Callable<Boolean> {
        Account account;

        UserLoginTask(String email, String password) {
            account = new Account();
            account.setEmail(email);
            account.setPassword(password);
        }

        @Override
        public Boolean call() {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            AtomicBoolean result = new AtomicBoolean(false);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (isEmailValid(account.getEmail()) && isPasswordValid(account.getPassword())) {
                TaskRunner.runOnUiThread(LoginActivity.this, () -> UiUtils.showProgress(loginFormView, progressBar, true, getResources().getInteger(android.R.integer.config_shortAnimTime)));
                mAuth.signInWithEmailAndPassword(account.getEmail(), account.getPassword())
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                result.set(true);
                                Intent view = new Intent(LoginActivity.this, TransactionsViewActivity.class);
                                view.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Toast.makeText(getApplicationContext(), "User Successfully logged with email " + task.getResult().getUser().getEmail(), Toast.LENGTH_LONG).show();
                                startActivity(view);
                            } else {
                                TaskRunner.runOnUiThread(LoginActivity.this, () -> UiUtils.showProgress(loginFormView, progressBar, false, getResources().getInteger(android.R.integer.config_shortAnimTime)));
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                result.set(false);
                            }
                        });
            } else {
                TaskRunner.runOnUiThread(LoginActivity.this, () -> UiUtils.showProgress(loginFormView, progressBar, false, getResources().getInteger(android.R.integer.config_shortAnimTime)));
                result.set(false);
            }
            return result.get();
        }
    }
}