package com.caregiver.ui.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.caregiver.R;
import com.caregiver.adapter.UserListAdapter;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.User;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class SearchActivity extends AppCompatActivity {

    private UserListAdapter mAdapter;
    private EditText searchField;
    private RadioButton radio_all, radio_nanny, radio_nurse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViewPager();
        clickListener();
    }

    private void clickListener() {
        findViewById(R.id.ib_back_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
            }
        });
    }

    private void setupViewPager() {

        searchField = findViewById(R.id.searchField);

        radio_all = findViewById(R.id.radio_all);
        radio_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radio_nanny.setChecked(false);
                    radio_nurse.setChecked(false);
                    fetchData();
                }
            }
        });
        radio_nanny = findViewById(R.id.radio_nanny);
        radio_nanny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radio_all.setChecked(false);
                    radio_nurse.setChecked(false);
                    fetchData();
                }
            }
        });
        radio_nurse = findViewById(R.id.radio_nurse);
        radio_nurse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radio_nanny.setChecked(false);
                    radio_all.setChecked(false);
                    fetchData();
                }
            }
        });

        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter = new UserListAdapter();
        rvList.setLayoutManager(layoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setAdapter(mAdapter);

    }

    private void fetchData() {
        String keyword = searchField.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        mAdapter.clear();
        WebApi.showLoadingDialog(this);
        String userType = "";
        if (radio_all.isChecked()) {
            userType = Constants.USER_TYPE_NANEY + "," + Constants.USER_TYPE_NURSE;
        } else if (radio_nurse.isChecked()) {
            userType = "" + Constants.USER_TYPE_NURSE;
        } else if (radio_nanny.isChecked()) {
            userType = "" + Constants.USER_TYPE_NANEY;
        }
        Log.d(Constants.TAG, "userType = " + userType);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "skip_id="+Constants.loginUser.id
                +"&user_type="+userType
                +"&keyword="+keyword);
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

}
