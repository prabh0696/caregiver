package com.caregiver.ui.reviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.Booking;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class AddReviewDialog extends Dialog {

    private Context mContext;
    private TextInputLayout input_comment;
    private TextInputEditText comment;
    private Booking booking;

    public AddReviewDialog(Context context, Booking booking) {
        super(context);
        mContext = context;
        this.booking = booking;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.add_review_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.round_rect_shape1);

        initializeComp();
    }

    private void initializeComp() {

        input_comment = findViewById(R.id.input_comment);
        comment = findViewById(R.id.et_comment);

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitClick();
            }
        });

    }

    private void submitClick() {
        input_comment.setError(null);
        String commentText = comment.getText().toString().trim();
        if (TextUtils.isEmpty(commentText)) {
            input_comment.setError(mContext.getString(R.string.empty_comment));
            return;
        }
        submitData(commentText);
    }


    private void submitData(String comment) {

        Log.d(Constants.TAG, "booking.to_user_id= " + booking.to_user_id);
        WebApi.showLoadingDialog(mContext);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "from_user_id="+Constants.loginUser.id
                +"&to_user_id="+booking.to_user_id
                +"&review="+comment
                +"&booking_id="+booking.booking_id);
        Request request = new Request.Builder()
                .url(WebApi.ADD_REVIEW)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.TAG, "Login::onFailure::Exception: " + e);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebApi.dismissLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                WebApi.dismissLoadingDialog();
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.FEEDBACK_SUBMITTED_ACTION));
                        dismiss();
                        WebApi.showLongToast(mContext, mContext.getString(R.string.feedback_submit));
                    }
                });
            }
        });

    }
}
