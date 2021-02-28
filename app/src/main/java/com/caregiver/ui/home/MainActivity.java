package com.caregiver.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.caregiver.R;
import com.caregiver.adapter.BookingListAdapter;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.Utils;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.Booking;
import com.caregiver.ui.login.LoginOptionActivity;
import com.caregiver.ui.profile.ProfileActivity;
import com.caregiver.ui.search.SearchActivity;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookingListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerLocalBroadcastReciver();

        clickListener();
        setupViewPager();
        fetchData();
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
                intent.putExtra(Constants.key_user, Constants.loginUser);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
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

    private void fetchData() {
        findViewById(R.id.empty_list).setVisibility(View.GONE);
        WebApi.showLoadingDialog(this);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "user_id=" + Constants.loginUser.id);
        Request request = new Request.Builder()
                .url(WebApi.GET_BOOKING_LIST)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.TAG, "Login::onFailure::Exception: " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebApi.dismissLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Booking> list = ResponseParser.parseBookingList(response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItems(list);
                        if (mAdapter.getItemCount() == 0) {
                            findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.empty_list).setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void refresh() {
        mAdapter.clear();
        fetchData();
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
                    Constants.loginUser = intent.getParcelableExtra(Constants.key_user);
                } else if (action.equalsIgnoreCase(Constants.BOOKING_ADDED_ACTION)) {
                    refresh();
                } else if (action.equalsIgnoreCase(Constants.FEEDBACK_SUBMITTED_ACTION)) {
                    refresh();
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