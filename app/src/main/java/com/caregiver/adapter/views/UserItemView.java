package com.caregiver.adapter.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caregiver.R;
import com.caregiver.core.Constants;
import com.caregiver.core.models.User;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemView extends RecyclerView.ViewHolder {
    public LinearLayoutCompat root;
    private CircleImageView user_image;
    private TextView user_name, charges, about, designation, reviews;
    public Button btn_action;
    private RequestOptions requestOptions;



    public UserItemView(View itemView) {
        super(itemView);

        root = itemView.findViewById(R.id.root);

        btn_action = itemView.findViewById(R.id.btn_action);
        charges = itemView.findViewById(R.id.charges);
        user_image = itemView.findViewById(R.id.user_image);

        about = itemView.findViewById(R.id.about);
        user_name = itemView.findViewById(R.id.user_name);
        designation = itemView.findViewById(R.id.designation);
        reviews = itemView.findViewById(R.id.reviews);

        requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.dontTransform();
        requestOptions.centerCrop();
        requestOptions.placeholder(R.drawable.ic_user_profile);
        requestOptions.error(R.drawable.ic_user_profile);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }

    public void bind(User pi) {

        Glide.with(user_image.getContext())
                .load(pi.Photo)
                .apply(requestOptions)
                .into(user_image);
        user_name.setText((pi.First_Name + " " + pi.Last_Name).trim());
        if (pi.User_Type != Constants.USER_TYPE_GENERAL) {
            charges.setText(pi.Charges);
            about.setText(pi.about);

            charges.setVisibility(View.VISIBLE);
            reviews.setVisibility(View.VISIBLE);
        } else {
            charges.setVisibility(View.GONE);
            about.setText("");
            reviews.setVisibility(View.GONE);
        }
        designation.setText(getDesignation(pi.User_Type));
    }

    private String getDesignation(int usertype) {
        if (usertype == Constants.USER_TYPE_GENERAL) {
            return this.itemView.getContext().getString(R.string.parents);
        } else if (usertype == Constants.USER_TYPE_NANEY) {
            return this.itemView.getContext().getString(R.string.nanny);
        } else if (usertype == Constants.USER_TYPE_NURSE) {
            return this.itemView.getContext().getString(R.string.nurse);
        }
        return "";
    }

}
