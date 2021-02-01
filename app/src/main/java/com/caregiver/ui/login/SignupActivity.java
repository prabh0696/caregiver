package com.caregiver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.caregiver.ui.home.MainActivity;
import com.caregiver.R;
import com.caregiver.core.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout input_fname, input_lname, input_phone, input_email, input_password,
            input_conform_password;

    private TextInputEditText et_fname, et_lname, et_phone, et_email, et_password, et_confirm_password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        clickListener();
        setupViewPager();

    }

    private void clickListener() {
        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAction();
            }
        });
    }

    private void setupViewPager() {

        input_fname = findViewById(R.id.input_fname);
        input_lname = findViewById(R.id.input_lname);
        input_phone = findViewById(R.id.input_phone);
        input_conform_password = findViewById(R.id.input_conform_password);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_phone = findViewById(R.id.et_phone);

    }

    private void clearError() {
        input_email.setError(null);
        input_password.setError(null);
        input_fname.setError(null);
        input_lname.setError(null);
        input_phone.setError(null);
        input_password.setError(null);
        input_conform_password.setError(null);

    }


    private void submitAction() {
        clearError();
        String fName = et_fname.getText().toString().trim();
        if (TextUtils.isEmpty(fName)) {
            input_fname.setError(getString(R.string.empty_fname));
            return;
        }

        String lName = et_lname.getText().toString().trim();
        if (TextUtils.isEmpty(lName)) {
            input_lname.setError(getString(R.string.empty_lname));
            return;
        }

        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            input_phone.setError(getString(R.string.empty_phone));
            return;
        }

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            input_email.setError(getString(R.string.empty_email));
            return;
        } else if (!Utils.validateEmailAddr(email)) {
            input_email.setError(getString(R.string.invalid_email));
            return;
        }
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            input_password.setError(getString(R.string.empty_password));
            return;
        }
        String cPassword = et_confirm_password.getText().toString().trim();
        if (!password.equals(cPassword)) {
            input_conform_password.setError(getString(R.string.invalid_cPassword));
            return;
        }
        startActivity(new Intent(this, MainActivity.class));
    }
}
