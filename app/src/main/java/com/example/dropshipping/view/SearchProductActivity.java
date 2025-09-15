package com.example.dropshipping.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dropshipping.R;
import com.example.dropshipping.adapter.ProductAdapter;
import com.example.dropshipping.adapter.ProductSearchAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.ProductItem;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchProductActivity extends AppCompatActivity implements ProductSearchAdapter.OnProductClickListener {

    private TextInputEditText searchEditText;
    private RecyclerView productsRecyclerView;
    private LinearLayout emptyStateLayout;
    private ProductSearchAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        initializeViews();
        setupRecyclerView();
        setupSearch();
        loadProducts(); // Load your products here
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.searchEditText);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
    }

    private void setupRecyclerView() {
        productAdapter = new ProductSearchAdapter(this);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(productAdapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterProducts(String query) {
        productAdapter.filter(query);
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (productAdapter.getItemCount() == 0) {
            emptyStateLayout.setVisibility(View.VISIBLE);
            productsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
            productsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void loadProducts() {
        try {
            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {

                        Gson gson = new Gson();
                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONArray productArray = response.getJSONArray("data");

                            Type listType = new TypeToken<List<ProductItem>>() {}.getType();
                            List<ProductItem> popularProducts = gson.fromJson(productArray.toString(), listType);

                            productAdapter.setProducts(popularProducts);
                            updateEmptyState();
                        } else {
                            Messenger.showAlertDialog(SearchProductActivity.this, "No Products", "No products found.", "OK").show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(SearchProductActivity.this, "Parse Error", "Failed to parse product data.", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(SearchProductActivity.this, "Error", errorMessage, "Ok").show();
                }
            }, "error", "items/retrieve-product.php").execute(new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onViewDetailsClick(ProductItem product) {


        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("pid", product.getPid());
        intent.putExtra("storeId", product.getStoreId());
        startActivity(intent);

    }
}