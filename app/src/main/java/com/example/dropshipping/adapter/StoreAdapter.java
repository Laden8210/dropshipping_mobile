package com.example.dropshipping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.model.StoreItem;
import com.example.dropshipping.view.StoreActivity;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private final Context context;
    private List<StoreItem> storeItems;

    public StoreAdapter(Context context, List<StoreItem> storeItems) {
        this.context = context;
        this.storeItems = storeItems;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        StoreItem store = storeItems.get(position);

        // Store name
        holder.tvStoreName.setText(store.getStoreName());

        // Store status (e.g. active/inactive)
        holder.tvStoreStatus.setText(store.getStatus());

        // Store description (optional)
        if (store.getStoreDescription() != null && !store.getStoreDescription().isEmpty()) {
            holder.tvStoreDescription.setVisibility(View.VISIBLE);
            holder.tvStoreDescription.setText(store.getStoreDescription());
        } else {
            holder.tvStoreDescription.setVisibility(View.GONE);
        }

        // Store phone (optional)
        if (store.getStorePhone() != null && !store.getStorePhone().isEmpty()) {
            holder.layoutContactInfo.setVisibility(View.VISIBLE);
            holder.tvStorePhone.setText(store.getStorePhone());
        } else {
            holder.layoutContactInfo.setVisibility(View.GONE);
        }

        // Store logo
        Glide.with(context)
                .load(ApiAddress.storeUrl + store.getStoreLogoUrl())
                .placeholder(R.drawable.product_sample)
                .error(R.drawable.product_sample)
                .into(holder.ivStoreLogo);

        // Item click -> go to StoreActivity
        holder.layoutStoreItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreActivity.class);
            intent.putExtra("store_id", store.getStoreId()); // pass store id
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return storeItems != null ? storeItems.size() : 0;
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutStoreItem;
        ImageView ivStoreLogo;
        TextView tvStoreName, tvStoreStatus, tvStoreDescription, tvStorePhone;
        LinearLayout layoutContactInfo;

        public StoreViewHolder(View itemView) {
            super(itemView);
            layoutStoreItem = itemView.findViewById(R.id.layoutStoreItem);
            ivStoreLogo = itemView.findViewById(R.id.ivStoreLogo);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvStoreStatus = itemView.findViewById(R.id.tvStoreStatus);
            tvStoreDescription = itemView.findViewById(R.id.tvStoreDescription);
            tvStorePhone = itemView.findViewById(R.id.tvStorePhone);
            layoutContactInfo = itemView.findViewById(R.id.layoutContactInfo);
        }
    }
}
