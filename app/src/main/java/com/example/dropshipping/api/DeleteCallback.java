package com.example.dropshipping.api;

public interface DeleteCallback {
    void onDeleteSuccess(String response);
    void onDeleteFail(String error);
}
