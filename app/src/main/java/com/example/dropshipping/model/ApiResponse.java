package com.example.dropshipping.model;

import java.util.List;

public class ApiResponse {
    private String status;
    private List<StoreCart> data;

    public String getStatus() { return status; }
    public List<StoreCart> getData() { return data; }
}