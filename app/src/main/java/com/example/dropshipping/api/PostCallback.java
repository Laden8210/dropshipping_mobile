package com.example.dropshipping.api;

public interface PostCallback {
    void onPostSuccess(String responseData);

    void onPostError(String errorMessage);
}
