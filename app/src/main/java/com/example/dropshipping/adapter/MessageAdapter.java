package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dropshipping.R;
import com.example.dropshipping.model.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_SUPPORT = 1;
    private static final int TYPE_SYSTEM = 2;

    private final List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.isSystemMessage()) {
            return TYPE_SYSTEM;
        } else if (message.isUserMessage()) {
            return TYPE_USER;
        } else {
            return TYPE_SUPPORT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_USER:
                return new UserMessageViewHolder(
                        inflater.inflate(R.layout.item_message_user, parent, false)
                );
            case TYPE_SUPPORT:
                return new SupportMessageViewHolder(
                        inflater.inflate(R.layout.item_message_support, parent, false)
                );
            case TYPE_SYSTEM:
            default:
                return new SystemMessageViewHolder(
                        inflater.inflate(R.layout.item_message_system, parent, false)
                );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_USER:
                ((UserMessageViewHolder) holder).bind(message);
                break;
            case TYPE_SUPPORT:
                ((SupportMessageViewHolder) holder).bind(message);
                break;
            case TYPE_SYSTEM:
                ((SystemMessageViewHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    // ViewHolder classes
    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageText, tvTimestamp;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        void bind(Message message) {
            tvMessageText.setText(message.getText());
            tvTimestamp.setText(message.getTimestamp());
        }
    }

    static class SupportMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageText, tvSenderName, tvTimestamp;

        public SupportMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            tvSenderName = itemView.findViewById(R.id.tvSenderName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        void bind(Message message) {
            tvMessageText.setText(message.getText());
            tvSenderName.setText(message.getSenderName());
            tvTimestamp.setText(message.getTimestamp());
        }
    }

    static class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSystemMessage;

        public SystemMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSystemMessage = itemView.findViewById(R.id.tvSystemMessage);
        }

        void bind(Message message) {
            tvSystemMessage.setText(message.getText());
        }
    }
}