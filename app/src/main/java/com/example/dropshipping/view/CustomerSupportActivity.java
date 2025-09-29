package com.example.dropshipping.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
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
import com.example.dropshipping.adapter.TicketAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.Ticket;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerSupportActivity extends AppCompatActivity implements TicketAdapter.OnTicketClickListener {

    private RecyclerView recyclerViewTickets;
    private TicketAdapter ticketAdapter;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private TextView tvTotalTickets;
    private TextView tvOpenTickets;
    private TextView tvInProgressTickets;
    private TextView tvResolvedTickets;
    private TextView tvClosedTickets;
    private FloatingActionButton fabCreateTicket;
    private List<Ticket> ticketList;
    private List<Map<String, String>> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_support);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadOrders();
        loadTickets();
    }

    private void initViews() {
        recyclerViewTickets = findViewById(R.id.recyclerViewTickets);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvTotalTickets = findViewById(R.id.tvTotalTickets);
        tvOpenTickets = findViewById(R.id.tvOpenTickets);
        tvInProgressTickets = findViewById(R.id.tvInProgressTickets);
        tvResolvedTickets = findViewById(R.id.tvResolvedTickets);
        tvClosedTickets = findViewById(R.id.tvClosedTickets);
        fabCreateTicket = findViewById(R.id.fabCreateTicket);

        // Setup RecyclerView
        ticketList = new ArrayList<>();
        orderList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(ticketList);
        ticketAdapter.setOnTicketClickListener(this);

        recyclerViewTickets.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTickets.setAdapter(ticketAdapter);

        // Setup FAB
        if (fabCreateTicket != null) {
            fabCreateTicket.setOnClickListener(v -> showCreateTicketDialog());
        }
    }

    private void loadOrders() {
        // Load user orders for the spinner
        new PostTask(this, new PostCallback() {
            @Override
            public void onPostSuccess(String responseData) {
                try {
                    JSONObject response = new JSONObject(responseData);
                    if (response.getString("status").equals("success")) {
                        JSONArray ordersArray = response.getJSONArray("data");
                        orderList.clear();

                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject order = ordersArray.getJSONObject(i);
                            Map<String, String> orderMap = new HashMap<>();
                            orderMap.put("id", order.getString("order_id"));
                            orderMap.put("display", "Order #" + order.getString("order_number") + " - " +
                                    order.optString("product_name", "Order"));
                            orderList.add(orderMap);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPostError(String errorMessage) {
                // Silent fail - user can still create ticket without order
            }
        }, "error", "order/get-all-order.php").execute(new JSONObject());
    }

    private void loadTickets() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewTickets.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        new PostTask(this, new PostCallback() {
            @Override
            public void onPostSuccess(String responseData) {
                progressBar.setVisibility(View.GONE);
                try {
                    parseAndDisplayTickets(responseData);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showError("Error parsing ticket data");
                }
            }

            @Override
            public void onPostError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                showError(errorMessage);
            }
        }, "error", "support/get-tickets.php").execute(new JSONObject());
    }

    private void parseAndDisplayTickets(String responseData) throws JSONException {
        JSONObject response = new JSONObject(responseData);
        JSONObject data = response.getJSONObject("data");

        // Parse tickets
        JSONArray ticketsArray = data.getJSONArray("tickets");
        ticketList.clear();

        for (int i = 0; i < ticketsArray.length(); i++) {
            JSONObject ticketJson = ticketsArray.getJSONObject(i);
            Ticket ticket = new Ticket(ticketJson);
            ticketList.add(ticket);
        }

        // Parse summary
        JSONObject summary = data.getJSONObject("summary");
        updateSummaryViews(summary);

        // Update UI
        if (ticketList.isEmpty()) {
            recyclerViewTickets.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTickets.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
            ticketAdapter.updateTickets(ticketList);
        }
    }

    private void updateSummaryViews(JSONObject summary) throws JSONException {
        if (tvTotalTickets != null) {
            tvTotalTickets.setText("Total: " + summary.getInt("total_tickets"));
        }
        if (tvOpenTickets != null) {
            tvOpenTickets.setText("Open: " + summary.getInt("open_tickets"));
        }
        if (tvInProgressTickets != null) {
            tvInProgressTickets.setText("In Progress: " + summary.getInt("in_progress_tickets"));
        }
        if (tvResolvedTickets != null) {
            tvResolvedTickets.setText("Resolved: " + summary.getInt("resolved_tickets"));
        }
        if (tvClosedTickets != null) {
            tvClosedTickets.setText("Closed: " + summary.getInt("closed_tickets"));
        }
    }

    private void showCreateTicketDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_ticket);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Initialize views
        AutoCompleteTextView spinnerOrder = dialog.findViewById(R.id.spinnerOrder);
        AutoCompleteTextView spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        AutoCompleteTextView spinnerPriority = dialog.findViewById(R.id.spinnerPriority);
        TextInputEditText etSubject = dialog.findViewById(R.id.etSubject);
        TextInputEditText etDescription = dialog.findViewById(R.id.etDescription);
        MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
        MaterialButton btnCreate = dialog.findViewById(R.id.btnCreate);

        // Setup Order Spinner
        List<String> orderDisplayList = new ArrayList<>();
        orderDisplayList.add("No specific order");
        for (Map<String, String> order : orderList) {
            orderDisplayList.add(order.get("display"));
        }
        ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, orderDisplayList);
        spinnerOrder.setAdapter(orderAdapter);
        spinnerOrder.setText(orderDisplayList.get(0), false);

        // Setup Category Spinner
        String[] categories = {"Order Issue", "Payment Issue", "Shipping Issue",
                "Product Quality", "Return/Refund", "Account Issue", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setText(categories[0], false);

        // Setup Priority Spinner
        String[] priorities = {"Low", "Medium", "High"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, priorities);
        spinnerPriority.setAdapter(priorityAdapter);
        spinnerPriority.setText(priorities[1], false);

        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Create button
        btnCreate.setOnClickListener(v -> {
            String subject = etSubject.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String category = spinnerCategory.getText().toString().toLowerCase().replace("/", "_")
                    .replace(" ", "_");
            String priority = spinnerPriority.getText().toString().toLowerCase();

            // Get order ID
            String selectedOrder = spinnerOrder.getText().toString();
            String orderId = "";
            if (!selectedOrder.equals("No specific order")) {
                int orderIndex = orderDisplayList.indexOf(selectedOrder) - 1;
                if (orderIndex >= 0 && orderIndex < orderList.size()) {
                    orderId = orderList.get(orderIndex).get("id");
                }
            }

            // Validation
            if (subject.isEmpty()) {
                Toast.makeText(this, "Please enter a subject", Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create ticket
            createTicket(orderId, subject, description, priority, category, dialog);
        });

        dialog.show();
    }

    private void createTicket(String orderId, String subject, String description,
                              String priority, String category, Dialog dialog) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("order_id", orderId);
            requestBody.put("subject", subject);
            requestBody.put("description", description);
            requestBody.put("priority", priority);
            requestBody.put("category", category);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(CustomerSupportActivity.this,
                                    "Ticket created successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadTickets(); // Refresh ticket list
                        } else {
                            Toast.makeText(CustomerSupportActivity.this,
                                    response.optString("message", "Failed to create ticket"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CustomerSupportActivity.this,
                                "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Toast.makeText(CustomerSupportActivity.this,
                            errorMessage, Toast.LENGTH_SHORT).show();
                }
            }, "error", "support/create-ticket.php").execute(requestBody);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating ticket", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText("Error loading tickets: " + message);
    }

    @Override
    public void onTicketClick(Ticket ticket) {
        Intent intent = new Intent(this, ViewTicketMessage.class);
        intent.putExtra("ticket_id", ticket.getTicketId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh tickets when returning to activity
        loadTickets();
    }
}