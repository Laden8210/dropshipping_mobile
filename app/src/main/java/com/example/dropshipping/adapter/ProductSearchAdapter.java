package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.model.ProductItem;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ProductSearchViewHolder> {

    private List<ProductItem> products;
    private List<ProductItem> productsFiltered;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onViewDetailsClick(ProductItem product);
    }

    public ProductSearchAdapter(OnProductClickListener listener) {
        this.products = new ArrayList<>();
        this.productsFiltered = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_search, parent, false);
        return new ProductSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSearchViewHolder holder, int position) {
        ProductItem product = productsFiltered.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productsFiltered.size();
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
        this.productsFiltered = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        productsFiltered.clear();
        if (query.isEmpty()) {
            productsFiltered.addAll(products);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (ProductItem product : products) {
                if (product.getProductName().toLowerCase().contains(lowerCaseQuery) ||
                        product.getProductSku().toLowerCase().contains(lowerCaseQuery) ||
                        product.getCategoryName().toLowerCase().contains(lowerCaseQuery)) {
                    productsFiltered.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ProductSearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productSku;
        private TextView productPrice;
        private TextView productStock;
        private MaterialButton viewDetailsButton;

        public ProductSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSku = itemView.findViewById(R.id.productSku);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStock = itemView.findViewById(R.id.productStock);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }

        public void bind(ProductItem product, OnProductClickListener listener) {
            productName.setText(product.getProductName());
            productSku.setText("SKU: " + product.getProductSku());

            // Format price
            try {
                double price = product.getSellingPrice();
                String currency = product.getConvertedCurrency();
                if (currency != null && !currency.isEmpty()) {
                    productPrice.setText(currency + " " + String.format(Locale.getDefault(), "%.2f", price));
                } else {
                    productPrice.setText("$" + String.format(Locale.getDefault(), "%.2f", price));
                }
            } catch (Exception e) {
                productPrice.setText(product.getPrice());
            }

            // Set stock with color indication
            int stock = product.getCurrentStock();
            productStock.setText("Stock: " + stock);
            if (stock > 50) {
                productStock.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            } else if (stock > 10) {
                productStock.setTextColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
            } else {
                productStock.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
            }

            // Load product image
            if (product.getPrimaryImage() != null && !product.getPrimaryImage().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getPrimaryImage())
                        .placeholder(R.drawable.product_sample)
                        .error(R.drawable.product_sample)
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.product_sample);
            }

            // Set click listener
            viewDetailsButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetailsClick(product);
                }
            });
        }
    }
}