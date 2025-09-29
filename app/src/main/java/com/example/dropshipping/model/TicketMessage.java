package com.example.dropshipping.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TicketMessage {
    private String messageId;
    private String senderId;
    private String senderType;
    private String message;
    private String messageType;
    private String attachmentUrl;
    private boolean isRead;
    private String createdAt;

    // Constructor from JSON
    public TicketMessage(JSONObject jsonObject) throws JSONException {
        this.messageId = jsonObject.optString("message_id", "");
        this.senderId = jsonObject.optString("sender_id", "");
        this.senderType = jsonObject.optString("sender_type", "");
        this.message = jsonObject.optString("message", "");
        this.messageType = jsonObject.optString("message_type", "text");
        this.attachmentUrl = jsonObject.optString("attachment_url");
        this.isRead = jsonObject.optBoolean("is_read", false);
        this.createdAt = jsonObject.optString("created_at", "");
    }

    // Constructor for new messages
    public TicketMessage(String message, String messageType, String attachmentUrl, String senderType) {
        this.message = message;
        this.messageType = messageType;
        this.attachmentUrl = attachmentUrl;
        this.senderType = senderType;
        this.isRead = true; // Mark own messages as read
        this.createdAt = getCurrentTimestamp();
    }

    private String getCurrentTimestamp() {
        // You can format this as needed
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getSenderType() { return senderType; }
    public String getMessage() { return message; }
    public String getMessageType() { return messageType; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public boolean isRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }

    // Helper methods
    public boolean isSystemMessage() {
        return "system".equals(senderType);
    }

    public boolean isUserMessage() {
        return "user".equals(senderType) || "customer".equals(senderType);
    }

    public boolean isSupportMessage() {
        return "support".equals(senderType) || "admin".equals(senderType) || "agent".equals(senderType);
    }

    public boolean hasAttachment() {
        return attachmentUrl != null && !attachmentUrl.isEmpty() && !attachmentUrl.equals("null");
    }
}

// Ticket Info Model
class TicketInfo {
    private String ticketId;
    private String subject;
    private String status;
    private String priority;
    private String category;
    private String createdAt;

    public TicketInfo(JSONObject jsonObject) throws JSONException {
        this.ticketId = jsonObject.optString("ticket_id", "");
        this.subject = jsonObject.optString("subject", "");
        this.status = jsonObject.optString("status", "");
        this.priority = jsonObject.optString("priority", "");
        this.category = jsonObject.optString("category", "");
        this.createdAt = jsonObject.optString("created_at", "");
    }

    // Getters
    public String getTicketId() { return ticketId; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public String getCategory() { return category; }
    public String getCreatedAt() { return createdAt; }
}