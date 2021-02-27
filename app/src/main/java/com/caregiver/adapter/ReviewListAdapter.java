package com.caregiver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caregiver.R;
import com.caregiver.adapter.views.ReviewItemView;
import com.caregiver.core.models.Review;
import com.caregiver.core.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewItemView> {
    private List<Review> items = new ArrayList<Review>();

    @NonNull
    @Override
    public ReviewItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item_view, parent, false);

        return new ReviewItemView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewItemView holder, int position) {
        holder.bind(items.get(position));
    }

    public boolean addItems(List<Review> list) {
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