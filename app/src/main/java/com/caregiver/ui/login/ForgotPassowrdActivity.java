package com.caregiver.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.Utils;
import com.caregiver.core.WebApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ForgotPassowrdActivity extends AppCompatActivity {

    private TextInputLayout input_email;

    private TextInputEditText et_email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passowrd);

        setupViewPager();
        clickListener();
    }

    private void clickListener() {
        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAction();
            }
        });

        findViewById(R.id.ib_back_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void setupViewPager() {

        input_email = findViewById(R.id.input_email);

        et_email = findViewById(R.id.et_email);

    }


    private void clearError() {
        input_email.setError(null);
    }


    private void submitAction() {
        clearError();

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            input_email.setError(getString(R.string.empty_email));
            return;
        } else if (!Utils.validateEmailAddr(email)) {
            input_email.setError(getString(R.string.invalid_email));
            return;
        }

        validateUser(email);
    }


    private void validateUser(String email){
        WebApi.showLoadingDialog(this);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "email="+email);
        Request request = new Request.Builder()
                .url(WebApi.FORGOT_PASS)
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
                boolean result = ResponseParser.checkUserExist(response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result) {
                            input_email.setError(getString(R.string.email_not_exist));
                            return;
                        } else {
                            WebApi.showLongToast(ForgotPassowrdActivity.this, getString(R.string.pass_sent)+" "+email);
                            finish();
                        }
                    }
                });
            }
        });
    }
}


