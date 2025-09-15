package com.example.dropshipping.view;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.ProductAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.ProductItem;
import com.example.dropshipping.model.Store;
import com.example.dropshipping.model.StoreItem;
import com.example.dropshipping.util.Messenger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private StoreItem store;
    private List<ProductItem> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private RecyclerView rvProducts;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store);

        context = this;

        long storeId = getIntent().getIntExtra("store_id", -1);

        initViews();
        setupToolbar();
        loadStoreData(storeId);
        loadProducts(storeId);
    }

    private void loadProducts(long storeId) {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("store_id", storeId);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        Gson gson = new Gson();
                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONArray productArray = response.getJSONArray("data");

                            Type listType = new TypeToken<List<ProductItem>>() {}.getType();
                            List<ProductItem> storeProducts = gson.fromJson(productArray.toString(), listType);

                            // Update the product list and notify adapter
                            productList.clear();
                            productList.addAll(storeProducts);
                            productAdapter.notifyDataSetChanged();


                        } else {
                            Messenger.showAlertDialog(context, "No Products", "No products found in this store.", "OK").show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(context, "Parse Error", "Failed to parse product data.", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(context, "Error", errorMessage, "Ok").show();
                }
            }, "error", "items/retrieve-product.php").execute(requestData);
        } catch (Exception e) {
            e.printStackTrace();
            Messenger.showAlertDialog(context, "Error", "Failed to load products: " + e.getMessage(), "OK").show();
        }
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, productList);
        rvProducts.setAdapter(productAdapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadStoreData(long storeId) {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("store_id", storeId);

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        Gson gson = new Gson();
                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONObject storeData = response.getJSONObject("data");
                            store = gson.fromJson(storeData.toString(), StoreItem.class);
                            bindStoreData();
                        } else {
                            // Fallback to dummy data if no store data found
                            createDummyStoreData();
                            bindStoreData();
                            Messenger.showAlertDialog(context, "Info", "Using demo store data.", "OK").show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Fallback to dummy data on parse error
                        createDummyStoreData();
                        bindStoreData();
                        Messenger.showAlertDialog(context, "Parse Error", "Using demo data due to parse error.", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    // Fallback to dummy data on error
                    createDummyStoreData();
                    bindStoreData();
                    Messenger.showAlertDialog(context, "Error", "Using demo data: " + errorMessage, "OK").show();
                }
            }, "error", "stores/retrieve-store.php").execute(requestData);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to dummy data on exception
            createDummyStoreData();
            bindStoreData();
            Messenger.showAlertDialog(context, "Error", "Using demo data due to error.", "OK").show();
        }
    }

    private void createDummyStoreData() {
        store = new StoreItem();
        store.setStoreName("LuzViMin Dropshipping");
        store.setStoreDescription("Your trusted dropshipping partner across Luzon, Visayas, and Mindanao. We provide quality products with fast delivery.");
        store.setStoreAddress("Manila, Philippines");
        store.setStorePhone("+63 912 345 6789");
        store.setStoreEmail("contact@luzvimindrop.com");
        store.setStatus("active");
    }

    private void bindStoreData() {
        TextView tvStoreName = findViewById(R.id.tvStoreName);
        TextView tvStoreDescription = findViewById(R.id.tvStoreDescription);
        TextView tvStoreAddress = findViewById(R.id.tvStoreAddress);
        TextView tvStorePhone = findViewById(R.id.tvStorePhone);
        TextView tvStoreEmail = findViewById(R.id.tvStoreEmail);
        TextView tvStoreStatus = findViewById(R.id.tvStoreStatus);

        if (store != null) {
            tvStoreName.setText(store.getStoreName());
            tvStoreDescription.setText(store.getStoreDescription());
            tvStoreAddress.setText(store.getStoreAddress());
            tvStorePhone.setText(store.getStorePhone());
            tvStoreEmail.setText(store.getStoreEmail());

            if (tvStoreStatus != null) {
                tvStoreStatus.setText(store.getStatus());

            }

            // Set toolbar title to store name
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(store.getStoreName());
            }
        }
    }

}