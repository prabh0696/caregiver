package com.caregiver.adapter.views;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.Review;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewItemView extends RecyclerView.ViewHolder {
    private CircleImageView image;
    private TextView userName, timeStamp, comment;
    private RequestOptions requestOptions;


    public ReviewItemView(View itemView) {
        super(itemView);


        timeStamp = itemView.findViewById(R.id.timeStamp);
        image = itemView.findViewById(R.id.image);

        comment = itemView.findViewById(R.id.comment);
        userName = itemView.findViewById(R.id.userName);


        requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.centerCrop();
        requestOptions.placeholder(R.drawable.ic_user_profile);
        requestOptions.error(R.drawable.ic_user_profile);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }

    public void bind(Review pi) {
        Glide.with(image.getContext())
                .load(WebApi.IMAGE_BASE_URL+pi.from_photo)
                .apply(requestOptions)
                .into(image);
        userName.setText((pi.from_first_name + " "+ pi.from_last_name).trim());
        timeStamp.setText(pi.review_date);
        comment.setText(pi.review);
    }

}
