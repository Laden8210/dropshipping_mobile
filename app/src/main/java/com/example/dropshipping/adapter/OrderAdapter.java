package com.example.dropshipping.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.view.OrderDetailActivity;
import com.example.dropshipping.R;
import com.example.dropshipping.model.Order;
import com.example.dropshipping.model.OrderItem;
import com.example.dropshipping.view.TrackingDetailActivity;

import org.json.JSONObject;

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

        if (order.getStatus().equalsIgnoreCase("delivered")) {
            holder.actionButtonsLayout.setVisibility(View.VISIBLE);
            holder.btnReturn.setOnClickListener(v -> {
                processReturn(order);
            });
            holder.btnComplete.setOnClickListener(v -> {
                processComplete(order);
            });
        } else {
            holder.actionButtonsLayout.setVisibility(View.GONE);
        }


        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.getOrderId());
            context.startActivity(intent);
        });

        if (order.getTrackingNumber() == null || order.getTrackingNumber().isEmpty()) {
            holder.btnTrack.setVisibility(View.GONE);
        } else {
            holder.btnTrack.setVisibility(View.VISIBLE);
            holder.btnTrack.setOnClickListener(v -> {
                Intent intent = new Intent(context, TrackingDetailActivity.class);
                intent.putExtra("tracking_number", order.getTrackingNumber());
                Log.d("OrderAdapter", "Tracking number: " + order.getTrackingNumber());
                context.startActivity(intent);
            });
        }


    }

    public void processReturn(Order order) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("order_id", order.getOrderId());

            new PostTask(context, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            showAlertDialog("Success",
                                    "Return process initiated for order " + order.getOrderNumber(),
                                    "OK", null, null, null, true);
                        } else {
                            String errorMessage = response.optString("message", "Failed to initiate return");
                            showAlertDialog("Error", errorMessage, "OK", null, null, null, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlertDialog("Error",
                                "Error initiating return for order " + order.getOrderNumber(),
                                "OK", null, null, null, true);
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    showAlertDialog("Error", errorMessage, "OK", null, null, null, true);
                }
            }, "success", "order/initiate-return.php").execute(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog("Error",
                    "Error initiating return for order " + order.getOrderNumber(),
                    "OK", null, null, null, true);
        }
    }

    // Helper method to show alert dialog
    private void showAlertDialog(String title, String message, String positiveButtonText,
                                 String negativeButtonText, DialogInterface.OnClickListener positiveListener,
                                 DialogInterface.OnClickListener negativeListener, boolean cancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable);

        // Set positive button
        if (positiveButtonText != null) {
            builder.setPositiveButton(positiveButtonText, positiveListener != null ?
                    positiveListener : (dialog, which) -> dialog.dismiss());
        }

        // Set negative button
        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, negativeListener != null ?
                    negativeListener : (dialog, which) -> dialog.dismiss());
        }

        builder.show();
    }

    public void processComplete(Order order) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("order_id", order.getOrderId());

            new PostTask(context, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            showAlertDialog("Success",
                                    "Order " + order.getOrderNumber() + " marked as complete",
                                    "OK", null, null, null, true);
                        } else {
                            String errorMessage = response.optString("message", "Failed to mark order as complete");
                            showAlertDialog("Error", errorMessage, "OK", null, null, null, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlertDialog("Error",
                                "Error marking order " + order.getOrderNumber() + " as complete",
                                "OK", null, null, null, true);
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    showAlertDialog("Error", errorMessage, "OK", null, null, null, true);
                }
            }, "success", "order/mark-complete.php").execute(requestJson);
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog("Error",
                    "Error marking order " + order.getOrderNumber() + " as complete",
                    "OK", null, null, null, true);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvStatus, tvOrderDate, tvProductSummary, tvTotalAmount;
        Button btnViewDetails, btnTrack, btnReturn, btnComplete;
        LinearLayout actionButtonsLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvProductSummary = itemView.findViewById(R.id.tvProductSummary);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnTrack = itemView.findViewById(R.id.btnTrack);
            actionButtonsLayout = itemView.findViewById(R.id.actionButtonsLayout);
            btnReturn = itemView.findViewById(R.id.btnReturn);
            btnComplete = itemView.findViewById(R.id.btnComplete);
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