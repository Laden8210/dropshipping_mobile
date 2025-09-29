package com.example.dropshipping.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dropshipping.R;
import com.example.dropshipping.model.TicketMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<TicketMessage> messages;

    public MessageAdapter(List<TicketMessage> messages) {
        this.messages = messages;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        TicketMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public void addMessage(TicketMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void updateMessages(List<TicketMessage> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private CardView cardMessage;
        private TextView tvMessage;
        private TextView tvTimestamp;
        private TextView tvSenderType;
        private ImageView ivAttachment;
        private View viewReadIndicator;
        private LinearLayout layoutMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMessage = itemView.findViewById(R.id.cardMessage);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvSenderType = itemView.findViewById(R.id.tvSenderType);
            ivAttachment = itemView.findViewById(R.id.ivAttachment);
            viewReadIndicator = itemView.findViewById(R.id.viewReadIndicator);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
        }

        public void bind(TicketMessage message) {
            if (message == null) return;

            // Set message text
            if (message.getMessage() != null) {
                tvMessage.setText(message.getMessage());
            }

            // Set timestamp
            if (message.getCreatedAt() != null) {
                tvTimestamp.setText(formatTimestamp(message.getCreatedAt()));
            }

            // Configure message appearance based on sender type
            configureMessageAppearance(message);

            // Handle attachments
            if (message.hasAttachment()) {
                ivAttachment.setVisibility(View.VISIBLE);
                ivAttachment.setOnClickListener(v -> {
                    // Handle attachment click
                });
            } else {
                ivAttachment.setVisibility(View.GONE);
            }

            // Read indicator
            if (viewReadIndicator != null) {
                viewReadIndicator.setVisibility(message.isRead() ? View.GONE : View.VISIBLE);
            }
        }

        private void configureMessageAppearance(TicketMessage message) {
            // Get the CardView's layout params within the LinearLayout parent
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardMessage.getLayoutParams();

            if (message.isSystemMessage()) {
                // System messages - centered
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.setMargins(60, 8, 60, 8);

                cardMessage.setCardBackgroundColor(itemView.getContext().getResources()
                        .getColor(android.R.color.background_light));
                tvSenderType.setText("System");
                tvSenderType.setVisibility(View.VISIBLE);
                tvSenderType.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.darker_gray));
                tvMessage.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.black));
                tvTimestamp.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.darker_gray));
                layoutMessage.setGravity(Gravity.CENTER_HORIZONTAL);

            } else if (message.isUserMessage()) {
                // User messages - right aligned (YOU)
                params.gravity = Gravity.END;
                params.setMargins(60, 8, 16, 8);

                cardMessage.setCardBackgroundColor(itemView.getContext().getResources()
                        .getColor(android.R.color.holo_blue_light));
                tvSenderType.setText("You");
                tvSenderType.setVisibility(View.VISIBLE);
                tvSenderType.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.white));
                tvMessage.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.white));
                tvTimestamp.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.white));
                layoutMessage.setGravity(Gravity.END);

            } else if (message.isSupportMessage()) {
                // Support/Agent messages - left aligned
                params.gravity = Gravity.START;
                params.setMargins(16, 8, 60, 8);

                cardMessage.setCardBackgroundColor(itemView.getContext().getResources()
                        .getColor(android.R.color.white));
                tvSenderType.setText("Support");
                tvSenderType.setVisibility(View.VISIBLE);
                tvSenderType.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.holo_green_dark));
                tvMessage.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.black));
                tvTimestamp.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.darker_gray));
                layoutMessage.setGravity(Gravity.START);

            } else {
                // Default - left aligned
                params.gravity = Gravity.START;
                params.setMargins(16, 8, 60, 8);

                cardMessage.setCardBackgroundColor(itemView.getContext().getResources()
                        .getColor(android.R.color.white));
                tvSenderType.setText(message.getSenderType());
                tvSenderType.setVisibility(View.VISIBLE);
                tvMessage.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.black));
                tvTimestamp.setTextColor(itemView.getContext().getResources()
                        .getColor(android.R.color.darker_gray));
                layoutMessage.setGravity(Gravity.START);
            }

            // Apply the updated layout params
            cardMessage.setLayoutParams(params);
        }

        private String formatTimestamp(String timestamp) {
            if (timestamp == null || timestamp.isEmpty()) return "";

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(timestamp);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return timestamp;
            }
        }
    }
}