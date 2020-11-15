package com.dangbinh.moneymanagement.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.utils.UiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    View buttonSubmit;
    EditText editTextForgotEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        buttonSubmit = findViewById(R.id.forgot_pass_submit);
        buttonSubmit.setOnClickListener(this);
        editTextForgotEmail = findViewById(R.id.forgot_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_pass_submit:
                FirebaseAuth.getInstance().sendPasswordResetEmail(editTextForgotEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // password reset email sent
                                    UiUtils dc = new UiUtils(ForgotPasswordActivity.this, R.string.check_mail);
                                    dc.sendResetPass();
                                } else {
                                    // TODO: handle error
                                }
                            }
                        });
        }
    }
}