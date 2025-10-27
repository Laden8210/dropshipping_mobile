package com.example.dropshipping.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.ProductAdapter;
import com.example.dropshipping.adapter.StoreAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.ProductItem;
import com.example.dropshipping.model.StoreItem;
import com.example.dropshipping.util.Messenger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShopFragment extends Fragment {

    private RecyclerView rvShopByStore;
    private RecyclerView rvPopularProducts;
    private Spinner categorySpinner;
    private List<ProductItem> allProducts = new ArrayList<>();
    private ProductAdapter productAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        rvShopByStore = view.findViewById(R.id.rvShopByStore);
        rvPopularProducts = view.findViewById(R.id.rvPopularProducts);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        loadShopByStore();
        loadPopularProducts();

        return view;
    }

    private void loadShopByStore() {
        try{
            new PostTask(getContext(), new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {

                        Gson gson = new Gson();
                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONArray storeArray = response.getJSONArray("data");

                            Type listType = new TypeToken<List<StoreItem>>() {}.getType();
                            List<StoreItem> shopByStores = gson.fromJson(storeArray.toString(), listType);

                            StoreAdapter storeAdapter = new StoreAdapter(getContext(), shopByStores);
                            rvShopByStore.setAdapter(storeAdapter);
                            rvShopByStore.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            rvShopByStore.setLayoutManager(layoutManager);
                        } else {
                            Messenger.showAlertDialog(getContext(), "No Stores", "No stores found.", "OK").show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(getContext(), "Parse Error", "Failed to parse store data.", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(getContext(), "Error", errorMessage, "Ok").show();
                }
            }, "error", "stores/get-stores.php").execute(new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPopularProducts() {
        try {
            new PostTask(getContext(), new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {

                        Gson gson = new Gson();
                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONArray productArray = response.getJSONArray("data");

                            Type listType = new TypeToken<List<ProductItem>>() {}.getType();
                            allProducts = gson.fromJson(productArray.toString(), listType);

                            // Initialize adapter with all products
                            productAdapter = new ProductAdapter(getContext(), allProducts);
                            rvPopularProducts.setAdapter(productAdapter);
                            rvPopularProducts.setHasFixedSize(true);
                            rvPopularProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

                            // Setup category filter
                            setupCategoryFilter();

                        } else {
                            Messenger.showAlertDialog(getContext(), "No Products", "No products found.", "OK").show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(getContext(), "Parse Error", "Failed to parse product data.", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(getContext(), "Error", errorMessage, "Ok").show();
                }
            }, "error", "items/retrieve-product.php").execute(new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCategoryFilter() {
        // Extract unique categories from products
        Set<String> categories = new HashSet<>();
        categories.add("All Categories"); // Default option

        for (ProductItem product : allProducts) {
            if (product.getCategoryName() != null && !product.getCategoryName().isEmpty()) {
                categories.add(product.getCategoryName());
            }
        }

        // Convert set to list and sort
        List<String> categoryList = new ArrayList<>(categories);
        java.util.Collections.sort(categoryList);

        // Setup spinner adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categoryList
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        // Set spinner selection listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterProductsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                filterProductsByCategory("All Categories");
            }
        });
    }

    private void filterProductsByCategory(String category) {
        List<ProductItem> filteredProducts;

        if (category.equals("All Categories")) {
            filteredProducts = new ArrayList<>(allProducts);
        } else {
            filteredProducts = new ArrayList<>();
            for (ProductItem product : allProducts) {
                if (product.getCategoryName() != null && product.getCategoryName().equals(category)) {
                    filteredProducts.add(product);
                }
            }
        }

        if (productAdapter != null) {
            productAdapter.updateList(filteredProducts);
        }
    }
}