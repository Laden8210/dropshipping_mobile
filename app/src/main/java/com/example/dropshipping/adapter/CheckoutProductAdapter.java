package com.example.dropshipping.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.model.CheckoutProduct;
import com.example.dropshipping.R;
import com.example.dropshipping.view.OrderProductActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ProductViewHolder> {


    private List<CheckoutProduct> productList;
    private OnQuantityChangedListener listener;

    public List<CheckoutProduct> getProductList() {
        return productList;
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged(int position, int newQuantity);
    }

    public CheckoutProductAdapter(List<CheckoutProduct> productList, OnQuantityChangedListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_checkout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CheckoutProduct product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("₱%.2f", product.getPrice()));
        holder.etQuantity.setText(String.valueOf(product.getQuantity()));

        // Handle image loading with Glide/Picasso if needed
         Glide.with(holder.itemView).load( ApiAddress.imageUrl + product.getImageUrl()).into(holder.productImage);

        Log.d("Check out Image URL", product.getImageUrl());

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = product.getQuantity() + 1;
            product.setQuantity(newQuantity);
            holder.etQuantity.setText(String.valueOf(newQuantity));
            if(listener != null) listener.onQuantityChanged(position, newQuantity);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if(product.getQuantity() > 1) {
                int newQuantity = product.getQuantity() - 1;
                product.setQuantity(newQuantity);
                holder.etQuantity.setText(String.valueOf(newQuantity));
                if(listener != null) listener.onQuantityChanged(position, newQuantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button btnIncrease, btnDecrease;

        TextInputEditText etQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}