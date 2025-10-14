package com.example.dropshipping.util;

import android.content.Context;

import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.CartItem;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    public interface CartUpdateListener {
        void onSuccess(String message, CartItem items);

        void onError(String error);
    }

    public interface CartRetrievalListener {
        void onSuccess(String message, List<CartItem> items);

        void onError(String error);
    }

    public interface CartClearListener {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void addToCart(CartItem item, CartUpdateListener listener, Context context) {
        if (item == null) {
            listener.onError("Item cannot be null");
            return;
        }

        if (item.getProduct_id() <= 0 || item.getStoreId() <= 0 || item.getQuantity() <= 0) {
            listener.onError("Invalid item details");
            return;
        }

        JSONObject data = new JSONObject();
        try {
            data.put("product_id", item.getProduct_id());
            data.put("variation_id", item.getVariation_id());
            data.put("store_id", item.getStoreId());
            data.put("quantity", item.getQuantity());

        } catch (Exception e) {
            listener.onError("Error creating JSON data: " + e.getMessage());
            return;
        }
        try {
            new PostTask(context, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            listener.onSuccess("Item added to cart", item);
                        } else {
                            listener.onError("Failed to add item: " + response.getString("message"));
                        }
                    } catch (Exception e) {
                        listener.onError("Error parsing response: " + e.getMessage());
                    }

                }

                @Override
                public void onPostError(String errorMessage) {
                    listener.onError("Error adding item to cart: " + errorMessage);

                }
            }, "cart/add", "cart/add-to-cart.php").execute(data);

        } catch (Exception e) {
            listener.onError("Error checking existing items: " + e.getMessage());
        }

    }

    public static void getCart(CartRetrievalListener listener, Context context) {
        try {
            new PostTask(context, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getBoolean("success")) {
                            List<CartItem> cartItems = new ArrayList<>();
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                JSONObject itemJson = response.getJSONArray("data").getJSONObject(i);
                                CartItem item = new CartItem(
                                        itemJson.getInt("product_id"),
                                        itemJson.getInt("variation_id"),
                                        itemJson.getDouble("price"),
                                        itemJson.getInt("quantity"),
                                        itemJson.getInt("store_id")
                                );
                                cartItems.add(item);
                            }
                            listener.onSuccess("Cart retrieved successfully", cartItems);
                        } else {
                            listener.onError("Failed to retrieve cart: " + response.getString("message"));
                        }
                    } catch (Exception e) {
                        listener.onError("Error parsing response: " + e.getMessage());
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    listener.onError("Error retrieving cart: " + errorMessage);
                }
            }, "cart/retrieve", "cart/retrieve-cart.php").execute(new JSONObject());
        } catch (Exception e) {
            listener.onError("Error initiating cart retrieval: " + e.getMessage());
        }

    }

    public static void clearCart(CartClearListener listener, Context context) {
        try {
            new PostTask(context, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getBoolean("success")) {
                            listener.onSuccess("Cart cleared successfully");
                        } else {
                            listener.onError("Failed to clear cart: " + response.getString("message"));
                        }
                    } catch (Exception e) {
                        listener.onError("Error parsing response: " + e.getMessage());
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    listener.onError("Error clearing cart: " + errorMessage);
                }
            }, "cart/clear", "cart/clear-cart.php").execute(new JSONObject());
        } catch (Exception e) {
            listener.onError("Error initiating cart clear: " + e.getMessage());
        }

    }
}