package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dropshipping.R;
import com.example.dropshipping.model.Ticket;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> tickets;
    private OnTicketClickListener listener;

    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    public TicketAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setOnTicketClickListener(OnTicketClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return tickets != null ? tickets.size() : 0;
    }

    public void updateTickets(List<Ticket> newTickets) {
        this.tickets = newTickets;
        notifyDataSetChanged();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTicketId;
        private TextView tvSubject;
        private TextView tvOrderNumber;
        private TextView tvStatus;
        private TextView tvPriority;
        private TextView tvStoreName;
        private TextView tvOrderAmount;
        private TextView tvMessageCount;
        private TextView tvCreatedAt;
        private TextView tvDescription;
        private CardView cardView;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketId = itemView.findViewById(R.id.tvTicketId);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvOrderAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvMessageCount = itemView.findViewById(R.id.tvMessageCount);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onTicketClick(tickets.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Ticket ticket) {
            tvTicketId.setText("Ticket: " + ticket.getTicketId());
            tvSubject.setText(ticket.getSubject());
            tvOrderNumber.setText("Order: " + ticket.getOrderNumber());
            tvStatus.setText(ticket.getStatus().toUpperCase());
            tvPriority.setText(ticket.getPriority().toUpperCase());
            tvStoreName.setText("Store: " + ticket.getStoreName());
            tvOrderAmount.setText("₱" + formatAmount(ticket.getOrderAmount()));
            tvMessageCount.setText(ticket.getMessageCount() + " messages");
            tvCreatedAt.setText(formatDate(ticket.getCreatedAt()));
            tvDescription.setText(ticket.getDescription());

            // Set status color
            int statusColor = getStatusColor(ticket.getStatus());
            tvStatus.setTextColor(statusColor);

            // Set priority color
            int priorityColor = getPriorityColor(ticket.getPriority());
            tvPriority.setTextColor(priorityColor);
        }

        private String formatAmount(String amount) {
            try {
                double amt = Double.parseDouble(amount);
                return String.format("%.2f", amt);
            } catch (NumberFormatException e) {
                return amount;
            }
        }

        private String formatDate(String dateString) {
            if (dateString == null || dateString.isEmpty()) return "";
            // Simple date formatting - you can enhance this with proper date formatting
            return dateString.replace(" ", "\n");
        }

        private int getStatusColor(String status) {
            switch (status.toLowerCase()) {
                case "open":
                    return itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark);
                case "in_progress":
                    return itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                case "resolved":
                    return itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                case "closed":
                    return itemView.getContext().getResources().getColor(android.R.color.darker_gray);
                default:
                    return itemView.getContext().getResources().getColor(android.R.color.black);
            }
        }

        private int getPriorityColor(String priority) {
            switch (priority.toLowerCase()) {
                case "high":
                    return itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                case "medium":
                    return itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                case "low":
                    return itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                default:
                    return itemView.getContext().getResources().getColor(android.R.color.black);
            }
        }
    }
}