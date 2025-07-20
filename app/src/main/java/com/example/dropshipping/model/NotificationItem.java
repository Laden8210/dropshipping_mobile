package com.example.dropshipping.model;

import java.util.Date;

public class NotificationItem {
    public enum Type {
        ORDER, PROMOTION, SYSTEM
    }

    private String id;
    private Type type;
    private String title;
    private String message;
    private Date timestamp;
    private boolean isRead;
    private String iconUrl; // Optional for custom icons

    public NotificationItem(String id, Type type, String title, String message, Date timestamp) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public Type getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
}