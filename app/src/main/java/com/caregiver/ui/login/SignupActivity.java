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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener {

    private TextInputLayout input_fname, input_lname, input_phone, input_email, input_password,
            input_conform_password;

    private TextInputEditText et_fname, et_lname, et_phone, et_email, et_password, et_confirm_password;
    private AppCompatSpinner userTypeDropDown;

    private CircleImageView userImage;
    private Button btn_continue;

    private String imagePath = "";

    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        registerLocalBroadcastReciver();

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

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });
    }

    private void openImagePicker() {

        BSImagePicker multiSelectionPicker = new BSImagePicker.Builder(getPackageName() + ".provider")
                .setMaximumMultiSelectCount(1)
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .setSpanCount(4)
                .setGridSpacing(Utils.dp2px(2))
                .setPeekHeight(Utils.dp2px(360))
                .setTag("A request ID")
                .build();

        multiSelectionPicker.show(getSupportFragmentManager(), "picker");

    }

    private void setupViewPager() {
        btn_continue = findViewById(R.id.btn_continue);
        userImage = findViewById(R.id.userImage);

        input_fname = findViewById(R.id.input_fname);
        input_lname = findViewById(R.id.input_lname);
        input_phone = findViewById(R.id.input_phone);
        input_conform_password = findViewById(R.id.input_conform_password);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_phone = findViewById(R.id.et_phone);

        setUserTypeDropDown();
        checkForEdit();
    }

    private void setUserTypeDropDown() {
        userTypeDropDown = findViewById(R.id.userTypeDropDown);
        String[] categoryArray = {getString(R.string.select_user_type),
                getString(R.string.parents),
                getString(R.string.nanny),
                getString(R.string.nurse)};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_drop_down, categoryArray);
        userTypeDropDown.setAdapter(arrayAdapter);
    }

    private void checkForEdit() {
        if (getIntent().getBooleanExtra(Constants.key_is_from_edit, false)) {

            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit_profile);

            btn_continue.setText(R.string.update);
            findViewById(R.id.iv_sub_text).setVisibility(View.GONE);
            findViewById(R.id.iv_header).setVisibility(View.GONE);
            input_conform_password.setVisibility(View.GONE);
            input_password.setVisibility(View.GONE);
            et_email.setInputType(InputType.TYPE_NULL);
            user = getIntent().getParcelableExtra(Constants.key_user);
            if (user != null) {
                et_fname.setText(user.First_Name);
                et_lname.setText(user.Last_Name);
                et_phone.setText(user.Phone);
                et_email.setText(user.Email);
                userTypeDropDown.setSelection(user.User_Type);
                imagePath = user.Photo;
                setUserImage(user.Photo);
            }
        }
    }


    private void clearError() {
        input_email.setError(null);
        input_password.setError(null);

        input_fname.setError(null);
        input_lname.setError(null);
        input_phone.setError(null);
        input_password.setError(null);
        input_conform_password.setError(null);

    }


    private void submitAction() {
        clearError();
        boolean isEdit = getIntent().getBooleanExtra(Constants.key_is_from_edit, false);
        int index = userTypeDropDown.getSelectedItemPosition();
        if (index == 0) {
            Toast.makeText(this, R.string.empty_user_type, Toast.LENGTH_LONG).show();
            return;
        }

        String fName = et_fname.getText().toString().trim();
        if (TextUtils.isEmpty(fName)) {
            input_fname.setError(getString(R.string.empty_fname));
            return;
        }

        String lName = et_lname.getText().toString().trim();
        if (TextUtils.isEmpty(lName)) {
            input_lname.setError(getString(R.string.empty_lname));
            return;
        }

        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            input_phone.setError(getString(R.string.empty_phone));
            return;
        }

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            input_email.setError(getString(R.string.empty_email));
            return;
        } else if (!Utils.validateEmailAddr(email)) {
            input_email.setError(getString(R.string.invalid_email));
            return;
        }

        String password = "";
        if (!isEdit) {
            password = et_password.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                input_password.setError(getString(R.string.empty_password));
                return;
            }
            String cPassword = et_confirm_password.getText().toString().trim();
            if (!password.equals(cPassword)) {
                input_conform_password.setError(getString(R.string.invalid_cPassword));
                return;
            }
        }


        if (user == null) {
            user = new User();
        }
        user.Email = email;
        user.Phone = phone;
        if (!isEdit) {
            user.Password = password;
        }
        user.First_Name = fName;
        user.Last_Name = lName;
        user.Photo = imagePath;
        user.User_Type = index;

        submitData(user);

    }

    private void submitData(User user) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String url = "";
        RequestBody body = null;
        if (user.id == 0) {
            url = WebApi.SIGNUP;
            body = RequestBody.create(mediaType, "first_name=" + user.First_Name
                    + "&last_name=" + user.Last_Name
                    + "&password=" + user.Password
                    + "&user_type=" + user.User_Type
                    + "&email=" + user.Email
                    + "&phone=" + user.Phone
                    + "&photo=" + user.Photo
                    + "&charges=" + user.Charges
                    + "&experience=" + user.Experience
                    + "&qualification=" + user.Qualification
                    + "&about=" + user.about
                    + "&address=" + user.addr.Address
                    + "&landmark=" + user.addr.landmark
                    + "&street=" + user.addr.Street
                    + "&city=" + user.addr.Street
                    + "&state=" + user.addr.State
                    + "&country=" + user.addr.Country
                    + "&pincode=" + user.addr.Pincode);
        } else {
            url = WebApi.UPDATE_PROFILE;
            body = RequestBody.create(mediaType, "id=" + user.id
                    +"&first_name=" + user.First_Name
                    + "&last_name=" + user.Last_Name
                    + "&password=" + user.Password
                    + "&user_type=" + user.User_Type
                    + "&email=" + user.Email
                    + "&phone=" + user.Phone
                    + "&photo=" + user.Photo
                    + "&charges=" + user.Charges
                    + "&experience=" + user.Experience
                    + "&qualification=" + user.Qualification
                    + "&about=" + user.about
                    + "&address=" + user.addr.Address
                    + "&landmark=" + user.addr.landmark
                    + "&street=" + user.addr.Street
                    + "&city=" + user.addr.Street
                    + "&state=" + user.addr.State
                    + "&country=" + user.addr.Country
                    + "&pincode=" + user.addr.Pincode);
        }


        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.TAG, "Signup::onFailure::Exception: " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebApi.dismissLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(user.id > 0){
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
                }else{
                    User user1 = ResponseParser.parseSignupResponse(response.body().string());
                    WebApi.dismissLoadingDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(user1 == null){
                                input_email.setError(getString(R.string.email_exist));
                            }else {
                                Constants.loginUser = user1;
                                Intent intent = new Intent(SignupActivity.this, UserDetailActivity.class);
                                intent.putExtra(Constants.key_is_from_signup, true);
                                intent.putExtra(Constants.key_user, user1);
                                startActivity(intent);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Constants.MOVE_TO_HOME_ACTION));
                            }
                        }
                    });
                }

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

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        if (uri != null) {
            imagePath = uri.toString();
            if (imagePath.toLowerCase().startsWith("content://")) {
                imagePath = Utils.getRealPathFromUri(this, uri);
            }
            setUserImage(imagePath);
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

}


