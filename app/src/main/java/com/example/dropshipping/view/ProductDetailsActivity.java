package com.example.dropshipping.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.CartItem;
import com.example.dropshipping.model.CheckoutProduct;
import com.example.dropshipping.model.ProductVariation;
import com.example.dropshipping.util.CartManager;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity implements PostCallback {

    private int pid;
    private int storeId;
    private MaterialButton buyNowBtn, addToCartBtn;
    private Spinner variationSpinner;
    private TextView priceView, stockView;
    private final DecimalFormat priceFormat = new DecimalFormat("#,##0.00");
    private List<ProductVariation> variations = new ArrayList<>();
    private ProductVariation selectedVariation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize views
        buyNowBtn = findViewById(R.id.buyNowBtn);
        addToCartBtn = findViewById(R.id.materialButton);
        variationSpinner = findViewById(R.id.variationSpinner);
        priceView = findViewById(R.id.productPrice);
        stockView = findViewById(R.id.productStock);

        // Handle back button in toolbar
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        if (getIntent().hasExtra("pid")) {
            this.pid = getIntent().getIntExtra("pid", 0);
            this.storeId = getIntent().getIntExtra("storeId", 0);

            // Show loading state
            showLoading(true);

            JSONObject params = new JSONObject();
            try {
                params.put("pid", pid);
                params.put("store_id", storeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PostTask(this, this, "error", "items/single-product.php").execute(params);
        }
    }

    private void showLoading(boolean isLoading) {
        findViewById(R.id.scrollView2).setVisibility(isLoading ? View.GONE : View.VISIBLE);
        //findViewById(R.id.progressBar).setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPostSuccess(String responseData) {
        runOnUiThread(() -> {
            showLoading(false);
            try {
                JSONObject response = new JSONObject(responseData);
                if (response.getString("status").equals("success")) {
                    JSONObject data = response.getJSONObject("data");

                    String name = data.optString("product_name");
                    String description = data.optString("description");
                    String image = data.optString("primary_image");
                    String sku = data.optString("product_sku");
                    String storeName = data.optString("store_name");
                    String storeLogo = data.optString("store_logo_url");
                    String storeAddress = data.optString("store_address");
                    int storeId = data.optInt("store_id", 0);

                    // Parse variations
                    JSONArray variationsArray = data.getJSONArray("variations");
                    variations.clear();
                    for (int i = 0; i < variationsArray.length(); i++) {
                        JSONObject variationObj = variationsArray.getJSONObject(i);
                        ProductVariation variation = new ProductVariation(
                                variationObj.getInt("variation_id"),
                                variationObj.getInt("product_id"),
                                variationObj.optString("size"),
                                variationObj.optString("color"),
                                variationObj.optDouble("weight", 0),
                                variationObj.optDouble("length", 0),
                                variationObj.optDouble("width", 0),
                                variationObj.optDouble("height", 0),
                                variationObj.optDouble("price", 0),
                                variationObj.optString("currency"),
                                variationObj.optString("sku_suffix"),
                                variationObj.optInt("stock_quantity", 0),
                                variationObj.optInt("is_active", 1) == 1,
                                variationObj.optDouble("selling_price", 0),
                                variationObj.optDouble("converted_price", 0),
                                variationObj.optString("converted_currency"),
                                data.optString("primary_image_url")
                        );
                        variations.add(variation);
                    }

                    // Update UI with basic product info
                    updateProductInfo(name, description, image, sku, storeName, storeLogo, storeAddress);

                    // Setup variation spinner
                    setupVariationSpinner();

                    // Setup button listeners
                    setupButtonListeners();

                } else {
                    Messenger.showAlertDialog(this, "Error",
                            response.optString("message", "Failed to load product details"), "OK");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Messenger.showAlertDialog(this, "Error", "Failed to parse product details", "OK");
            }
        });
    }

    private void updateProductInfo(String name, String description, String image, String sku,
                                   String storeName, String storeLogo, String storeAddress) {
        TextView nameView = findViewById(R.id.productName);
        TextView descView = findViewById(R.id.productDescription);
        ImageView imageView = findViewById(R.id.productImage);
        TextView skuView = findViewById(R.id.productSku);
        TextView storeNameView = findViewById(R.id.storeName);
        TextView storeAddressView = findViewById(R.id.storeAddress);
        ImageView storeLogoView = findViewById(R.id.storeLogo);

        nameView.setText(name);
        descView.setText(description);
        skuView.setText("SKU: " + sku);
        storeNameView.setText(storeName);
        storeAddressView.setText(storeAddress);

        // Load product image
        if (!image.isEmpty()) {
            String fullImageUrl = ApiAddress.imageUrl + image;
            Glide.with(this)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.product_sample)
                    .error(R.drawable.product_sample)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.product_sample);
        }

        // Load store logo
        if (!storeLogo.isEmpty()) {
            String storeLogoUrl = ApiAddress.imageUrl + storeLogo;
            Glide.with(this)
                    .load(storeLogoUrl)
                    .placeholder(R.drawable.ic_user_profile)
                    .circleCrop()
                    .into(storeLogoView);
        } else {
            storeLogoView.setImageResource(R.drawable.ic_user_profile);
        }
    }

    private void setupVariationSpinner() {
        List<String> variationOptions = new ArrayList<>();
        for (ProductVariation variation : variations) {
            String option = buildVariationOption(variation);
            variationOptions.add(option);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, variationOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        variationSpinner.setAdapter(adapter);

        variationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVariation = variations.get(position);
                updatePriceAndStock();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedVariation = null;
                updateButtonStates(false);
            }
        });

        // Select first variation by default if available
        if (!variations.isEmpty()) {
            variationSpinner.setSelection(0);
        }
    }

    private String buildVariationOption(ProductVariation variation) {
        StringBuilder option = new StringBuilder();

        if (variation.getSize() != null && !variation.getSize().isEmpty()) {
            option.append("Size: ").append(variation.getSize());
        }

        if (variation.getColor() != null && !variation.getColor().isEmpty()) {
            if (option.length() > 0) option.append(" | ");
            option.append("Color: ").append(variation.getColor());
        }

        // If no size or color, show variation ID
        if (option.length() == 0) {
            option.append("Option ").append(variation.getVariationId());
        }

        // Add stock indicator
        option.append(" (").append(variation.getStockQuantity()).append(" in stock)");

        return option.toString();
    }

    private void updatePriceAndStock() {
        if (selectedVariation != null) {
            double sellingPrice = selectedVariation.getSellingPrice();
            int stock = selectedVariation.getStockQuantity();

            priceView.setText("₱" + priceFormat.format(sellingPrice));

            if (stock > 0) {
                stockView.setText("In Stock: " + stock + " items");
                stockView.setTextColor(ContextCompat.getColor(this, R.color.green));
                updateButtonStates(true);
            } else {
                stockView.setText("Out of Stock");
                stockView.setTextColor(ContextCompat.getColor(this, R.color.red));
                updateButtonStates(false);
            }
        }
    }

    private void updateButtonStates(boolean enabled) {
        buyNowBtn.setEnabled(enabled);
        addToCartBtn.setEnabled(enabled);
        buyNowBtn.setAlpha(enabled ? 1f : 0.5f);
        addToCartBtn.setAlpha(enabled ? 1f : 0.5f);
    }

    private void setupButtonListeners() {
        buyNowBtn.setOnClickListener(v -> {
            if (selectedVariation != null && selectedVariation.getStockQuantity() > 0) {
                showQuantityDialog(true); // true for buy now
            }
        });

        addToCartBtn.setOnClickListener(v -> {
            if (selectedVariation != null && selectedVariation.getStockQuantity() > 0) {
                showQuantityDialog(false); // false for add to cart
            }
        });
    }

    private void showQuantityDialog(boolean isBuyNow) {
        int maxQuantity = selectedVariation.getStockQuantity();

        // Create custom dialog view
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quantity_selector, null);

        TextView titleText = dialogView.findViewById(R.id.dialogTitle);
        TextInputEditText quantityText = dialogView.findViewById(R.id.quantityInput);
        MaterialButton decreaseBtn = dialogView.findViewById(R.id.decreaseBtn);
        MaterialButton increaseBtn = dialogView.findViewById(R.id.increaseBtn);
        MaterialButton confirmBtn = dialogView.findViewById(R.id.confirmBtn);

        // Set dialog title based on action
        titleText.setText(isBuyNow ? "Select Quantity (Buy Now)" : "Select Quantity (Add to Cart)");

        // Initialize quantity
        final int[] quantity = {1};
        quantityText.setText(String.valueOf(quantity[0]));

        // Setup button listeners
        decreaseBtn.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityText.setText(String.valueOf(quantity[0]));
                updateQuantityButtons(quantity[0], maxQuantity, decreaseBtn, increaseBtn);
            }
        });

        increaseBtn.setOnClickListener(v -> {
            if (quantity[0] < maxQuantity) {
                quantity[0]++;
                quantityText.setText(String.valueOf(quantity[0]));
                updateQuantityButtons(quantity[0], maxQuantity, decreaseBtn, increaseBtn);
            }
        });

        // Initialize button states
        updateQuantityButtons(quantity[0], maxQuantity, decreaseBtn, increaseBtn);

        // Create dialog
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // Handle confirm button click
        confirmBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if (isBuyNow) {
                proceedToBuyNow(quantity[0]);
            } else {
                addToCart(quantity[0]);
            }
        });
    }

    private void updateQuantityButtons(int currentQuantity, int maxQuantity,
                                       MaterialButton decreaseBtn, MaterialButton increaseBtn) {
        // Update decrease button
        decreaseBtn.setEnabled(currentQuantity > 1);
        decreaseBtn.setAlpha(currentQuantity > 1 ? 1f : 0.5f);

        // Update increase button
        increaseBtn.setEnabled(currentQuantity < maxQuantity);
        increaseBtn.setAlpha(currentQuantity < maxQuantity ? 1f : 0.5f);
    }
    ;
    private void proceedToBuyNow(int quantity) {
        CheckoutProduct product = new CheckoutProduct(
                pid,
                selectedVariation.getVariationId(),
                getProductNameWithVariation(),
                selectedVariation.getSellingPrice(),
                quantity,
                selectedVariation.getWeight(),
                selectedVariation.getLength(),
                storeId,
                selectedVariation.getImageUrl()
        );
        Intent intent = new Intent(this, OrderProductActivity.class);
        intent.putExtra("checkoutProduct", product);
        startActivity(intent);
    }

    private void addToCart(int quantity) {
        CartItem item = new CartItem(
                pid,
                selectedVariation.getVariationId(),
                selectedVariation.getSellingPrice(),
                quantity,
                storeId
        );
        CartManager.addToCart(item, new CartManager.CartUpdateListener() {
            @Override
            public void onSuccess(String message, CartItem items) {
                Messenger.showAlertDialog(ProductDetailsActivity.this, "Success",
                        message, "OK", "Back to Home", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProductDetailsActivity.this, HeroActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
            }

            @Override
            public void onError(String error) {
                Messenger.showAlertDialog(ProductDetailsActivity.this, "Error",
                        error, "OK").show();
            }
        }, this);
    }

    private String getProductNameWithVariation() {
        TextView nameView = findViewById(R.id.productName);
        String baseName = nameView.getText().toString();

        if (selectedVariation != null) {
            StringBuilder variationInfo = new StringBuilder();
            if (selectedVariation.getSize() != null && !selectedVariation.getSize().isEmpty()) {
                variationInfo.append(" - ").append(selectedVariation.getSize());
            }
            if (selectedVariation.getColor() != null && !selectedVariation.getColor().isEmpty()) {
                if (variationInfo.length() == 0) variationInfo.append(" - ");
                else variationInfo.append(", ");
                variationInfo.append(selectedVariation.getColor());
            }
            return baseName + variationInfo.toString();
        }

        return baseName;
    }

    @Override
    public void onPostError(String errorMessage) {
        runOnUiThread(() -> {
            showLoading(false);
            Messenger.showAlertDialog(this, "Error", errorMessage, "OK");
        });
    }
}