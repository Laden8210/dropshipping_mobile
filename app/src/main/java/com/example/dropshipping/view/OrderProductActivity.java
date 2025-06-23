package com.example.dropshipping.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
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
import com.example.dropshipping.adapter.CheckoutProductAdapter;
import com.example.dropshipping.adapter.ProductAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.CheckoutProduct;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderProductActivity extends AppCompatActivity implements CheckoutProductAdapter.OnQuantityChangedListener, PostCallback {

    private RecyclerView productsRecyclerView;
    private CheckoutProductAdapter productAdapter;
    private TextView subtotalPrice, shippingPrice, taxPrice, totalPrice;
    private MaterialButton placeOrderButton;
    private RadioGroup paymentMethodGroup;
    private double subtotal, shipping, tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        // Initialize views
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        subtotalPrice = findViewById(R.id.subtotalPrice);
        shippingPrice = findViewById(R.id.shippingPrice);
        taxPrice = findViewById(R.id.taxPrice);
        totalPrice = findViewById(R.id.totalPrice);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        // Setup RecyclerView
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new CheckoutProductAdapter(getSampleProducts(), this);
        productsRecyclerView.setAdapter(productAdapter);

        // Calculate initial totals
        calculateTotals();

        // Set address details (could be loaded from user profile)
        TextView addressName = findViewById(R.id.addressName);
        TextView addressStreet = findViewById(R.id.addressStreet);
        TextView addressCity = findViewById(R.id.addressCity);
        TextView addressPhone = findViewById(R.id.addressPhone);

        addressName.setText("John Doe");
        addressStreet.setText("123 Main Street");
        addressCity.setText("New York, NY 10001");
        addressPhone.setText("+1 (555) 123-4567");

        // Place Order button click
        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private List<CheckoutProduct> getSampleProducts() {
        List<CheckoutProduct> products = new ArrayList<>();

        if (getIntent().hasExtra("checkoutProduct")) {
           CheckoutProduct product = getIntent().getParcelableExtra("checkoutProduct");
            if (product != null) {
                products.add(product);
            }
        }

        return products;
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        calculateTotals();
    }

    private void calculateTotals() {
        subtotal = 0;
        for (CheckoutProduct product : productAdapter.getProductList()) {
            subtotal += product.getPrice() * product.getQuantity();
        }

        shipping = 100.00;
        tax = subtotal * 0.12;

        subtotalPrice.setText(String.format("₱%.2f", subtotal));
        shippingPrice.setText(String.format("₱%.2f", shipping));
        taxPrice.setText(String.format("₱%.2f", tax));
        totalPrice.setText(String.format("₱%.2f", subtotal + shipping + tax));
    }

    private void placeOrder() {
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        String paymentMethod = "";

        if (selectedId == R.id.creditCardOption) {
            paymentMethod = "credit_card";
        } else if (selectedId == R.id.paypalOption) {
            paymentMethod = "paypal";
        } else if (selectedId == R.id.cashOption) {
            paymentMethod = "cod";
        }

        JSONObject orderDetails = new JSONObject();
        try {
            orderDetails.put("payment_method", paymentMethod);
            orderDetails.put("subtotal", this.subtotal);
            orderDetails.put("shipping", this.shipping);
            orderDetails.put("tax", this.tax);
            orderDetails.put("total", this.subtotal + this.shipping + this.tax);

            JSONArray productsArray = new JSONArray();
            for (CheckoutProduct product : productAdapter.getProductList()) {
                JSONObject productObject = new JSONObject();
                productObject.put("pid", product.getPid());
                productObject.put("name", product.getName());
                productObject.put("price", product.getPrice());
                productObject.put("quantity", product.getQuantity());
                productsArray.put(productObject);
            }
            orderDetails.put("products", productsArray);

            new PostTask(this, this, "error", "order/place-order.php").execute(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPostSuccess(String responseData) {
        try {
            JSONObject response = new JSONObject(responseData);
            if (response.getString("status").equals("success")) {
               Messenger.showAlertDialog(this, "Order Success", "Your order has been placed successfully!", "Ok", "Back", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       startActivity(new Intent(OrderProductActivity.this, HeroActivity.class));
                   }
               }, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       startActivity(new Intent(OrderProductActivity.this, HeroActivity.class));
                   }
               }).show();

            } else {
                String errorMessage = response.optString("message", "Failed to place order");
                onPostError(errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onPostError("An error occurred while processing the order.");
        }

    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Order Error", errorMessage, "Ok").show();
    }
}