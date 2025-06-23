package com.example.dropshipping.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.CheckoutProduct;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity implements PostCallback {

    private MaterialButton buyNowBtn;
    private int pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);

        buyNowBtn = findViewById(R.id.buyNowBtn);



        if (getIntent().hasExtra("pid")){
            this.pid = getIntent().getIntExtra("pid", 0);

            JSONObject params = new JSONObject();
            try {
                params.put("pid", pid);
                Log.d("ProductDetailsActivity", "Fetching product with PID: " + pid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PostTask(this, this, "error", "items/single-product.php").execute(params);

        }

    }

    @Override
    public void onPostSuccess(String responseData) {
        try {
            JSONObject response = new JSONObject(responseData);
            if (response.getString("status").equals("success")) {
                JSONObject data = response.getJSONObject("data");

                String name = data.optString("product_name");
                String description = data.optString("description");
                String price = data.optString("selling_price");
                String images = data.optString("primary_image");

                // Update views
                TextView nameView = findViewById(R.id.productName);
                TextView descView = findViewById(R.id.productDescription);
                ImageView imageView = findViewById(R.id.productImage);
                TextView priceView = findViewById(R.id.productPrice);

                nameView.setText(name);

                priceView.setText("₱ " + price);

                descView.setText(description);

                if (images != null && !images.isEmpty()) {
                    Glide.with(this)
                            .load(images)
                            .placeholder(R.drawable.product_sample)
                            .into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.product_sample);
                }

                buyNowBtn.setOnClickListener(view -> {
                    Intent intent = new Intent(this, OrderProductActivity.class);

                    CheckoutProduct checkoutProduct = new CheckoutProduct(
                            pid,
                            name,
                            Double.parseDouble(price),
                            1
                    );
                    intent.putExtra("checkoutProduct", checkoutProduct);

                    startActivity(intent);
                });

            } else {
                Messenger.showAlertDialog(this, "Failed", response.getString("message"), "Ok");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Messenger.showAlertDialog(this, "Parse Error", "Failed to parse product details.", "Ok");
        }
    }
    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Error", errorMessage, "Ok");
    }
}