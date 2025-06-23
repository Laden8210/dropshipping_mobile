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
        holder.tvPrice.setText("₱" + item.getSellingPrice());

        if (item.getPrimaryImage() != null && !item.getPrimaryImage().isEmpty()) {
            Glide.with(context)
                    .load(item.getPrimaryImage())
                    .placeholder(R.drawable.product_sample)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.product_sample);
        }

        holder.cvProductItem.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("pid", item.getPid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productItem != null ? productItem.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private CardView cvProductItem;
        private ImageView imageView;
        private TextView tvProductName, tvPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            cvProductItem = itemView.findViewById(R.id.cvProductItem);
            imageView = itemView.findViewById(R.id.imageView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

}
