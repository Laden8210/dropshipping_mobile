package com.example.dropshipping.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.CartStoreAdapter;
import com.example.dropshipping.model.CartProduct;
import com.example.dropshipping.model.Store;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

// CartActivity.java
public class CartActivity extends AppCompatActivity {
    private RecyclerView rvStores;
    private TextView tvItemsTotal, tvShippingTotal, tvTax, tvOrderTotal;
    private MaterialButton btnCheckout;
    private List<Store> stores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvStores = findViewById(R.id.rvStores);
        tvItemsTotal = findViewById(R.id.tvItemsTotal);
        tvShippingTotal = findViewById(R.id.tvShippingTotal);
        tvTax = findViewById(R.id.tvTax);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Sample data
        stores = new ArrayList<>();
        List<CartProduct> products1 = new ArrayList<>();
        products1.add(new CartProduct("prod1", "Men's Casual Shirt", "Color: Blue, Size: M", 29.99, R.drawable.product_sample));
        products1.add(new CartProduct("prod2", "Women's Jeans", "Color: Black, Size: S", 39.99, R.drawable.product_sample));
        stores.add(new Store("store1", "Fashion Store", products1, 4.99));

        List<CartProduct> products2 = new ArrayList<>();
        products2.add(new CartProduct("prod3", "Wireless Headphones", "Color: Black", 89.99, R.drawable.product_sample));
        stores.add(new Store("store2", "Electronics Store", products2, 2.99));

        // Setup adapter
        CartStoreAdapter adapter = new CartStoreAdapter(
                stores,
                (storeId, isSelected) -> handleStoreSelection(storeId, isSelected),
                (storeId, productId, isSelected) -> {
                    updateProductSelection(storeId, productId, isSelected);
                    updateOrderSummary();
                },
                (storeId, productId, newQuantity) -> {
                    updateProductQuantity(storeId, productId, newQuantity);
                    updateOrderSummary();
                }
        );

        rvStores.setLayoutManager(new LinearLayoutManager(this));
        rvStores.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> proceedToCheckout());
        updateOrderSummary();
    }

    private void handleStoreSelection(String storeId, boolean isSelected) {
        // Implement store-wide selection logic
        updateOrderSummary();
    }

    private void updateProductSelection(String storeId, String productId, boolean isSelected) {
        // Update product selection state in your data model
    }

    private void updateProductQuantity(String storeId, String productId, int newQuantity) {
        // Update product quantity in your data model
    }

    private void updateOrderSummary() {
        // Calculate totals across all stores
        double itemTotal = 0;
        double shippingTotal = 0;
        int itemCount = 0;

        // This should iterate through your actual data model
        for (Store store : stores) {
            for (CartProduct product : store.getProducts()) {
                if (product.isSelected()) {
                    itemTotal += product.getPrice() * product.getQuantity();
                    itemCount++;
                }
            }
            shippingTotal += store.getShippingFee();
        }

        double tax = itemTotal * 0.08; // Example tax calculation
        double orderTotal = itemTotal + shippingTotal + tax;

        tvItemsTotal.setText(String.format("$%.2f", itemTotal));
        tvShippingTotal.setText(String.format("$%.2f", shippingTotal));
        tvTax.setText(String.format("$%.2f", tax));
        tvOrderTotal.setText(String.format("$%.2f", orderTotal));
        btnCheckout.setText("Proceed to Checkout (" + itemCount + " items)");
    }

    private void proceedToCheckout() {
        startActivity(new Intent(this, OrderProductActivity.class));
    }
}