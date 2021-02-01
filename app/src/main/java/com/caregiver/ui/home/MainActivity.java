package com.caregiver.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.Utils;
import com.caregiver.core.models.User;
import com.caregiver.ui.login.LoginOptionActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getIntent().getParcelableExtra(Constants.key_user);
        registerLocalBroadcastReciver();

        clickListener();
    }

    private void clickListener(){
        findViewById(R.id.ib_search_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(view.getContext());
            }
        });

        findViewById(R.id.btn_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private BroadcastReceiver broadcastReceiver;
    private void registerLocalBroadcastReciver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(Constants.MOVE_TO_LOGIN_ACTION)) {
                    startActivity(new Intent(getApplicationContext(), LoginOptionActivity.class));
                    finish();
                }else if (action.equalsIgnoreCase(Constants.PROFILE_UPDATED_ACTION)) {
                    user = intent.getParcelableExtra(Constants.key_user);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MOVE_TO_LOGIN_ACTION);
        intentFilter.addAction(Constants.PROFILE_UPDATED_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }
    public void finish(){
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.finish();
    }
}