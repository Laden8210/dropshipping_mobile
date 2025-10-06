package com.example.dropshipping.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.model.CartProduct;

import java.util.List;

// ProductAdapter.java
public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {
    private List<CartProduct> products;
    private OnQuantityChangedListener quantityListener;
    private OnSelectionChangedListener selectionListener;
    private Context context;

    public interface OnQuantityChangedListener {
        void onQuantityChanged(String productId, int newQuantity);
    }


    public interface OnSelectionChangedListener {
        void onSelectionChanged(String productId, String name, double price, int quantity, boolean isSelected);
    }

    public CartProductAdapter(Context context, List<CartProduct> products,
                              OnQuantityChangedListener quantityListener,
                              OnSelectionChangedListener selectionListener) {
        this.context = context;
        this.products = products;
        this.quantityListener = quantityListener;
        this.selectionListener = selectionListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbProduct;
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvAttributes;
        TextView tvPrice;
        TextView tvQuantity;
        Button btnDecrease;
        Button btnIncrease;

        public ViewHolder(View itemView) {
            super(itemView);
            cbProduct = itemView.findViewById(R.id.cbProduct);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvAttributes = itemView.findViewById(R.id.tvAttributes);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartProduct product = products.get(position);

        Glide.with(holder.ivProduct.getContext())
                .load( ApiAddress.imageUrl + product.getImageRes())
                .placeholder(R.drawable.product_sample)
                .into(holder.ivProduct);
        holder.tvProductName.setText(product.getName());
        holder.tvAttributes.setText(product.getAttributes());
        holder.tvPrice.setText(String.format("₱%.2f", product.getPrice()));
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        holder.cbProduct.setChecked(product.isSelected());

        holder.cbProduct.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setSelected(isChecked);
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        isChecked
                );
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int newQty = product.getQuantity() + 1;
            holder.tvQuantity.setText(String.valueOf(newQty));
            if (quantityListener != null) {
                quantityListener.onQuantityChanged(product.getId(), newQty);
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                int newQty = product.getQuantity() - 1;
                holder.tvQuantity.setText(String.valueOf(newQty));
                if (quantityListener != null) {
                    quantityListener.onQuantityChanged(product.getId(), newQty);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}