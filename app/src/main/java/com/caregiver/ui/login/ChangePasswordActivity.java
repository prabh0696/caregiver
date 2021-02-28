package com.caregiver.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.Utils;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputLayout input_old_pass, input_password,
            input_conform_password;

    private TextInputEditText et_old_pass, et_password, et_confirm_password;

    private Button btn_continue;

    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setupViewPager();
        clickListener();
    }

    private void clickListener() {
        btn_continue.setOnClickListener(new View.OnClickListener() {
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

        user = getIntent().getParcelableExtra(Constants.key_user);

        btn_continue = findViewById(R.id.btn_continue);

        input_conform_password = findViewById(R.id.input_conform_password);
        input_old_pass = findViewById(R.id.input_old_pass);
        input_password = findViewById(R.id.input_password);

        et_old_pass = findViewById(R.id.et_old_pass);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
    }

    private void clearError() {
        input_old_pass.setError(null);
        input_password.setError(null);
        input_conform_password.setError(null);

    }


    private void submitAction() {
        clearError();

        String oldPass = et_old_pass.getText().toString().trim();
        if (TextUtils.isEmpty(oldPass)) {
            input_old_pass.setError(getString(R.string.empty_password));
            return;
        } else if (!user.Password.equals(oldPass)) {
            input_old_pass.setError(getString(R.string.invalid_pass));
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            input_password.setError(getString(R.string.empty_password));
            return;
        }
        String cPassword = et_confirm_password.getText().toString().trim();
        if (!password.equals(cPassword)) {
            input_conform_password.setError(getString(R.string.invalid_cPassword));
            return;
        }

        user.Password = password;
        submitData(user);
    }

    private void submitData(User user) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String url = WebApi.UPDATE_PROFILE;

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("id", ""+user.id);
        builder.addFormDataPart("first_name", user.First_Name);
        builder.addFormDataPart("last_name", user.Last_Name);
        builder.addFormDataPart("password", user.Password);
        builder.addFormDataPart("user_type", "" + user.User_Type);
        builder.addFormDataPart("email", user.Email);
        builder.addFormDataPart("phone", user.Phone);
        builder.addFormDataPart("charges", user.Charges);
        builder.addFormDataPart("experience", user.Experience);
        builder.addFormDataPart("qualification", user.Qualification);
        builder.addFormDataPart("about", user.about);
        builder.addFormDataPart("address", user.addr.Address);
        builder.addFormDataPart("landmark", user.addr.landmark);
        builder.addFormDataPart("street", user.addr.Street);
        builder.addFormDataPart("city", user.addr.City);
        builder.addFormDataPart("state", user.addr.State);
        builder.addFormDataPart("country", user.addr.Country);
        builder.addFormDataPart("pincode", user.addr.Pincode);
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.TAG, "ChangePasswordActivity::onFailure::Exception: " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebApi.dismissLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(Constants.TAG, "ChangePasswordActivity::onResponse::response.body().string(): " + response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Constants.PROFILE_UPDATED_ACTION);
                        intent.putExtra(Constants.key_user, user);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        finish();
                    }
                });
            }
        });
    }
}


