package com.caregiver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout input_username, input_password;
    private TextInputEditText et_email, et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        clickListener();
        setupViewPager();

    }

    private void clickListener(){
        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TextView signupBtn = findViewById(R.id.signupBtn);
        if(getIntent().getIntExtra(Constants.key_user_type, 0) != Constants.USER_TYPE_ADMIN){
            signupBtn.setVisibility(View.VISIBLE);
            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), SignupActivity.class));
                }
            });
        }




    }

    private void setupViewPager() {

        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext())
                .load(R.drawable.logosplash)
                .apply(requestOptions)
                .into((AppCompatImageView) findViewById(R.id.tv_cloud));
    }




}
