package com.example.dropshipping.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.ProductAdapter;
import com.example.dropshipping.adapter.StoreAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.ProductItem;
import com.example.dropshipping.model.StoreItem;
import com.example.dropshipping.util.Messenger;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONArray;

public class ShopFragment extends Fragment {


    private RecyclerView rvShopByStore;
    private RecyclerView rvPopularProducts;

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

        loadShopByStore();
        loadPopularProducts();

        return view;
    }

    private void loadShopByStore() {
        List<StoreItem> shopByStores = new ArrayList<>();
        shopByStores.add(new StoreItem());
        shopByStores.add(new StoreItem());
        shopByStores.add(new StoreItem());
        shopByStores.add(new StoreItem());

        StoreAdapter storeAdapter = new StoreAdapter(getContext(), shopByStores);

        rvShopByStore.setAdapter(storeAdapter);
        rvShopByStore.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvShopByStore.setLayoutManager(layoutManager);

    }

    private void loadPopularProducts() {
        try {
            new PostTask(getContext(), new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        // Parse JSON
                        Gson gson = new Gson();
                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONArray productArray = response.getJSONArray("data");

                            Type listType = new TypeToken<List<ProductItem>>() {}.getType();
                            List<ProductItem> popularProducts = gson.fromJson(productArray.toString(), listType);

                            // Set adapter
                            ProductAdapter productAdapter = new ProductAdapter(getContext(), popularProducts);
                            rvPopularProducts.setAdapter(productAdapter);
                            rvPopularProducts.setHasFixedSize(true);
                            rvPopularProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
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

}