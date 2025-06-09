package com.example.dropshipping.api;

public interface GetCallback {

    void onGetSuccess(String responseData);

    void onGetError(String errorMessage);
}
