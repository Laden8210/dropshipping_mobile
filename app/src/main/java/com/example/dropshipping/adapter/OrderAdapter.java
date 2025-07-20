package com.example.dropshipping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.view.OrderDetailActivity;
import com.example.dropshipping.R;
import com.example.dropshipping.model.Order;
import com.example.dropshipping.model.OrderItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orders;
private Context context;

    public OrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        String formattedDate = formatOrderDate(order.getCreatedAt());


        String productSummary = order.getItems().size() + " item(s)";
        if (!order.getItems().isEmpty()) {
            OrderItem firstItem = order.getItems().get(0);
            productSummary = firstItem.getQuantity() + " x " + firstItem.getName();
        }


        holder.tvOrderNumber.setText(order.getOrderNumber());
        holder.tvStatus.setText(order.getStatus().isEmpty() ? "Processing" : order.getStatus());
        holder.tvOrderDate.setText(formattedDate);
        holder.tvProductSummary.setText(productSummary);
        holder.tvTotalAmount.setText(String.format("Total: ₱%,.2f", order.getTotalAmount()));
        int bgResId = getStatusBackground(order.getStatus());
        holder.tvStatus.setBackgroundResource(bgResId);

        int textColor = getStatusTextColor(order.getStatus());
        holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), textColor));


        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.getOrderId());
            context.startActivity(intent);
        });

        holder.btnTrack.setOnClickListener(v -> {
            Toast.makeText(context, "Track order: " + order.getOrderNumber(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvStatus, tvOrderDate, tvProductSummary, tvTotalAmount;
        Button btnViewDetails, btnTrack;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvProductSummary = itemView.findViewById(R.id.tvProductSummary);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnTrack = itemView.findViewById(R.id.btnTrack);
        }
    }

    private String formatOrderDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            return rawDate;
        }
    }

    private int getStatusBackground(String status) {
        if (status.equalsIgnoreCase("pending")) {
            return R.drawable.bg_status_pending;
        } else if (status.equalsIgnoreCase("shipped")) {
            return R.drawable.bg_status_shipped;
        } else if (status.equalsIgnoreCase("delivered")) {
            return R.drawable.bg_status_delivered;
        } else if (status.equalsIgnoreCase("cancelled")) {
            return R.drawable.bg_status_cancelled;
        }
        return R.drawable.bg_status_pending;
    }

    private int getStatusTextColor(String status) {
        if (status.equalsIgnoreCase("pending")) {
            return R.color.status_pending_text;
        } else if (status.equalsIgnoreCase("shipped")) {
            return R.color.status_shipped_text;
        } else if (status.equalsIgnoreCase("delivered")) {
            return R.color.status_delivered_text;
        } else if (status.equalsIgnoreCase("cancelled")) {
            return R.color.status_cancelled_text;
        }
        return R.color.status_pending_text;
    }


}