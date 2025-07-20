package com.example.dropshipping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.model.StatusItem;
import com.example.dropshipping.view.OrderDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatusTimelineAdapter extends RecyclerView.Adapter<StatusTimelineAdapter.ViewHolder> {
    public StatusTimelineAdapter(List<StatusItem> statusItems, Context context) {
        this.statusItems = statusItems;
        this.context = context;
    }
    private List<StatusItem> statusItems;

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StatusItem item = statusItems.get(position);
        holder.tvStatus.setText(item.getStatus());
        holder.tvTimestamp.setText(formatOrderDate(item.getTimestamp()));

    }

    @Override
    public int getItemCount() {
        return statusItems.size();
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView tvStatus, tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}