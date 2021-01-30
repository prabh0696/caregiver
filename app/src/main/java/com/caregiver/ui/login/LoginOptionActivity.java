package com.caregiver.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LoginOptionActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        registerLocalBroadcastReciver();
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
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.key_user_type, userType);
        startActivity(intent);
    }

    private void registerLocalBroadcastReciver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(Constants.MOVE_TO_HOME_ACTION)) {
                    finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MOVE_TO_HOME_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    public void finish(){
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.finish();
    }

}
