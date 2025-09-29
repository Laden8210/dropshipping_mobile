package com.example.dropshipping.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.dropshipping.adapter.MessageAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.TicketMessage;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewTicketMessage extends AppCompatActivity  {

    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private EditText etMessage;
    private ImageButton btnSend;

    private ProgressBar progressBar;
    private TextView tvTicketSubject;
    private TextView tvTicketStatus;
    private TextView tvTicketPriority;
    private TextView tvEmptyState;

    private List<TicketMessage> messageList;
    private String ticketId;
    private String ticketSubject;
    private String ticketStatus;
    private String ticketPriority;

    // Auto-refresh components
    private Handler refreshHandler;
    private Runnable refreshRunnable;
    private static final long REFRESH_INTERVAL = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_ticket_message);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get ticket ID from intent
        ticketId = getIntent().getStringExtra("ticket_id");
        if (ticketId == null || ticketId.isEmpty()) {
            Toast.makeText(this, "Invalid ticket ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupAutoRefresh();
        loadMessages();
    }

    private void initViews() {
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        progressBar = findViewById(R.id.progressBar);
        tvTicketSubject = findViewById(R.id.tvTicketSubject);
        tvTicketStatus = findViewById(R.id.tvTicketStatus);
        tvTicketPriority = findViewById(R.id.tvTicketPriority);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        // Setup RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Setup send button
        btnSend.setOnClickListener(v -> sendMessage());

        // Enable/disable send button based on message content
        etMessage.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSend.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupAutoRefresh() {
        refreshHandler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                loadMessages(false); // Load silently without showing progress bar
                refreshHandler.postDelayed(this, REFRESH_INTERVAL);
            }
        };
    }

    private void loadMessages() {
        loadMessages(true); // Show progress bar on initial load
    }

    private void loadMessages(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(View.VISIBLE);
        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("ticket_id", ticketId);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    if (showProgress) {
                        progressBar.setVisibility(View.GONE);
                    }
                    try {
                        parseAndDisplayMessages(responseData, showProgress);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (showProgress) {
                            showError("Error parsing messages");
                        }
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    if (showProgress) {
                        progressBar.setVisibility(View.GONE);
                        showError(errorMessage);
                    }
                }
            }, "error", "support/get-messages.php").execute(requestBody);

        } catch (JSONException e) {
            e.printStackTrace();
            if (showProgress) {
                showError("Error creating request");
            }
        }
    }

    private void parseAndDisplayMessages(String responseData, boolean isInitialLoad) throws JSONException {
        JSONObject response = new JSONObject(responseData);
        JSONObject data = response.getJSONObject("data");

        // Parse ticket info
        JSONObject ticket = data.getJSONObject("ticket");
        ticketSubject = ticket.getString("subject");
        ticketStatus = ticket.getString("status");
        ticketPriority = ticket.getString("priority");

        updateTicketHeader();

        // Parse messages
        JSONArray messagesArray = data.getJSONArray("messages");
        List<TicketMessage> newMessages = new ArrayList<>();

        for (int i = 0; i < messagesArray.length(); i++) {
            JSONObject messageJson = messagesArray.getJSONObject(i);
            TicketMessage message = new TicketMessage(messageJson);
            newMessages.add(message);
        }

        // Update UI
        if (newMessages.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerViewMessages.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerViewMessages.setVisibility(View.VISIBLE);

            // Smart update - only add new messages
            int oldSize = messageList.size();
            int newSize = newMessages.size();

            if (newSize > oldSize) {
                // New messages arrived - add only the new ones
                for (int i = oldSize; i < newSize; i++) {
                    messageList.add(newMessages.get(i));
                    messageAdapter.notifyItemInserted(i);
                }

                // Scroll to bottom for new messages
                recyclerViewMessages.postDelayed(() -> {
                    recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                }, 100);
            } else if (isInitialLoad) {
                // Initial load - replace all messages
                messageList.clear();
                messageList.addAll(newMessages);
                messageAdapter.notifyDataSetChanged();

                // Scroll to bottom
                recyclerViewMessages.postDelayed(() -> {
                    recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                }, 100);
            }
            // If newSize == oldSize, don't update (no new messages)
        }
    }

    private void updateTicketHeader() {
        tvTicketSubject.setText(ticketSubject);
        tvTicketStatus.setText(ticketStatus.toUpperCase());
        tvTicketPriority.setText(ticketPriority.toUpperCase());

        // Set status color
        int statusColor = getStatusColor(ticketStatus);
        tvTicketStatus.setTextColor(statusColor);

        // Set priority color
        int priorityColor = getPriorityColor(ticketPriority);
        tvTicketPriority.setTextColor(priorityColor);
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable send button while sending
        btnSend.setEnabled(false);

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("ticket_id", ticketId);
            requestBody.put("message", messageText);
            requestBody.put("message_type", "text");
            requestBody.put("attachment_url", JSONObject.NULL);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    btnSend.setEnabled(true);
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            // Clear input immediately
                            etMessage.setText("");

                            // Refresh messages to get server response (will add new message)
                            loadMessages(false);
                        } else {
                            Toast.makeText(ViewTicketMessage.this,
                                    response.optString("message", "Failed to send message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ViewTicketMessage.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    btnSend.setEnabled(true);
                    Toast.makeText(ViewTicketMessage.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }, "error", "support/send-message.php").execute(requestBody);

        } catch (JSONException e) {
            e.printStackTrace();
            btnSend.setEnabled(true);
            Toast.makeText(this, "Error creating message", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText("Error: " + message);
    }

    private int getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "open":
                return getResources().getColor(android.R.color.holo_blue_dark);
            case "in_progress":
                return getResources().getColor(android.R.color.holo_orange_dark);
            case "resolved":
                return getResources().getColor(android.R.color.holo_green_dark);
            case "closed":
                return getResources().getColor(android.R.color.darker_gray);
            default:
                return getResources().getColor(android.R.color.black);
        }
    }

    private int getPriorityColor(String priority) {
        switch (priority.toLowerCase()) {
            case "high":
                return getResources().getColor(android.R.color.holo_red_dark);
            case "medium":
                return getResources().getColor(android.R.color.holo_orange_dark);
            case "low":
                return getResources().getColor(android.R.color.holo_green_dark);
            default:
                return getResources().getColor(android.R.color.black);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start auto-refresh when activity becomes visible
        refreshHandler.postDelayed(refreshRunnable, REFRESH_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop auto-refresh when activity is not visible to save resources
        refreshHandler.removeCallbacks(refreshRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up handler to prevent memory leaks
        if (refreshHandler != null) {
            refreshHandler.removeCallbacks(refreshRunnable);
        }
    }
}