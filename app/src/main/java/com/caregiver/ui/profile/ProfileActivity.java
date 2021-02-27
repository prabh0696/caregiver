package com.caregiver.ui.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.User;
import com.caregiver.ui.login.SignupActivity;
import com.caregiver.ui.login.UserDetailActivity;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView user_name, phone, email, about, qualification, experience, charges, address;

    private User user;
    private Button editInfoButton, editButton, changePassButton, btnDelete, btnReview;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        registerLocalBroadcastReciver();

        setupViewPager();
        clickListener();
    }

    private void clickListener() {

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra(Constants.key_user, user);
                intent.putExtra(Constants.key_is_from_edit, true);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignupActivity.class);
                intent.putExtra(Constants.key_user, user);
                intent.putExtra(Constants.key_is_from_edit, true);
                startActivity(intent);
            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });


        findViewById(R.id.ib_back_toolbar).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                confirmationDialog(view.getContext());
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setupViewPager() {
        btnReview = findViewById(R.id.btn_review);
        btnDelete = findViewById(R.id.btn_delete);
        changePassButton = findViewById(R.id.changePassButton);
        editInfoButton = findViewById(R.id.editInfoButton);
        editButton = findViewById(R.id.editButton);

        userImage = findViewById(R.id.userImage);
        user_name = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        about = findViewById(R.id.about);
        qualification = findViewById(R.id.qualification);
        experience = findViewById(R.id.experience);
        charges = findViewById(R.id.charges);
        address = findViewById(R.id.address);

        User pi = getIntent().getParcelableExtra(Constants.key_user);
        if (pi != null) {
            setUserData(pi);
        } else {
            fetchUserData(getIntent().getStringExtra(Constants.key_user_id));
        }

    }

    private void setUserData(User pi) {
        user = pi;
        if (user != null) {
            if (Constants.loginUser.id != user.id) {
                editInfoButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                changePassButton.setVisibility(View.GONE);
                btnReview.setVisibility(View.VISIBLE);
            }
            user_name.setText(user.First_Name + " " + user.Last_Name);
            phone.setText(user.Phone);
            email.setText(user.Email);
            experience.setText(user.Experience + " Yrs");
            charges.setText(user.Charges + " CAD");
            about.setText(user.about);
            qualification.setText(user.Qualification);
            address.setText(user.addr.getAddressAsString());
            setUserImage(user.Photo);

            if (Constants.loginUser.User_Type == Constants.USER_TYPE_ADMIN) {
                findViewById(R.id.line).setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }

            if (user.User_Type == Constants.USER_TYPE_GENERAL) {
                findViewById(R.id.chargesLabel).setVisibility(View.GONE);
                findViewById(R.id.aboutLabel).setVisibility(View.GONE);
                findViewById(R.id.experienceLabel).setVisibility(View.GONE);
                findViewById(R.id.qualificationLabel).setVisibility(View.GONE);

                experience.setVisibility(View.GONE);
                charges.setVisibility(View.GONE);
                about.setVisibility(View.GONE);
                qualification.setVisibility(View.GONE);
                btnReview.setVisibility(View.GONE);
            }
        }
    }

    private void setUserImage(String path) {
        if(!new File(path).exists()){
            path = WebApi.IMAGE_BASE_URL+path;
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.placeholder(R.drawable.ic_user_profile);
        requestOptions.dontTransform();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplicationContext())
                .load(path)
                .apply(requestOptions)
                .into(userImage);

    }

    private void confirmationDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle(R.string.warning_title)
                .setMessage(R.string.warning_msg)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    deleteUser();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> {
                    dialog.dismiss();

                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void deleteUser() {
        WebApi.showLoadingDialog(this);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "id=" + user.id);
        Request request = new Request.Builder()
                .url(WebApi.DELETE_USER)
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
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebApi.showLongToast(ProfileActivity.this, getString(R.string.user_deleted));
                        finish();
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Constants.USER_DELETED));
                    }
                });
            }
        });
    }

    private void fetchUserData(String userId) {
        WebApi.showLoadingDialog(this);

        Log.d(Constants.TAG, "userId=" + userId);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "id=" + userId);
        Request request = new Request.Builder()
                .url(WebApi.USER_DETAIL)
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
                User pi = ResponseParser.parseSignupResponse(response.body().string());
                WebApi.dismissLoadingDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUserData(pi);
                    }
                });
            }
        });
    }

    private void registerLocalBroadcastReciver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(Constants.PROFILE_UPDATED_ACTION)) {
                    setUserData(intent.getParcelableExtra(Constants.key_user));
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PROFILE_UPDATED_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

}
