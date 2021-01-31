package com.caregiver.ui.admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.ui.login.LoginOptionActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AdminDashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        registerLocalBroadcastReciver();;
        setupViewPager();

    }

    private void setupViewPager() {

        initializeCategoryModule();

    }

    private void initializeCategoryModule() {
        AppCompatSpinner cat_action_bar = findViewById(R.id.cat_action_bar);
        String[] categoryArray = {getString(R.string.all_user),
                getString(R.string.parents),
                getString(R.string.nanny),
                getString(R.string.nurse)};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_drop_down, categoryArray);
        cat_action_bar.setAdapter(arrayAdapter);
        cat_action_bar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MOVE_TO_LOGIN_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }
    public void finish(){
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.finish();
    }
}
