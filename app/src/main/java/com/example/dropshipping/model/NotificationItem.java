package com.example.dropshipping.model;

import com.google.gson.annotations.SerializedName;

public class NotificationItem {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("message")
    private String message;

    @SerializedName("is_read")
    private int isRead; // 0 = unread, 1 = read

    @SerializedName("created_at")
    private String createdAt;

    // Constructors
    public NotificationItem() {}

    public NotificationItem(int id, String userId, String message, int isRead, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead == 1;
    }

    public void markAsRead() {
        this.isRead = 1;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods to extract title from message
    public String getTitle() {
        // Extract a title from the message
        if (message != null) {
            if (message.toLowerCase().contains("order")) {
                if (message.toLowerCase().contains("processed")) {
                    return "Order Processing";
                } else if (message.toLowerCase().contains("shipped")) {
                    return "Order Shipped";
                } else if (message.toLowerCase().contains("delivered")) {
                    return "Order Delivered";
                } else if (message.toLowerCase().contains("cancelled")) {
                    return "Order Cancelled";
                } else {
                    return "Order Update";
                }
            } else if (message.toLowerCase().contains("payment")) {
                return "Payment Update";
            } else if (message.toLowerCase().contains("account")) {
                return "Account Notice";
            } else if (message.toLowerCase().contains("promotion") || message.toLowerCase().contains("sale")) {
                return "Special Offer";
            } else {
                return "Notification";
            }
        }
        return "Notification";
    }

    // Get notification type based on message content
    public NotificationType getType() {
        if (message != null) {
            String lowerMessage = message.toLowerCase();
            if (lowerMessage.contains("order")) {
                return NotificationType.ORDER;
            } else if (lowerMessage.contains("payment")) {
                return NotificationType.PAYMENT;
            } else if (lowerMessage.contains("promotion") || lowerMessage.contains("sale") || lowerMessage.contains("offer")) {
                return NotificationType.PROMOTION;
            } else if (lowerMessage.contains("account") || lowerMessage.contains("profile")) {
                return NotificationType.ACCOUNT;
            } else {
                return NotificationType.GENERAL;
            }
        }
        return NotificationType.GENERAL;
    }

    public enum NotificationType {
        ORDER,
        PAYMENT,
        PROMOTION,
        ACCOUNT,
        GENERAL
    }
}