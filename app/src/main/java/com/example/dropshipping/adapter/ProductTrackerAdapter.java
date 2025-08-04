package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductTrackerAdapter extends RecyclerView.Adapter<ProductTrackerAdapter.ViewHolder> {

    private final JSONArray products;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.productQuantity);
        }
    }

    public ProductTrackerAdapter(JSONArray products) {
        this.products = products;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject product = products.getJSONObject(position);
            holder.name.setText(product.getString("name"));
            holder.price.setText("₱" + product.getString("price"));
            holder.quantity.setText("Qty: " + product.getInt("quantity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return products.length();
    }
}