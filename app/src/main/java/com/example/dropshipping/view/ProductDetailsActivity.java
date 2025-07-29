package com.example.dropshipping.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;

import com.example.dropshipping.model.CartItem;
import com.example.dropshipping.model.CheckoutProduct;

import com.example.dropshipping.util.CartManager;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity implements PostCallback {

    private int pid;
    private int storeId;
    private MaterialButton buyNowBtn, addToCartBtn;
    private final DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize buttons
        buyNowBtn = findViewById(R.id.buyNowBtn);
        addToCartBtn = findViewById(R.id.materialButton);

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
                    double sellingPrice = data.optDouble("selling_price", 0);
                    String image = data.optString("primary_image");
                    int stock = data.optInt("current_stock", 0);
                    String sku = data.optString("product_sku");
                    String storeName = data.optString("store_name");
                    String storeLogo = data.optString("store_logo_url");
                    String storeAddress = data.optString("store_address");
                    int storeId = data.optInt("store_id", 0);


                    TextView nameView = findViewById(R.id.productName);
                    TextView descView = findViewById(R.id.productDescription);
                    ImageView imageView = findViewById(R.id.productImage);
                    TextView priceView = findViewById(R.id.productPrice);
                    TextView stockView = findViewById(R.id.productStock);
                    TextView skuView = findViewById(R.id.productSku);
                    TextView storeNameView = findViewById(R.id.storeName);
                    TextView storeAddressView = findViewById(R.id.storeAddress);
                    ImageView storeLogoView = findViewById(R.id.storeLogo);

                    nameView.setText(name);
                    priceView.setText("₱" + priceFormat.format(sellingPrice));
                    descView.setText(description);
                    skuView.setText("SKU: " + sku);
                    storeNameView.setText(storeName);
                    storeAddressView.setText(storeAddress);


                    if (stock > 0) {
                        stockView.setText("In Stock: " + stock + " items");
                        stockView.setTextColor(ContextCompat.getColor(this, R.color.green));
                    } else {
                        stockView.setText("Out of Stock");
                        stockView.setTextColor(ContextCompat.getColor(this, R.color.red));
                    }


                    if (!image.isEmpty()) {
                        String fullImageUrl =  ApiAddress.imageUrl  + image;
                        Glide.with(this)
                                .load(fullImageUrl)
                                .placeholder(R.drawable.product_sample)
                                .error(R.drawable.product_sample)
                                .into(imageView);
                    } else {
                        imageView.setImageResource(R.drawable.product_sample);
                    }


                    if (!storeLogo.isEmpty()) {
                        String storeLogoUrl = ApiAddress.imageUrl  + storeLogo;
                        Glide.with(this)
                                .load(storeLogoUrl)
                                .placeholder(R.drawable.ic_user_profile)
                                .circleCrop()
                                .into(storeLogoView);
                    } else {
                        storeLogoView.setImageResource(R.drawable.ic_user_profile);
                    }


                    boolean inStock = stock > 0;
                    buyNowBtn.setEnabled(inStock);
                    addToCartBtn.setEnabled(inStock);
                    buyNowBtn.setAlpha(inStock ? 1f : 0.5f);
                    addToCartBtn.setAlpha(inStock ? 1f : 0.5f);

                    buyNowBtn.setOnClickListener(v -> {
                        CheckoutProduct product = new CheckoutProduct(
                                pid,
                                name,
                                sellingPrice,
                                1,
                                storeId

                        );
                        Intent intent = new Intent(this, OrderProductActivity.class);
                        intent.putExtra("checkoutProduct", product);
                        startActivity(intent);
                    });

                    addToCartBtn.setOnClickListener(v -> {
                        CartItem item = new CartItem(
                                pid,
                                sellingPrice,
                                1,
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

                    });

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

    @Override
    public void onPostError(String errorMessage) {
        runOnUiThread(() -> {
            showLoading(false);
            Messenger.showAlertDialog(this, "Error", errorMessage, "OK");
        });
    }
}