package com.caregiver.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.Utils;
import com.caregiver.core.models.User;
import com.caregiver.database.AppTableInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;


public class ForgotPassowrdActivity extends AppCompatActivity {

    private TextInputLayout input_email;

    private TextInputEditText et_email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passowrd);

        setupViewPager();
        clickListener();
    }

    private void clickListener() {
        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAction();
            }
        });

        findViewById(R.id.ib_back_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void setupViewPager() {

        input_email = findViewById(R.id.input_email);

        et_email = findViewById(R.id.et_email);

    }


    private void clearError() {
        input_email.setError(null);
    }


    private void submitAction() {
        clearError();

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            input_email.setError(getString(R.string.empty_email));
            return;
        } else if (!Utils.validateEmailAddr(email)) {
            input_email.setError(getString(R.string.invalid_email));
            return;
        }

        User user = AppTableInfo.checkUserExist(Constants.getDataBaseObj(getApplicationContext()),
                email, "");
        if (user == null) {
            input_email.setError(getString(R.string.email_not_exist));
            return;
        } else {
            Toast.makeText(this, getString(R.string.pass_sent)+" "+email, Toast.LENGTH_LONG).show();
            finish();
        }
    }

}


