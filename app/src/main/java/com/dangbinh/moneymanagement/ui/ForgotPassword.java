package com.dangbinh.moneymanagement.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.utils.Constants;
import com.dangbinh.moneymanagement.utils.DialogContainer;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    View submit_btn;
    EditText fgt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        submit_btn = findViewById(R.id.forgot_pass_submit);
        submit_btn.setOnClickListener(this);
        fgt_email = (EditText) findViewById(R.id.forgot_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_pass_submit:
                /*Firebase ref = new Firebase(Constants.FIREBASE_URL);

                ref.resetPassword(fgt_email.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        DialogContainer dc;
                        // password reset email sent
                        dc = new DialogContainer(com.dangbinh.moneymanagement.Ui.ForgotPassword.this, R.string.check_mail);
                        dc.show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                        firebaseError.getMessage();

                        DialogContainer dc;
                        switch (firebaseError.getCode()) {
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                // handle a non existing user
                                dc = new DialogContainer(com.dangbinh.moneymanagement.Ui.ForgotPassword.this, R.string.user_does_not_exist);
                                dc.show();
                                break;
                            default:
                                dc = new DialogContainer(com.dangbinh.moneymanagement.Ui.ForgotPassword.this, R.string.some_error);
                                dc.show();
                                // handle other errors
                                break;
                        }
                    }
                });*/
        }
    }
}