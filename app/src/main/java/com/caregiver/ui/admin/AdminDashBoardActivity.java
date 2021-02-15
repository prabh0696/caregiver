package com.caregiver.ui.admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.caregiver.R;
import com.caregiver.adapter.UserListAdapter;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.User;
import com.caregiver.ui.login.LoginOptionActivity;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
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

public class AdminDashBoardActivity extends AppCompatActivity {

    private UserListAdapter mAdapter;
    private AppCompatSpinner cat_action_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        registerLocalBroadcastReciver();
        clickListener();
        setupViewPager();

    }

    private void clickListener() {

    }

    private void setupViewPager() {

        initializeCategoryModule();
        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter = new UserListAdapter();
        rvList.setLayoutManager(layoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setAdapter(mAdapter);

    }

    private void initializeCategoryModule() {
        cat_action_bar = findViewById(R.id.cat_action_bar);
        String[] categoryArray = {getString(R.string.all_user),
                getString(R.string.parents),
                getString(R.string.nurse),
                getString(R.string.nanny)};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_drop_down, categoryArray);
        cat_action_bar.setAdapter(arrayAdapter);
        cat_action_bar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.clear();
                fetchData(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void fetchData(int userType) {
        WebApi.showLoadingDialog(this);
        String userTypeStr = "" + userType;
        if (userType == 0) {
            userTypeStr = Constants.USER_TYPE_GENERAL + "," + Constants.USER_TYPE_NANEY + "," + Constants.USER_TYPE_NURSE;
        }
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "skip_id=" + Constants.loginUser.id + "&user_type=" + userTypeStr);
        Request request = new Request.Builder()
                .url(WebApi.GET_USER_LIST)
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
                List<User> list = ResponseParser.parseUserList(response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItems(list);
                    }
                });
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
                } else if (action.equalsIgnoreCase(Constants.USER_DELETED)) {
                    mAdapter.clear();
                    fetchData(cat_action_bar.getSelectedItemPosition());
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MOVE_TO_LOGIN_ACTION);
        intentFilter.addAction(Constants.USER_DELETED);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    public void finish() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.finish();
    }
}
