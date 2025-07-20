package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.model.CartProduct;
import com.example.dropshipping.model.Store;

import java.util.List;

// StoreAdapter.java
public class CartStoreAdapter extends RecyclerView.Adapter<CartStoreAdapter.ViewHolder> {
    private List<Store> stores;
    private OnStoreSelectionChangedListener storeSelectionListener;
    private OnProductSelectionChangedListener productSelectionListener;
    private OnProductQuantityChangedListener quantityListener;

    public interface OnStoreSelectionChangedListener {
        void onStoreSelectionChanged(String storeId, boolean isSelected);
    }

    public interface OnProductSelectionChangedListener {
        void onProductSelectionChanged(String storeId, String productId, boolean isSelected);
    }

    public interface OnProductQuantityChangedListener {
        void onProductQuantityChanged(String storeId, String productId, int newQuantity);
    }

    public CartStoreAdapter(List<Store> stores,
                        OnStoreSelectionChangedListener storeSelectionListener,
                        OnProductSelectionChangedListener productSelectionListener,
                        OnProductQuantityChangedListener quantityListener) {
        this.stores = stores;
        this.storeSelectionListener = storeSelectionListener;
        this.productSelectionListener = productSelectionListener;
        this.quantityListener = quantityListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbStore;
        TextView tvStoreName;
        RecyclerView rvProducts;
        TextView tvSubtotal;
        TextView tvSubtotalLabel;
        TextView tvShipping;

        public ViewHolder(View itemView) {
            super(itemView);
            cbStore = itemView.findViewById(R.id.cbStore);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            rvProducts = itemView.findViewById(R.id.rvProducts);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            tvSubtotalLabel = itemView.findViewById(R.id.tvSubtotalLabel);
            tvShipping = itemView.findViewById(R.id.tvShipping);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.tvStoreName.setText(store.getName());

        // Setup products RecyclerView
        CartProductAdapter productAdapter = new CartProductAdapter(
                holder.itemView.getContext(),
                store.getProducts(),
                new CartProductAdapter.OnQuantityChangedListener() {
                    @Override
                    public void onQuantityChanged(String productId, int newQuantity) {
                        if (quantityListener != null) {
                            quantityListener.onProductQuantityChanged(store.getId(), productId, newQuantity);
                        }
                    }
                },
                new CartProductAdapter.OnSelectionChangedListener() {
                    @Override
                    public void onSelectionChanged(String productId, boolean isSelected) {
                        if (productSelectionListener != null) {
                            productSelectionListener.onProductSelectionChanged(store.getId(), productId, isSelected);
                        }
                    }
                }
        );

        holder.rvProducts.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvProducts.setAdapter(productAdapter);

        // Calculate store totals
        double subtotal = 0;
        int itemCount = 0;
        for (CartProduct product : store.getProducts()) {
            if (product.isSelected()) {
                subtotal += product.getPrice() * product.getQuantity();
                itemCount++;
            }
        }

        holder.tvSubtotalLabel.setText("Subtotal (" + itemCount + " items)");
        holder.tvSubtotal.setText(String.format("$%.2f", subtotal));
        holder.tvShipping.setText(String.format("$%.2f", store.getShippingFee()));

        // Store checkbox logic
        boolean allSelected = true;
        for (CartProduct product : store.getProducts()) {
            if (!product.isSelected()) {
                allSelected = false;
                break;
            }
        }
        holder.cbStore.setChecked(allSelected);

        holder.cbStore.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (storeSelectionListener != null) {
                storeSelectionListener.onStoreSelectionChanged(store.getId(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
}