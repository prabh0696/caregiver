package com.caregiver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.MainActivity;
import com.caregiver.R;
import com.caregiver.core.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class LoginOptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        clickListener();
        setupViewPager();

    }

    private void clickListener(){
        findViewById(R.id.btn_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAction(Constants.USER_TYPE_ADMIN);
            }
        });

        findViewById(R.id.btn_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAction(Constants.USER_TYPE_GENERAL);
            }
        });



    }

    private void setupViewPager() {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext())
                .load(R.drawable.logosplash)
                .apply(requestOptions)
                .into((AppCompatImageView) findViewById(R.id.tv_cloud));
    }

    private void submitAction(int userType) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.key_user_type, userType);
        startActivity(intent);
    }
}
