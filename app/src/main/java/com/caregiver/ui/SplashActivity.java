package com.caregiver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.MainActivity;
import com.caregiver.R;
import com.caregiver.ui.login.LoginOptionActivity;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setupViewPager();


        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, LoginOptionActivity.class));

            finish();
        }, 2 * 1000);


    }
    private void setupViewPager() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext())
                .load(R.drawable.welcome)
                .apply(requestOptions)
                .into((ImageView) findViewById(R.id.iv_image));

        requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext())
                .load(R.drawable.logosplash)
                .apply(requestOptions)
                .into((ImageView) findViewById(R.id.iv_society));
    }

}
