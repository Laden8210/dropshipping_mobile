package com.example.dropshipping.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dropshipping.R;
import com.example.dropshipping.adapter.MessageAdapter;
import com.example.dropshipping.model.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;
    private final List<Message> messages = new ArrayList<>();
    private EditText etMessage;
    private FloatingActionButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        // Setup RecyclerView
        messageAdapter = new MessageAdapter(messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(messageAdapter);

        // Add sample messages
        addSampleMessages();

        // Scroll to bottom
        scrollToBottom();

        // Send button listener
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void addSampleMessages() {
        messages.add(new Message(Message.TYPE_SYSTEM, "Ticket started • Today", "12:00 PM"));
        messages.add(new Message(Message.TYPE_SUPPORT, "Hello! How can I assist you today?", "Support Team", "12:01 PM"));
        messages.add(new Message(Message.TYPE_USER, "My order #12345 hasn't arrived yet", "12:02 PM"));
        messages.add(new Message(Message.TYPE_SUPPORT, "I'll check the status for you. Can you please confirm your order number?", "Support Agent", "12:03 PM"));
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Get current time
            Calendar calendar = Calendar.getInstance();
            String time = String.format(Locale.getDefault(), "%02d:%02d %s",
                    calendar.get(Calendar.HOUR) == 0 ? 12 : calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");

            // Create and add message
            Message message = new Message(Message.TYPE_USER, messageText, time);
            messageAdapter.addMessage(message);

            // Clear input
            etMessage.setText("");

            // Scroll to bottom
            scrollToBottom();

            // Simulate support reply after delay
            simulateSupportReply(messageText);
        }
    }

    private void simulateSupportReply(String userMessage) {
        // In a real app, this would be replaced with actual network call
        rvMessages.postDelayed(() -> {
            String reply = "Thanks for your message. We'll look into this.";

            // Get current time
            Calendar calendar = Calendar.getInstance();
            String time = String.format(Locale.getDefault(), "%02d:%02d %s",
                    calendar.get(Calendar.HOUR) == 0 ? 12 : calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");

            Message supportMessage = new Message(Message.TYPE_SUPPORT, reply, "Support Agent", time);
            messageAdapter.addMessage(supportMessage);
            scrollToBottom();
        }, 2000);
    }

    private void scrollToBottom() {
        rvMessages.post(() -> {
            // Scroll to bottom of the list
            if (messageAdapter.getItemCount() > 0) {
                rvMessages.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }
}