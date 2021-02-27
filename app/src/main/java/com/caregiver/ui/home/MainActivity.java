package com.caregiver.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.caregiver.R;
import com.caregiver.adapter.BookingListAdapter;
import com.caregiver.core.Constants;
import com.caregiver.core.Utils;
import com.caregiver.core.models.User;
import com.caregiver.ui.login.LoginOptionActivity;
import com.caregiver.ui.profile.ProfileActivity;
import com.caregiver.ui.search.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private User user;
    private BookingListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getIntent().getParcelableExtra(Constants.key_user);
        registerLocalBroadcastReciver();

        clickListener();
        setupViewPager();
    }

    private void clickListener() {

        AppCompatImageView searchBtn = findViewById(R.id.ib_search_toolbar);
        TextView btn_book_now = findViewById(R.id.btn_book_now);
        if (Constants.loginUser.User_Type == Constants.USER_TYPE_GENERAL) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), SearchActivity.class));
                }
            });
            btn_book_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), SearchActivity.class));
                }
            });
        } else {
            searchBtn.setVisibility(View.GONE);
            btn_book_now.setVisibility(View.GONE);
        }


        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.logout(view.getContext());
            }
        });

        findViewById(R.id.btn_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                intent.putExtra(Constants.key_user, user);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager() {

        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter = new BookingListAdapter();
        rvList.setLayoutManager(layoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setAdapter(mAdapter);
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
                } else if (action.equalsIgnoreCase(Constants.PROFILE_UPDATED_ACTION)) {
                    user = intent.getParcelableExtra(Constants.key_user);
                } else if (action.equalsIgnoreCase(Constants.BOOKING_ADDED_ACTION)) {
                    mAdapter.clear();
                }else if (action.equalsIgnoreCase(Constants.FEEDBACK_SUBMITTED_ACTION)) {
                    mAdapter.clear();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MOVE_TO_LOGIN_ACTION);
        intentFilter.addAction(Constants.PROFILE_UPDATED_ACTION);
        intentFilter.addAction(Constants.BOOKING_ADDED_ACTION);
        intentFilter.addAction(Constants.FEEDBACK_SUBMITTED_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    public void finish() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.finish();
    }
}