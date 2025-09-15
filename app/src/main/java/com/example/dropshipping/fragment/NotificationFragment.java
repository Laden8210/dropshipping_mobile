package com.example.dropshipping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.NotificationAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.NotificationItem;
import com.example.dropshipping.util.Messenger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NotificationFragment extends Fragment implements
        NotificationAdapter.OnNotificationClickListener {

    private NotificationAdapter adapter;
    private final List<NotificationItem> notifications = new ArrayList<>();
    private final Random random = new Random();

    // UI components
    private RecyclerView rvNotifications;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnClearAll, btnRefresh;
    private LinearLayout emptyStateView;

    public NotificationFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout manually
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI references
        rvNotifications = view.findViewById(R.id.rvNotifications);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        btnClearAll = view.findViewById(R.id.btnClearAll);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        emptyStateView = view.findViewById(R.id.emptyStateView);

        setupRecyclerView();
        setupSwipeRefresh();
        setupButtons();

        loadNotifications();
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(this);
        rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvNotifications.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadNotifications();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupButtons() {
        btnClearAll.setOnClickListener(v -> {
            if (!notifications.isEmpty()) {
                clearAllNotifications();
                Toast.makeText(requireContext(), "All notifications cleared", Toast.LENGTH_SHORT).show();
            }
        });

        btnRefresh.setOnClickListener(v -> loadNotifications());
    }

    private void loadNotifications() {
        swipeRefreshLayout.setRefreshing(true);

        try {
            new PostTask(getContext(), new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        notifications.clear();

                        JSONObject response = new JSONObject(responseData);

                        if (response.has("data")) {
                            JSONArray notificationArray = response.getJSONArray("data");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<NotificationItem>>() {}.getType();
                            List<NotificationItem> fetchedNotifications = gson.fromJson(notificationArray.toString(), listType);

                            notifications.addAll(fetchedNotifications);
                        } else {
                            Messenger.showAlertDialog(getContext(), "No Notification", "No notifications found.", "OK").show();
                        }

                        adapter.submitList(new ArrayList<>(notifications));
                        updateUIState();
                        swipeRefreshLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(getContext(), "Parse Error", "Failed to parse notification data.", "OK").show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(getContext(), "Error", errorMessage, "Ok").show();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }, "error", "notifications/get-notification.php").execute(new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);

        }
    }




    private void clearAllNotifications() {
        notifications.clear();
        adapter.submitList(new ArrayList<>(notifications));
        updateUIState();
    }

    private void updateUIState() {
        if (notifications.isEmpty()) {
            emptyStateView.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
            btnClearAll.setVisibility(View.GONE);
        } else {
            emptyStateView.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
            btnClearAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNotificationClick(NotificationItem notification) {
        try {
         JSONObject payload = new JSONObject();
            payload.put("notification_id", notification.getId());
            new PostTask(getContext(), new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    Messenger.showAlertDialog(getContext(), "Success", notification.getMessage(), "OK").show();
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(getContext(), "Error", errorMessage, "Ok").show();
                }
            }, "error", "notifications/mark-as-read.php").execute(payload);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}