package com.caregiver.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caregiver.R;
import com.caregiver.adapter.views.BookingItemView;
import com.caregiver.core.Constants;
import com.caregiver.core.ResponseParser;
import com.caregiver.core.WebApi;
import com.caregiver.core.models.Booking;
import com.caregiver.ui.profile.ProfileActivity;
import com.caregiver.ui.reviews.AddReviewDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookingListAdapter extends RecyclerView.Adapter<BookingItemView> {
    private List<Booking> items = new ArrayList<Booking>();

    @NonNull
    @Override
    public BookingItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_view, parent, false);

        return new BookingItemView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingItemView holder, int position) {
        holder.bind(items.get(position));
        holder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddReviewDialog(view.getContext(), items.get(holder.getLayoutPosition())).show();
            }
        });
        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking pi = items.get(holder.getLayoutPosition());
                int currentStatus = Integer.parseInt(pi.booking_status);
                if (Constants.loginUser.User_Type == Constants.USER_TYPE_GENERAL) {
                    if (currentStatus == Constants.BOOKING_STATUS_PENDING) {
                        pi.booking_status = "" + Constants.BOOKING_STATUS_CANCELED;
                    } else if (currentStatus == Constants.BOOKING_STATUS_ACCEPTED) {
                        pi.booking_status = "" + Constants.BOOKING_STATUS_COMPLETED;
                    }
                } else if (Constants.loginUser.User_Type == Constants.USER_TYPE_NANEY || Constants.loginUser.User_Type == Constants.USER_TYPE_NURSE) {
                    if (currentStatus == Constants.BOOKING_STATUS_PENDING) {
                        pi.booking_status = "" + Constants.BOOKING_STATUS_REJECTED;
                    } else if (currentStatus == Constants.BOOKING_STATUS_ACCEPTED) {
                        pi.booking_status = "" + Constants.BOOKING_STATUS_COMPLETED;
                    }
                }
                bookingAction(pi);
                refreshItem(pi, holder.getLayoutPosition());
            }
        });

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking pi = items.get(holder.getLayoutPosition());
                pi.booking_status = "" + Constants.BOOKING_STATUS_ACCEPTED;
                bookingAction(pi);
                refreshItem(pi, holder.getLayoutPosition());
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking bb = items.get(holder.getLayoutPosition());
                String userId = bb.from_user_id;
                if(String.valueOf(Constants.loginUser.id).equalsIgnoreCase(userId)){
                    userId = bb.to_user_id;
                }
                openProfile(view.getContext(), userId);
            }
        });

        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking bb = items.get(holder.getLayoutPosition());
                String userId = bb.from_user_id;
                if(String.valueOf(Constants.loginUser.id).equalsIgnoreCase(userId)){
                    userId = bb.to_user_id;
                }
                openProfile(view.getContext(), userId);
            }
        });
    }

    private void openProfile(Context context, String userId){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(Constants.key_user_id, userId);
        context.startActivity(intent);
    }

    private void refreshItem(Booking pi, int pos) {
        items.set(pos, pi);
        notifyItemChanged(pos);
    }

    public boolean addItems(List<Booking> list) {
        if (list != null && !list.isEmpty()) {
            items.addAll(list);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void bookingAction(Booking pi) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "booking_id=" + pi.booking_id
                + "&status=" + pi.booking_status
                + "&user_id=" + pi.to_user_id);
        Request request = new Request.Builder()
                .url(WebApi.UPDATE_BOOKING)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Constants.TAG, "Login::onFailure::Exception: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(Constants.TAG, "onResponse::response.body().string() = " + response.body().string());
            }
        });
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}