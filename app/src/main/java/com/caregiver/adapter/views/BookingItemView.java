package com.caregiver.adapter.views;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.models.Booking;
import com.caregiver.core.models.User;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookingItemView extends RecyclerView.ViewHolder {
    public CircleImageView image;
    public TextView user_name, phone, email, message, messageLabel, booking_request, date, timeing, status;
    public Button btnAction, btn_accept, btnReview;
    private RequestOptions requestOptions;


    public BookingItemView(View itemView) {
        super(itemView);

        btnReview = itemView.findViewById(R.id.btn_review);
        btnAction = itemView.findViewById(R.id.btn_action);
        btn_accept = itemView.findViewById(R.id.btn_accept);
        phone = itemView.findViewById(R.id.phone);
        email = itemView.findViewById(R.id.email);
        image = itemView.findViewById(R.id.user_image);

        booking_request = itemView.findViewById(R.id.booking_request);
        date = itemView.findViewById(R.id.date);
        timeing = itemView.findViewById(R.id.timeing);
        status = itemView.findViewById(R.id.status);
        message = itemView.findViewById(R.id.message);
        messageLabel = itemView.findViewById(R.id.messageLabel);
        user_name = itemView.findViewById(R.id.user_name);


        requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.centerCrop();
        requestOptions.placeholder(R.drawable.ic_user_profile);
        requestOptions.error(R.drawable.ic_user_profile);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }

    public void bind(Booking pi) {
        String loginUserId = String.valueOf(Constants.loginUser.id);
        int statusInt = Integer.parseInt(pi.booking_status);
        if (loginUserId.equalsIgnoreCase(pi.from_user_id)) {
            user_name.setText(pi.to_first_name + " " + pi.to_last_name);
            phone.setText(itemView.getContext().getString(R.string.phone) + " :" + pi.to_phone);
            email.setText(itemView.getContext().getString(R.string.email1) + " :" + pi.to_email);

            if (statusInt == Constants.BOOKING_STATUS_PENDING) {
                btnAction.setText(itemView.getContext().getString(R.string.cancel));
                btnAction.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.GONE);
            } else if (statusInt == Constants.BOOKING_STATUS_ACCEPTED) {
                btnAction.setText(itemView.getContext().getString(R.string.end_contract));
                btnAction.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.GONE);
            } else {
                btnAction.setVisibility(View.GONE);
                btn_accept.setVisibility(View.GONE);
            }

            if (hasReview(pi.review_id)) {
                btnReview.setVisibility(View.GONE);
            } else {
                btnReview.setVisibility(View.VISIBLE);
            }

        } else if (loginUserId.equalsIgnoreCase(pi.to_user_id)) {
            user_name.setText(pi.from_first_name + " " + pi.from_last_name);
            phone.setText(itemView.getContext().getString(R.string.phone) + " :" + pi.from_phone);
            email.setText(itemView.getContext().getString(R.string.email1) + " :" + pi.from_email);
            if (statusInt == Constants.BOOKING_STATUS_PENDING) {
                btnAction.setText(itemView.getContext().getString(R.string.reject));
                btn_accept.setVisibility(View.VISIBLE);
                btnAction.setVisibility(View.VISIBLE);
            } else if (statusInt == Constants.BOOKING_STATUS_ACCEPTED) {
                btnAction.setText(itemView.getContext().getString(R.string.end_contract));
                btnAction.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.GONE);
            } else {
                btnAction.setVisibility(View.GONE);
                btn_accept.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(pi.booking_message)) {
            message.setVisibility(View.VISIBLE);
            messageLabel.setVisibility(View.VISIBLE);
            message.setText(pi.booking_message);
        } else {
            messageLabel.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
        }
        setBookingStatus(statusInt);
        booking_request.setText(itemView.getContext().getString(R.string.booking_request_on) + " " + pi.booked_date);
        date.setText(pi.from_date + " - " + pi.to_date);
        timeing.setText(pi.from_time + " - " + pi.to_time);

    }

    private void setBookingStatus(int statusInt) {
        if (statusInt == Constants.BOOKING_STATUS_PENDING) {
            status.setText(itemView.getContext().getString(R.string.pending));
        } else if (statusInt == Constants.BOOKING_STATUS_ACCEPTED) {
            status.setText(itemView.getContext().getString(R.string.on_going));
        } else if (statusInt == Constants.BOOKING_STATUS_COMPLETED) {
            status.setText(itemView.getContext().getString(R.string.completed));
        } else if (statusInt == Constants.BOOKING_STATUS_CANCELED) {
            status.setText(itemView.getContext().getString(R.string.canceled));
        } else if (statusInt == Constants.BOOKING_STATUS_REJECTED) {
            status.setText(itemView.getContext().getString(R.string.rejected));
        }
    }

    private boolean hasReview(String id) {
        if (id != null && !id.equalsIgnoreCase("null")) {
            int rId = Integer.parseInt(id);
            if (rId > 0) {
                return true;
            }
        }
        return false;
    }
}
