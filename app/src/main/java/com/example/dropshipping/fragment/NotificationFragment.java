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
import com.example.dropshipping.model.NotificationItem;

import java.util.ArrayList;
import java.util.Arrays;
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
        swipeRefreshLayout.postDelayed(() -> {
            notifications.clear();
            notifications.addAll(generateSampleNotifications());

            adapter.submitList(new ArrayList<>(notifications));
            updateUIState();
        }, 1000);
    }

    private List<NotificationItem> generateSampleNotifications() {
        Calendar calendar = Calendar.getInstance();
        String[] orderIds = {"#ORD-12987", "#ORD-45612", "#ORD-78945"};
        String[] promotions = {"Summer Sale", "Flash Deal", "New Arrivals"};

        return Arrays.asList(
                new NotificationItem(
                        "1",
                        NotificationItem.Type.ORDER,
                        "Order Shipped",
                        "Your order " + orderIds[random.nextInt(orderIds.length)] + " has been shipped",
                        getPastDate(calendar, random.nextInt(60))
                ),
                new NotificationItem(
                        "2",
                        NotificationItem.Type.PROMOTION,
                        "Special Offer",
                        promotions[random.nextInt(promotions.length)] + " - Get 20% off on selected items",
                        getPastDate(calendar, random.nextInt(120))
                ),
                new NotificationItem(
                        "3",
                        NotificationItem.Type.SYSTEM,
                        "App Update",
                        "New version 1.2.3 is available with bug fixes",
                        getPastDate(calendar, random.nextInt(180))
                ),
                new NotificationItem(
                        "4",
                        NotificationItem.Type.ORDER,
                        "Order Delivered",
                        "Your order " + orderIds[random.nextInt(orderIds.length)] + " has been delivered",
                        getPastDate(calendar, random.nextInt(240))
                ),
                new NotificationItem(
                        "5",
                        NotificationItem.Type.PROMOTION,
                        "Limited Time Offer",
                        "Last chance! 30% off ends tonight",
                        getPastDate(calendar, random.nextInt(300))
                )
        );
    }

    private Date getPastDate(Calendar calendar, int minutesAgo) {
        calendar.add(Calendar.MINUTE, -minutesAgo);
        return calendar.getTime();
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
        Toast.makeText(requireContext(),
                "Notification clicked: " + notification.getTitle(),
                Toast.LENGTH_SHORT).show();
    }
}
