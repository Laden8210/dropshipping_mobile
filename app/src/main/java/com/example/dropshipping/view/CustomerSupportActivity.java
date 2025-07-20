package com.example.dropshipping.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dropshipping.R;
import com.google.android.material.button.MaterialButton;

public class CustomerSupportActivity extends AppCompatActivity {

    private MaterialButton btnSubmitTicket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        btnSubmitTicket = findViewById(R.id.btnSubmitTicket);
        btnSubmitTicket.setOnClickListener(v -> {
            startActivity(new Intent(CustomerSupportActivity.this, MessageActivity.class));
        });

    }
}