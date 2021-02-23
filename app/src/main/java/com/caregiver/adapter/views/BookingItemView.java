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


}
