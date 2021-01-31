package com.caregiver.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.caregiver.MainActivity;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.models.Address;
import com.caregiver.core.models.User;
import com.caregiver.database.AppTableInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class UserDetailActivity extends AppCompatActivity {

    private TextInputLayout input_hourly_charges, input_experience, input_qualification, input_about,
            input_address, input_landmark, input_street, input_city, input_state, input_country, input_pincode;
    private TextInputEditText et_hourly_charges, et_experience, et_qualification, et_about,
            et_landmark, et_address, et_street, et_city, et_state, et_country, et_pincode;

    private Button btn_continue;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

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
    }

    private void setupViewPager() {

        user = getIntent().getParcelableExtra(Constants.key_user);

        btn_continue = findViewById(R.id.btn_continue);

        input_hourly_charges = findViewById(R.id.input_hourly_charges);
        input_experience = findViewById(R.id.input_experience);
        input_qualification = findViewById(R.id.input_qualification);
        input_address = findViewById(R.id.input_address);
        input_landmark = findViewById(R.id.input_landmark);
        input_street = findViewById(R.id.input_street);
        input_city = findViewById(R.id.input_city);
        input_state = findViewById(R.id.input_state);
        input_country = findViewById(R.id.input_country);
        input_pincode = findViewById(R.id.input_pincode);
        input_about = findViewById(R.id.input_about);

        et_about = findViewById(R.id.et_about);
        et_hourly_charges = findViewById(R.id.et_hourly_charges);
        et_experience = findViewById(R.id.et_experience);
        et_qualification = findViewById(R.id.et_qualification);
        et_landmark = findViewById(R.id.et_landmark);
        et_address = findViewById(R.id.et_address);
        et_street = findViewById(R.id.et_street);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_country = findViewById(R.id.et_country);
        et_pincode = findViewById(R.id.et_pincode);

        if(user.User_Type == Constants.USER_TYPE_GENERAL){
            input_hourly_charges.setVisibility(View.GONE);
            input_experience.setVisibility(View.GONE);
            input_qualification.setVisibility(View.GONE);
            input_about.setVisibility(View.GONE);
        }

    }

    private void clearError() {
        input_about.setError(null);

        input_hourly_charges.setError(null);
        input_experience.setError(null);
        input_qualification.setError(null);
        input_address.setError(null);
        input_pincode.setError(null);
        input_country.setError(null);
        input_state.setError(null);
        input_city.setError(null);
        input_street.setError(null);
        input_landmark.setError(null);
    }


    private void submitAction() {
        clearError();

        String charges = "";
        String experience = "";
        String qualification = "";
        String about = "";
        if(user.User_Type != Constants.USER_TYPE_GENERAL){
            about = et_about.getText().toString().trim();
            if (TextUtils.isEmpty(about)) {
                input_about.setError(getString(R.string.empty_about));
                return;
            }

            charges = et_hourly_charges.getText().toString().trim();
            if (TextUtils.isEmpty(charges)) {
                input_hourly_charges.setError(getString(R.string.empty_charges));
                return;
            }

            experience = et_experience.getText().toString().trim();
            if (TextUtils.isEmpty(experience)) {
                input_experience.setError(getString(R.string.empty_experience));
                return;
            }

            qualification = et_qualification.getText().toString().trim();
            if (TextUtils.isEmpty(qualification)) {
                input_qualification.setError(getString(R.string.empty_qualification));
                return;
            }
        }
        String addr = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(addr)) {
            input_address.setError(getString(R.string.empty_address));
            return;
        }

        String landmark = et_landmark.getText().toString().trim();
        if (TextUtils.isEmpty(landmark)) {
            input_landmark.setError(getString(R.string.empty_landmark));
            return;
        }

        String street = et_street.getText().toString().trim();
        if (TextUtils.isEmpty(street)) {
            input_street.setError(getString(R.string.empty_street));
            return;
        }

        String city = et_city.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            input_city.setError(getString(R.string.empty_city));
            return;
        }
        String state = et_state.getText().toString().trim();
        if (TextUtils.isEmpty(state)) {
            input_city.setError(getString(R.string.empty_state));
            return;
        }

        String country = et_country.getText().toString().trim();
        if (TextUtils.isEmpty(country)) {
            input_country.setError(getString(R.string.empty_country));
            return;
        }

        String pincode = et_pincode.getText().toString().trim();
        if (TextUtils.isEmpty(pincode)) {
            input_pincode.setError(getString(R.string.empty_pincode));
            return;
        }

        user.about = about;
        user.Experience = experience;
        user.Qualification = qualification;
        user.Charges = charges;

        Address address = user.addr;
        address.Address = addr;
        address.Pincode = pincode;
        address.Country = country;
        address.Street = street;
        address.landmark = landmark;
        address.City = city;
        address.Country = country;
        address.State = state;

        user.addr = address;

        AppTableInfo.updateUserDetail(Constants.getDataBaseObj(getApplicationContext()),
                user);
        if(getIntent().getBooleanExtra(Constants.key_is_from_signup, false)){
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Constants.MOVE_TO_HOME_ACTION));
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
