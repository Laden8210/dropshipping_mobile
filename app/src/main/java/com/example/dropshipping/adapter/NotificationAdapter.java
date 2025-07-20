package com.example.dropshipping.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.model.NotificationItem;
import com.example.dropshipping.util.TimeUtils;

import java.util.Date;

public class NotificationAdapter extends ListAdapter<NotificationItem, NotificationAdapter.NotificationViewHolder> {

    private final OnNotificationClickListener listener;

    public NotificationAdapter(OnNotificationClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<NotificationItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NotificationItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull NotificationItem oldItem, @NonNull NotificationItem newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull NotificationItem oldItem, @NonNull NotificationItem newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                            oldItem.getMessage().equals(newItem.getMessage()) &&
                            oldItem.getTimestamp().equals(newItem.getTimestamp()) &&
                            oldItem.isRead() == newItem.isRead();
                }
            };

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = getItem(position);
        holder.bind(notification, listener);
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivNotificationIcon;
        private final TextView tvNotificationTitle;
        private final TextView tvNotificationMessage;
        private final TextView tvNotificationTime;
        private final View indicatorUnread;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNotificationIcon = itemView.findViewById(R.id.ivNotificationIcon);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            indicatorUnread = itemView.findViewById(R.id.indicatorUnread);
        }

        public void bind(NotificationItem notification, OnNotificationClickListener listener) {
            // Set icon based on notification type
            int iconRes = R.drawable.ic_notifications;
            switch (notification.getType()) {
//                case ORDER:
//                    iconRes = R.drawable.ic_order;
//                    break;
//                case PROMOTION:
//                    iconRes = R.drawable.ic_promotion;
//                    break;
//                case SYSTEM:
//                    iconRes = R.drawable.ic_system;
//                    break;
            }
            ivNotificationIcon.setImageResource(iconRes);

            tvNotificationTitle.setText(notification.getTitle());
            tvNotificationMessage.setText(notification.getMessage());
            tvNotificationTime.setText(TimeUtils.getRelativeTime(notification.getTimestamp()));

            // Show unread indicator
            indicatorUnread.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);

            // Handle item click
            itemView.setOnClickListener(v -> {
                listener.onNotificationClick(notification);
                // Mark as read when clicked
                if (!notification.isRead()) {
                    notification.setRead(true);
                    indicatorUnread.setVisibility(View.GONE);
                }
            });
        }
    }

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem notification);
    }
}