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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull NotificationItem oldItem, @NonNull NotificationItem newItem) {
                    return oldItem.getId() == newItem.getId() &&
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

        // Date formatter for parsing the API response
        private final SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

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

            NotificationItem.NotificationType type = notification.getType();
            switch (type) {
                case ORDER:
                    iconRes = R.drawable.ic_order;
                    break;
                case PAYMENT:
                    iconRes = R.drawable.ic_payment;
                    break;

                case ACCOUNT:
                    iconRes = R.drawable.ic_account;
                    break;
                case GENERAL:
                default:
                    iconRes = R.drawable.ic_notifications;
                    break;
            }
            ivNotificationIcon.setImageResource(iconRes);

            tvNotificationTitle.setText(notification.getTitle());
            tvNotificationMessage.setText(notification.getMessage());

            // Parse the createdAt string to display relative time
            String createdAt = notification.getCreatedAt();
            if (createdAt != null && !createdAt.isEmpty()) {
                try {
                    Date date = apiDateFormat.parse(createdAt);
                    if (date != null) {
                        tvNotificationTime.setText(TimeUtils.getRelativeTime(date));
                    } else {
                        tvNotificationTime.setText("Recently");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    tvNotificationTime.setText("Recently");
                }
            } else {
                tvNotificationTime.setText("Recently");
            }

            // Show unread indicator
            indicatorUnread.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);

            // Handle item click
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNotificationClick(notification);
                }
                // Mark as read when clicked
                if (!notification.isRead()) {
                    notification.markAsRead();
                    indicatorUnread.setVisibility(View.GONE);
                    // You might want to notify the server about the read status
                }
            });
        }
    }

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem notification);
    }
}