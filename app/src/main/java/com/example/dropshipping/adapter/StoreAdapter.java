package com.example.dropshipping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
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

        holder.layoutStoreItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {

        return storeItems != null ? storeItems.size() : 0;
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutStoreItem;

        public StoreViewHolder(View itemView) {
            super(itemView);
            layoutStoreItem = itemView.findViewById(R.id.layoutStoreItem);

        }
    }
}
