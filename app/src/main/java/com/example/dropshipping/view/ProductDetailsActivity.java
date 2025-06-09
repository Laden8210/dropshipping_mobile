package com.example.dropshipping.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dropshipping.R;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.util.Messenger;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity implements PostCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);

        if (getIntent().hasExtra("pid")){
            String pid = getIntent().getStringExtra("pid");

            JSONObject params = new JSONObject();
            try {
                params.put("pid", pid);
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

                String name = data.optString("productNameEn");
                String description = data.optString("description").replaceAll("(?i)<img[^>]*>", "");
                String price = data.optString("exchangeRate");
                JSONArray images = data.optJSONArray("productImageSet");

                // Update views
                TextView nameView = findViewById(R.id.productName);
                TextView descView = findViewById(R.id.productDescription);
                ImageView imageView = findViewById(R.id.productImage);
                TextView priceView = findViewById(R.id.productPrice);

                nameView.setText(name);

                priceView.setText("₱ " + price);

                // Optional: Convert HTML description to Spanned
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    descView.setText(android.text.Html.fromHtml(description, android.text.Html.FROM_HTML_MODE_LEGACY));
                } else {
                    descView.setText(android.text.Html.fromHtml(description));
                }


                // Load first image with Glide
                if (images != null && images.length() > 0) {
                    String imageUrl = images.getString(0);
                    com.bumptech.glide.Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.product_sample)
                            .into(imageView);
                }

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