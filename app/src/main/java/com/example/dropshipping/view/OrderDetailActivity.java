package com.example.dropshipping.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.adapter.OrderItemAdapter;
import com.example.dropshipping.adapter.ReviewAdapter;
import com.example.dropshipping.adapter.StatusTimelineAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.OrderItem;
import com.example.dropshipping.model.Review;
import com.example.dropshipping.model.StatusItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderNumber, tvOrderDate, tvPaymentMethod, tvTrackingNumber;
    private TextView tvAddressLine, tvAddressCity, tvAddressBrgy;
    private TextView tvSubtotal, tvShippingFee, tvTax, tvTotalAmount;
    private Chip chipStatus;
    private RecyclerView rvOrderItems, rvStatusTimeline;
    private ExtendedFloatingActionButton fabTrack;
    private ReviewAdapter reviewAdapter;

    private long orderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Initialize views
        initViews();
        setupToolbar();

        // Get order ID from intent
        orderId = getIntent().getLongExtra("order_id", -1);
        if (orderId != -1) {
            fetchOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Invalid order ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvTrackingNumber = findViewById(R.id.tvTrackingNumber);
        tvAddressLine = findViewById(R.id.tvAddressLine);
        tvAddressCity = findViewById(R.id.tvAddressCity);
        tvAddressBrgy = findViewById(R.id.tvAddressBrgy);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTax = findViewById(R.id.tvTax);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        chipStatus = findViewById(R.id.chipStatus);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        rvStatusTimeline = findViewById(R.id.rvStatusTimeline);
        fabTrack = findViewById(R.id.fabTrack);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void fetchOrderDetails(long orderId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("order_id", orderId);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            JSONObject orderData = response.getJSONObject("data");
                            runOnUiThread(() -> {
                                try {
                                    populateOrderDetails(orderData);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            String errorMessage = response.optString("message", "Failed to load order details");
                            Toast.makeText(OrderDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(OrderDetailActivity.this, "Error parsing order details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Toast.makeText(OrderDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }, "error", "order/get-order-details.php").execute(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateOrderDetails(JSONObject orderData) throws JSONException {
        // Order info


        Log.d("OrderDetailActivity", "Populating order details");
        fabTrack.setOnClickListener(v -> {
            Log.d("OrderDetailActivity", "Track button clicked");
            Intent intent = new Intent(OrderDetailActivity.this, TrackingDetailActivity.class);

            try {
                if (orderData != null && orderData.has("tracking_number")) {
                    String trackingNumber = orderData.getString("tracking_number");
                    intent.putExtra("tracking_number", trackingNumber);
                } else {
                    Log.e("OrderDetailActivity", "Tracking number not available");
                }
            } catch (JSONException ex) {
                Log.e("OrderDetailActivity", "Error parsing tracking number", ex);
            }

            startActivity(intent);
        });

        tvOrderNumber.setText(orderData.getString("order_number"));
        chipStatus.setText(orderData.getString("status"));
        tvOrderDate.setText(formatOrderDate(orderData.getString("created_at")));
//        tvPaymentMethod.setText(getPaymentMethodDisplay(orderData.getString("payment_method")));
        tvTrackingNumber.setText(orderData.getString("tracking_number"));

        // Address
        JSONObject address = orderData.getJSONObject("shipping_address");
        tvAddressLine.setText(address.getString("address_line"));
        tvAddressCity.setText(address.getString("city") + ", " + address.getString("region"));
        tvAddressBrgy.setText(address.getString("brgy") + ", " + address.getString("postal_code"));

        // Prices
        tvSubtotal.setText(String.format("₱%,.2f", orderData.getDouble("subtotal")));
        tvShippingFee.setText(String.format("₱%,.2f", orderData.getDouble("shipping_fee")));
        tvTax.setText(String.format("₱%,.2f", orderData.getDouble("tax")));
        tvTotalAmount.setText(String.format("₱%,.2f", orderData.getDouble("total_amount")));

        // Order items
        JSONArray itemsArray = orderData.getJSONArray("items");
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject item = itemsArray.getJSONObject(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setName(item.getString("product_name"));
            orderItem.setPrice(item.getDouble("price"));
            orderItem.setQuantity(item.getInt("quantity"));
            orderItem.setImageUrl(item.getString("primary_image"));
            orderItem.setSku(item.getString("product_sku"));
            orderItem.setCategoryName(item.getString("category_name"));
            orderItem.setDescription(item.getString("description"));

            orderItems.add(orderItem);
        }

        OrderItemAdapter itemAdapter = new OrderItemAdapter(orderItems, OrderDetailActivity.this, item -> {
            showProductDetailBottomSheet(item);
        });
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(itemAdapter);

        // Status timeline
        JSONArray statusHistory = orderData.getJSONArray("status_history");
        List<StatusItem> statusItems = new ArrayList<>();
        for (int i = 0; i < statusHistory.length(); i++) {
            JSONObject status = statusHistory.getJSONObject(i);
            StatusItem statusItem = new StatusItem();
            statusItem.setStatus(status.getString("status"));
            statusItem.setTimestamp(status.getString("created_at"));
            statusItems.add(statusItem);
        }

        StatusTimelineAdapter timelineAdapter = new StatusTimelineAdapter(statusItems, OrderDetailActivity.this);
        rvStatusTimeline.setLayoutManager(new LinearLayoutManager(this));
        rvStatusTimeline.setAdapter(timelineAdapter);


        // Update status chip color
        updateStatusChip(orderData.getString("status"));
    }

    private void showProductDetailBottomSheet(OrderItem item) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheet_product_detail, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Initialize views
        TextView tvProductName = bottomSheetView.findViewById(R.id.tvProductName);
        ImageView ivProductImage = bottomSheetView.findViewById(R.id.ivProductImage);
        TextView tvProductDescription = bottomSheetView.findViewById(R.id.tvProductDescription);
        TextView tvProductPrice = bottomSheetView.findViewById(R.id.tvProductPrice);
        TextView tvProductSku = bottomSheetView.findViewById(R.id.tvProductSku);
        TextView tvProductCategory = bottomSheetView.findViewById(R.id.tvProductCategory);
        RecyclerView rvReviews = bottomSheetView.findViewById(R.id.rvReviews);
        Button btnClose = bottomSheetView.findViewById(R.id.btnClose);

        // Set product data
        tvProductName.setText(item.getName());
        tvProductDescription.setText(item.getDescription());
        tvProductPrice.setText(String.format("₱%,.2f", item.getPrice()));
        tvProductSku.setText("SKU: " + item.getSku());
        tvProductCategory.setText("Category: " + item.getCategoryName());

        // Load product image
        Glide.with(this)
                .load(item.getImageUrl())
                .placeholder(R.drawable.product_sample)
                .into(ivProductImage);

        // Set up reviews
        List<Review> reviews = getProductReviews(item.getProductId());
        reviewAdapter = new ReviewAdapter(reviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);

        // Close button
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Initialize review submission elements
        Spinner spinnerRating = bottomSheetView.findViewById(R.id.spinnerRating);
        TextInputEditText etReview = bottomSheetView.findViewById(R.id.etReview);
        MaterialButton btnSubmitReview = bottomSheetView.findViewById(R.id.btnSubmitReview);

        // Setup rating spinner
        setupRatingSpinner(spinnerRating);

        bottomSheetDialog.show();

        // Submit review button click
        btnSubmitReview.setOnClickListener(v -> {
            int selectedPosition = spinnerRating.getSelectedItemPosition();
            float rating = selectedPosition + 1; // Convert to 1-5 rating
            String reviewText = etReview.getText().toString().trim();

            if (reviewText.isEmpty()) {
                Toast.makeText(this, "Please write your review", Toast.LENGTH_SHORT).show();
                return;
            }

            submitReview(item.getProductId(), rating, reviewText, new ReviewSubmitCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(OrderDetailActivity.this, "Review submitted!", Toast.LENGTH_SHORT).show();
                    // Clear form
                    spinnerRating.setSelection(4); // Default to 5 stars
                    etReview.setText("");

                    loadReviews(item.getProductId());
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(OrderDetailActivity.this, "Failed to submit review: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupRatingSpinner(Spinner spinner) {
        // Create rating options with stars
        String[] ratingOptions = {
                "⭐ 1 Star",
                "⭐⭐ 2 Stars",
                "⭐⭐⭐ 3 Stars",
                "⭐⭐⭐⭐ 4 Stars",
                "⭐⭐⭐⭐⭐ 5 Stars"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ratingOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Default to 5 stars
        spinner.setSelection(4);
    }

    private void submitReview(long productId, float rating, String review, ReviewSubmitCallback callback) {
        try {
            JSONObject reviewJson = new JSONObject();
            reviewJson.put("product_id", productId);
            reviewJson.put("rating", rating);
            reviewJson.put("review", review);
            reviewJson.put("order_id", orderId);
            // Add user ID if needed

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            callback.onSuccess();
                        } else {
                            String errorMessage = response.optString("message", "Review submission failed");
                            callback.onError(errorMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Error parsing response");
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            }, "error", "feedback/submit.php").execute(reviewJson);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError("Error creating review request");
        }
    }

    interface ReviewSubmitCallback {
        void onSuccess();
        void onError(String message);
    }

    private void loadReviews(long productId) {
        reviewAdapter.setReviews(getProductReviews(productId));

    }

    private List<Review> getProductReviews(long productId) {


        List<Review> reviews = new ArrayList<>();

        Review review1 = new Review();
        review1.setReviewerName("John Smith");
        review1.setRating(4.5f);
        review1.setReviewDate("2025-07-15");
        review1.setReviewText("Great product! Works as described.");
        reviews.add(review1);

        Review review2 = new Review();
        review2.setReviewerName("Sarah Johnson");
        review2.setRating(5f);
        review2.setReviewDate("2025-07-10");
        review2.setReviewText("Absolutely love this product. Highly recommend!");
        reviews.add(review2);

        return reviews;
    }

    private String formatOrderDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            return rawDate;
        }
    }

    private String getPaymentMethodDisplay(String method) {
        switch (method) {
            case "credit_card": return "Credit Card";
            case "paypal": return "PayPal";
            case "cod": return "Cash on Delivery";
            default: return method;
        }
    }

    private void updateStatusChip(String status) {
        int bgColor = Color.parseColor("#40FFFFFF"); // Default
        int strokeColor = Color.WHITE;

        switch (status.toLowerCase()) {
            case "shipped":
                bgColor = Color.parseColor("#401976D2");
                strokeColor = Color.parseColor("#1976D2");
                break;
            case "delivered":
                bgColor = Color.parseColor("#404CAF50");
                strokeColor = Color.parseColor("#4CAF50");
                break;
            case "cancelled":
                bgColor = Color.parseColor("#40F44336");
                strokeColor = Color.parseColor("#F44336");
                break;
                case "processing":
                bgColor = Color.parseColor("#40FF9800");
                strokeColor = Color.parseColor("#FF9800");
                break;
            case "pending":
                bgColor = Color.parseColor("#40FFEB3B");
                strokeColor = Color.parseColor("#FFEB3B");
                break;
        }

        chipStatus.setChipBackgroundColor(ColorStateList.valueOf(bgColor));
        chipStatus.setChipStrokeColor(ColorStateList.valueOf(strokeColor));
    }

    // Model classes






}