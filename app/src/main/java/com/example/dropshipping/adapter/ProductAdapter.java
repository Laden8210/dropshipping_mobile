package com.example.dropshipping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.model.ProductItem;
import com.example.dropshipping.view.ProductDetailsActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<ProductItem> productItem;

    public ProductAdapter(Context context, List<ProductItem> productItem) {
        this.context = context;
        this.productItem = productItem;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_product, parent, false);
        return new ProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductItem item = productItem.get(position);

        holder.tvProductName.setText(item.getProductName());
        holder.tvPrice.setText(item.getDisplayPrice());

        // Show stock status
        if (item.hasStock()) {
            holder.tvStock.setText(item.getStockStatus());
            if (item.getTotalStock() < 10) {
                holder.tvStock.setTextColor(context.getResources().getColor(R.color.orange));
            } else {
                holder.tvStock.setTextColor(context.getResources().getColor(R.color.green));
            }
        } else {
            holder.tvStock.setText("Out of Stock");
            holder.tvStock.setTextColor(context.getResources().getColor(R.color.red));
        }

        // Load image
        if (item.getPrimaryImage() != null && !item.getPrimaryImage().isEmpty()) {
            Glide.with(context)
                    .load(ApiAddress.imageUrl + item.getPrimaryImage())
                    .placeholder(R.drawable.product_sample)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.product_sample);
        }

        // Show category
        holder.tvCategory.setText(item.getCategoryName());

        holder.cvProductItem.setOnClickListener(view -> {
            if (item.hasStock()) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("pid", item.getPid());
                intent.putExtra("storeId", item.getStoreId());
                context.startActivity(intent);
            } else {
                // Show out of stock message
                android.widget.Toast.makeText(context, "This product is currently out of stock", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItem != null ? productItem.size() : 0;
    }

    public void updateList(List<ProductItem> newList) {
        productItem = newList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private CardView cvProductItem;
        private ImageView imageView;
        private TextView tvProductName, tvPrice, tvStock, tvCategory;

        public ProductViewHolder(View itemView) {
            super(itemView);
            cvProductItem = itemView.findViewById(R.id.cvProductItem);
            imageView = itemView.findViewById(R.id.imageView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}