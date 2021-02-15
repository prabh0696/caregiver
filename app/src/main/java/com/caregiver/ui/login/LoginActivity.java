package com.caregiver.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.Utils;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.User;
import com.caregiver.ui.admin.AdminDashBoardActivity;
import com.caregiver.ui.home.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout input_username, input_password;
    private TextInputEditText et_email, et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerLocalBroadcastReciver();
        ;
        clickListener();
        setupViewPager();

    }

    private void clickListener() {
        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAction();
            }
        });
        TextView signupBtn = findViewById(R.id.signupBtn);
        if (getIntent().getIntExtra(Constants.key_user_type, 0) != Constants.USER_TYPE_ADMIN) {
            signupBtn.setVisibility(View.VISIBLE);
            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), SignupActivity.class));
                }
            });
        }

        findViewById(R.id.forgotPwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ForgotPassowrdActivity.class));
            }
        });


    }

    private void setupViewPager() {

        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext())
                .load(R.drawable.logosplash)
                .apply(requestOptions)
                .into((AppCompatImageView) findViewById(R.id.tv_cloud));
    }

    private void clearError() {
        input_username.setError(null);
        input_password.setError(null);
    }

    private void submitAction() {
        clearError();
        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            input_username.setError(getString(R.string.empty_email));
            return;
        } else if (!Utils.validateEmailAddr(email)) {
            input_username.setError(getString(R.string.invalid_email));
            return;
        }
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            input_password.setError(getString(R.string.empty_password));
            return;
        }

        callAPiConnection(email, password);
    }

    private void loginDone(User user) {
        if (user == null) {
            input_password.setError(getString(R.string.user_not_exist));
            return;
        } else {
            Constants.loginUser = user;
            if (user.User_Type == Constants.USER_TYPE_ADMIN) {
                startActivity(new Intent(this, AdminDashBoardActivity.class));
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.key_user, user);
                startActivity(intent);
            }
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Constants.MOVE_TO_HOME_ACTION));
        }
    }

    private void callAPiConnection(String email,
                                   String password) {
        WebApi.showLoadingDialog(this);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "email="+email+"&password="+password);
        Request request = new Request.Builder()
                .url(WebApi.LOGIN)
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
                User user = ResponseParser.parseLoginResponse(response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginDone(user);
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
                if (action.equalsIgnoreCase(Constants.MOVE_TO_HOME_ACTION)) {
                    finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MOVE_TO_HOME_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

    public void finish() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        super.finish();
    }
}
