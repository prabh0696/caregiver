package com.caregiver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caregiver.R;
import com.caregiver.adapter.views.UserItemView;
import com.caregiver.core.Utils;
import com.caregiver.core.models.User;
import com.caregiver.ui.booking.BookingRequestDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserListAdapter extends RecyclerView.Adapter<UserItemView> {
    private List<User> items = new ArrayList<User>();

    @NonNull
    @Override
    public UserItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_view, parent, false);

        return new UserItemView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemView holder, int position) {
        holder.bind(items.get(position));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openProfile(v.getContext(), items.get(holder.getLayoutPosition()));
            }
        });

        holder.btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BookingRequestDialog(v.getContext(), "").show();
            }
        });
    }

    public boolean addItems(List<User> list) {
        if (list != null && !list.isEmpty()) {
            items.addAll(list);
            notifyDataSetChanged();
            return true;
        }
        return false;
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