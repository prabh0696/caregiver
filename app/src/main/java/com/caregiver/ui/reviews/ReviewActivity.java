package com.caregiver.ui.reviews;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.caregiver.R;
import com.caregiver.adapter.ReviewListAdapter;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.Booking;
import com.caregiver.core.models.Review;

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

public class ReviewActivity extends AppCompatActivity {

    private ReviewListAdapter mAdapter;
    private String userId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        clickListener();
        setupViewPager();
        userId = getIntent().getStringExtra(Constants.key_user_id);
        fetchData();
    }

    private void clickListener(){
        findViewById(R.id.ib_back_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setupViewPager() {

        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mAdapter = new ReviewListAdapter();
        rvList.setLayoutManager(layoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setAdapter(mAdapter);
        mAdapter.addItems(null);

    }

    private void fetchData() {
        findViewById(R.id.empty_list).setVisibility(View.GONE);
        WebApi.showLoadingDialog(this);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "to_user_id=" + userId);
        Request request = new Request.Builder()
                .url(WebApi.GET_ALL_REVIEW)
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
                List<Review> list = ResponseParser.parseReviewList(response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItems(list);
                        if(mAdapter.getItemCount() == 0){
                            findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.empty_list).setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

}
