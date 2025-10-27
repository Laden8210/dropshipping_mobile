package com.example.dropshipping.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.CartStoreAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.ApiResponse;
import com.example.dropshipping.model.CartItem;
import com.example.dropshipping.model.CartProduct;
import com.example.dropshipping.model.CheckoutProduct;
import com.example.dropshipping.model.Store;
import com.example.dropshipping.model.StoreCart;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// CartActivity.java
public class CartActivity extends AppCompatActivity implements PostCallback {
    private RecyclerView rvStores;
    private TextView tvItemsTotal, tvOrderTotal;
    private MaterialButton btnCheckout;
    private List<Store> stores;
    private CartStoreAdapter adapter;
    private Gson gson = new Gson();

    private List<CheckoutProduct> checkoutProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvStores = findViewById(R.id.rvStores);
        tvItemsTotal = findViewById(R.id.tvItemsTotal);

        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        stores = new ArrayList<>();


        adapter = new CartStoreAdapter(
                stores,
                (storeId,  isSelected) -> handleStoreSelection(storeId, isSelected),
                (cartId, storeId, productId, variantId, name, sellingPrice, quantity, weight, height, isSelected, imageUrl) -> {
                    updateProductSelection(cartId, storeId, productId, variantId, name, sellingPrice, quantity, weight, height, isSelected, imageUrl);
                    updateOrderSummary();
                },
                (storeId, productId, newQuantity) -> {
                    updateProductQuantity(storeId, productId, newQuantity);
                    updateOrderSummary();
                }
        );
        rvStores.setLayoutManager(new LinearLayoutManager(this));
        rvStores.setAdapter(adapter);

        // Fetch cart data
        try {
            JSONObject jsonObject = new JSONObject();
            new PostTask(this, this, "error", "cart/get-cart.php").execute(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnCheckout.setOnClickListener(v -> proceedToCheckout());
        updateOrderSummary();
    }

    private void handleStoreSelection(String storeId, boolean isSelected) {

        updateOrderSummary();
    }

    private void updateProductSelection(String cartId, String storeId, String productId, String variantId, String name, double sellingPrice, int quantity, double weight, double height, boolean isSelected, String imageUrl) {
        if (isSelected) {
            Log.d("CartActivity", "Adding product to checkout: " + productId + " from store: " + storeId + " with quantity: " + quantity);
            checkoutProducts.add(new CheckoutProduct(
                    cartId,
                    Integer.parseInt(productId),
                    Integer.parseInt(variantId),
                    name,
                    sellingPrice,
                    quantity,
                    weight,
                    height,
                    Integer.parseInt(storeId),
                    imageUrl

            ));
        } else {

            for (Iterator<CheckoutProduct> iterator = checkoutProducts.iterator(); iterator.hasNext();) {
                CheckoutProduct cp = iterator.next();
                if (cp.getPid() == Integer.parseInt(productId) &&
                        cp.getStoreId() == Integer.parseInt(storeId)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private void updateProductQuantity(String storeId, String productId, int newQuantity) {

        for (CheckoutProduct cp : checkoutProducts) {
            if (cp.getPid() == Integer.parseInt(productId) &&
                    cp.getStoreId() == Integer.parseInt(storeId)) {
                cp.setQuantity(newQuantity);
                break;
            }
        }
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

        double tax = itemTotal * 0.08;
        double orderTotal = itemTotal + shippingTotal + tax;

        tvItemsTotal.setText(String.format("₱%.2f", itemTotal));

        tvOrderTotal.setText(String.format("₱%.2f", itemTotal));
        btnCheckout.setText("Proceed to Checkout (" + itemCount + " items)");
    }

    private void proceedToCheckout() {
        if (checkoutProducts.isEmpty()) {
            Toast.makeText(this, "Please select items to checkout", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, OrderProductActivity.class);
        intent.putExtra("checkout_products", new Gson().toJson(checkoutProducts));
        startActivity(intent);
    }

    @Override
    public void onPostSuccess(String responseData) {
        try {

            JSONObject jsonResponse = new JSONObject(responseData);
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                JSONArray data = jsonResponse.getJSONArray("data");
                stores.clear();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject storeJson = data.getJSONObject(i);
                    int store_id = storeJson.getInt("store_id");
                    String store_name = storeJson.getString("store_name");
                    JSONArray items = storeJson.getJSONArray("items");

                    List<CartProduct> products = new ArrayList<>();
                    for (int j = 0; j < items.length(); j++) {
                        JSONObject item = items.getJSONObject(j);
                        int cart_id = item.getInt("cart_id");
                        int product_id = item.getInt("product_id");
                        String product_name = item.getString("product_name");
                        String description = item.getString("description");
                        double converted_price = item.getDouble("selling_price");
                        String product_image = item.getString("product_image");
                        int quantity = item.getInt("quantity");
                        int variant_id = item.getInt("variation_id");


                        products.add(new CartProduct(
                                String.valueOf(cart_id),
                                String.valueOf(product_id),
                                String.valueOf(variant_id),
                                product_name,
                                description,
                                converted_price,
                                product_image,
                                quantity
                        ));
                    }
                    stores.add(new Store(
                            String.valueOf(store_id),
                            store_name,
                            products,
                            100.0
                    ));
                }
                adapter.notifyDataSetChanged();
                updateOrderSummary();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostError(String errorMessage) {

    }
}