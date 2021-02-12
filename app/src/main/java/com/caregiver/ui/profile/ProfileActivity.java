package com.caregiver.ui.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.models.User;
import com.caregiver.ui.login.SignupActivity;
import com.caregiver.ui.login.UserDetailActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView user_name, phone, email, about, qualification, experience, charges, address;

    private User user;
    private Button editInfoButton, editButton;

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

        findViewById(R.id.ib_back_toolbar).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupViewPager() {
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
        setUserDate(getIntent().getParcelableExtra(Constants.key_user));
    }

    private void setUserDate(User pi) {
        user = pi;
        if (user != null) {
            if(Constants.loginUserId != user.id){
                editInfoButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
            }
            user_name.setText(user.First_Name+" "+user.Last_Name);
            phone.setText(user.Phone);
            email.setText(user.Email);
            experience.setText(user.Experience);
            charges.setText(user.Charges + " CAD");
            about.setText(user.about);
            qualification.setText(user.Qualification);
            address.setText(user.addr.getAddressAsString());
            setUserImage(user.Photo);
        }
    }

    private void setUserImage(String path) {
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

    private void registerLocalBroadcastReciver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(Constants.PROFILE_UPDATED_ACTION)) {
                    setUserDate(intent.getParcelableExtra(Constants.key_user));
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PROFILE_UPDATED_ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);

    }

}
