package com.example.dropshipping.view;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.ProductTrackerAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrackingDetailActivity extends AppCompatActivity implements PostCallback {


    private TextView tvOrderNumber, tvOrderDate, tvTotalAmount, tvTrackingNumber;
    private TextView tvCustomerName, tvCustomerEmail, tvCustomerPhone;
    private TextView tvFullAddress, tvStoreName, tvStoreContact;
    private RecyclerView productsRecyclerView;
    private LinearLayout timelineContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_detail);

        // Initialize UI components
        initViews();

        // Get tracking number from intent
        String trackingNumber = getIntent().getStringExtra("tracking_number");
        if (trackingNumber == null || trackingNumber.isEmpty()) {
            showErrorDialog("Invalid tracking number");
            return;
        }

        try {
            JSONObject payload = new JSONObject();
            payload.put("tracking_number", trackingNumber);
            new PostTask(this, this, "Loading order details...", "order/track-order.php").execute(payload);
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorDialog("Error creating request");
        }
    }

    private void initViews() {
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTrackingNumber = findViewById(R.id.tvTrackingNumber);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvFullAddress = findViewById(R.id.tvFullAddress);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreContact = findViewById(R.id.tvStoreContact);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        timelineContainer = findViewById(R.id.timelineContainer);

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onPostSuccess(String responseData) {
        runOnUiThread(() -> {
            try {
                JSONObject response = new JSONObject(responseData);
                if (response.getString("status").equalsIgnoreCase("success")) {
                    JSONObject data = response.getJSONObject("data");
                    updateUI(data);
                } else {
                    String errorMsg = response.optString("message", "Unknown error occurred");
                    onPostError(errorMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onPostError("Failed to parse response");
            }
        });
    }

    private void updateUI(JSONObject data) throws JSONException {
        // Order header
        tvOrderNumber.setText(data.getString("order_number"));
        tvTrackingNumber.setText("Tracking: " + data.getString("tracking_number"));
        tvTotalAmount.setText("Total: ₱" + data.getString("total_amount"));

        // Format order date
        String rawDate = data.getString("order_date");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(rawDate);
            String formattedDate = "Order Date: " + DateUtils.formatDateTime(this,
                    date.getTime(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
            tvOrderDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            tvOrderDate.setText("Order Date: " + rawDate);
        }

        // Customer info
        JSONObject customer = data.getJSONObject("customer");
        tvCustomerName.setText(String.format("%s %s",
                customer.getString("first_name"),
                customer.getString("last_name")));
        tvCustomerEmail.setText(customer.getString("email"));
        tvCustomerPhone.setText(customer.getString("phone"));

        // Shipping address
        JSONObject address = data.getJSONObject("shipping_address");
        String fullAddress = String.format("%s\n%s, %s\n%s %s",
                address.getString("address_line"),
                address.getString("barangay"),
                address.getString("city"),
                address.getString("region"),
                address.getString("postal_code"));
        tvFullAddress.setText(fullAddress);

        // Store info
        JSONObject store = data.getJSONObject("store");
        tvStoreName.setText(store.getString("store_name"));
        tvStoreContact.setText("Contact: " + store.getString("store_contact"));

        // Products
        ProductTrackerAdapter adapter = new ProductTrackerAdapter(data.getJSONArray("products"));
        productsRecyclerView.setAdapter(adapter);

        // Timeline
        setupTimeline(data.getJSONArray("shipping_statuses"));
    }

    private void setupTimeline(JSONArray statuses) {
        timelineContainer.removeAllViews(); // Clear previous views

        try {
            for (int i = 0; i < statuses.length(); i++) {
                JSONObject status = statuses.getJSONObject(i);
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_timeline, timelineContainer, false);

                // Handle timeline connectors
                View topLine = itemView.findViewById(R.id.lineTop);
                View bottomLine = itemView.findViewById(R.id.lineBottom);

                if (i == 0) topLine.setVisibility(View.INVISIBLE);
                if (i == statuses.length() - 1) bottomLine.setVisibility(View.INVISIBLE);

                // Format timestamp
                TextView tvTime = itemView.findViewById(R.id.tvStatusTime);
                TextView tvRemarks = itemView.findViewById(R.id.tvStatusRemarks);

                String rawTime = status.getString("update_time");
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date = sdf.parse(rawTime);
                    String timeText = DateUtils.formatDateTime(this,
                            date.getTime(),
                            DateUtils.FORMAT_SHOW_TIME);
                    tvTime.setText(timeText);
                } catch (ParseException e) {
                    e.printStackTrace();
                    tvTime.setText(rawTime);
                }

                tvRemarks.setText(status.getString("remarks"));
                timelineContainer.addView(itemView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostError(String errorMessage) {
        runOnUiThread(() -> showErrorDialog(errorMessage));
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}