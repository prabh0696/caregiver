package com.caregiver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.caregiver.MainActivity;
import com.caregiver.R;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));

            finish();
        }, 2 * 1000);


    }


}
