package com.example.dropshipping.model;

public class Message {
    public static final int TYPE_USER = 0;
    public static final int TYPE_SUPPORT = 1;
    public static final int TYPE_SYSTEM = 2;

    private final int type;
    private final String text;
    private final String timestamp;
    private String senderName;

    public Message(int type, String text, String timestamp) {
        this.type = type;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Message(int type, String text, String senderName, String timestamp) {
        this.type = type;
        this.text = text;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public boolean isUserMessage() {
        return type == TYPE_USER;
    }

    public boolean isSupportMessage() {
        return type == TYPE_SUPPORT;
    }

    public boolean isSystemMessage() {
        return type == TYPE_SYSTEM;
    }

    // Getters
    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSenderName() {
        return senderName;
    }
}