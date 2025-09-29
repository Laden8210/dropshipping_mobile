package com.example.dropshipping.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ticket {
    private String ticketId;
    private int orderId;
    private String orderNumber;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private String category;
    private String assignedTo;
    private String storeName;
    private String orderAmount;
    private int messageCount;
    private String lastMessageAt;
    private String createdAt;
    private String updatedAt;
    private String resolvedAt;

    // Constructor
    public Ticket(JSONObject jsonObject) throws JSONException {
        this.ticketId = jsonObject.optString("ticket_id", "");
        this.orderId = jsonObject.optInt("order_id", 0);
        this.orderNumber = jsonObject.optString("order_number", "");
        this.subject = jsonObject.optString("subject", "");
        this.description = jsonObject.optString("description", "");
        this.priority = jsonObject.optString("priority", "");
        this.status = jsonObject.optString("status", "");
        this.category = jsonObject.optString("category", "");
        this.assignedTo = jsonObject.optString("assigned_to");
        this.storeName = jsonObject.optString("store_name", "");
        this.orderAmount = jsonObject.optString("order_amount", "");
        this.messageCount = jsonObject.optInt("message_count", 0);
        this.lastMessageAt = jsonObject.optString("last_message_at");
        this.createdAt = jsonObject.optString("created_at", "");
        this.updatedAt = jsonObject.optString("updated_at", "");
        this.resolvedAt = jsonObject.optString("resolved_at");
    }

    // Getters
    public String getTicketId() { return ticketId; }
    public int getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getCategory() { return category; }
    public String getAssignedTo() { return assignedTo; }
    public String getStoreName() { return storeName; }
    public String getOrderAmount() { return orderAmount; }
    public int getMessageCount() { return messageCount; }
    public String getLastMessageAt() { return lastMessageAt; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getResolvedAt() { return resolvedAt; }
}