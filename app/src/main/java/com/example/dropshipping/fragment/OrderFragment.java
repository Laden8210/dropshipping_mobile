package com.example.dropshipping.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.OrderAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.Order;
import com.example.dropshipping.model.OrderItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView ordersRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Initialize views
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);


        // Setup toolbar

        // Setup RecyclerView
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(orderList, getContext());
        ordersRecyclerView.setAdapter(orderAdapter);

        // Setup swipe refresh
        swipeRefreshLayout.setOnRefreshListener(this);

        // Filter button

        // Load orders
        loadOrders();

        return view;
    }

    @Override
    public void onRefresh() {
        loadOrders();
    }

    private void loadOrders() {
        swipeRefreshLayout.setRefreshing(true);

        try {
            JSONObject requestJson = new JSONObject();


            new PostTask( getContext(),new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    requireActivity().runOnUiThread(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject response = new JSONObject(responseData);
                            if (response.getString("status").equals("success")) {
                                parseOrders(response.getJSONArray("data"));
                            } else {
                                String errorMessage = response.optString("message", "Failed to load orders");
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing orders", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onPostError(String errorMessage) {
                    requireActivity().runOnUiThread(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            }, "error", "order/get-all-order.php").execute(requestJson);
        } catch (Exception e) {
            swipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        }
    }

    private void parseOrders(JSONArray ordersArray) throws JSONException {
        orderList.clear();
        for (int i = 0; i < ordersArray.length(); i++) {
            JSONObject orderObj = ordersArray.getJSONObject(i);
            Order order = new Order();

            order.setOrderId(orderObj.getLong("order_id"));
            order.setOrderNumber(orderObj.getString("order_number"));
            order.setTotalAmount(Double.parseDouble(orderObj.getString("total_amount")));

            order.setCreatedAt(orderObj.getString("created_at"));
            order.setStatus(orderObj.getString("status"));
            order.setTrackingNumber(orderObj.optString("tracking_number", ""));

            Log.d("OrderFragment", "Order ID: " + order.getOrderId() + ", Status: " + order.getStatus() + ", Total: " + order.getTotalAmount());

            // Parse items
            JSONArray itemsArray = orderObj.getJSONArray("items");
            List<OrderItem> items = new ArrayList<>();
            for (int j = 0; j < itemsArray.length(); j++) {
                JSONObject itemObj = itemsArray.getJSONObject(j);
                OrderItem item = new OrderItem();
                item.setProductId(Long.parseLong(itemObj.getString("product_id")));
                item.setQuantity(Integer.parseInt(itemObj.getString("quantity")));
                item.setPrice(Double.parseDouble(itemObj.getString("price")));
                item.setName(itemObj.getString("product_name"));

                items.add(item);
            }
            order.setItems(items);

            orderList.add(order);
        }
        orderAdapter.notifyDataSetChanged();
    }



    private void filterOrders(String status) {
        if (status.equals("All")) {
            orderAdapter.setOrders(orderList);
        } else {
            List<Order> filtered = new ArrayList<>();
            for (Order order : orderList) {
                if (order.getStatus().equalsIgnoreCase(status)) {
                    filtered.add(order);
                }
            }
            orderAdapter.setOrders(filtered);
        }
    }

}