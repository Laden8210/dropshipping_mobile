package com.example.dropshipping.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.adapter.TicketAdapter;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.Ticket;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerSupportActivity extends AppCompatActivity implements TicketAdapter.OnTicketClickListener {

    private RecyclerView recyclerViewTickets;
    private TicketAdapter ticketAdapter;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private TextView tvTotalTickets;
    private TextView tvOpenTickets;
    private TextView tvInProgressTickets;
    private TextView tvResolvedTickets;
    private TextView tvClosedTickets;
    private List<Ticket> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_support);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadTickets();
    }

    private void initViews() {
        recyclerViewTickets = findViewById(R.id.recyclerViewTickets);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvTotalTickets = findViewById(R.id.tvTotalTickets);
        tvOpenTickets = findViewById(R.id.tvOpenTickets);
        tvInProgressTickets = findViewById(R.id.tvInProgressTickets);
        tvResolvedTickets = findViewById(R.id.tvResolvedTickets);
        tvClosedTickets = findViewById(R.id.tvClosedTickets);

        // Setup RecyclerView
        ticketList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(ticketList);
        ticketAdapter.setOnTicketClickListener(this);

        recyclerViewTickets.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTickets.setAdapter(ticketAdapter);
    }

    private void loadTickets() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewTickets.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        new PostTask(this, new PostCallback() {
            @Override
            public void onPostSuccess(String responseData) {
                progressBar.setVisibility(View.GONE);
                try {
                    parseAndDisplayTickets(responseData);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showError("Error parsing ticket data");
                }
            }

            @Override
            public void onPostError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                showError(errorMessage);
            }
        }, "error", "support/get-tickets.php").execute(new JSONObject());
    }

    private void parseAndDisplayTickets(String responseData) throws JSONException {
        JSONObject response = new JSONObject(responseData);
        JSONObject data = response.getJSONObject("data");

        // Parse tickets
        JSONArray ticketsArray = data.getJSONArray("tickets");
        ticketList.clear();

        for (int i = 0; i < ticketsArray.length(); i++) {
            JSONObject ticketJson = ticketsArray.getJSONObject(i);
            Ticket ticket = new Ticket(ticketJson);
            ticketList.add(ticket);
        }

        // Parse summary
        JSONObject summary = data.getJSONObject("summary");
        updateSummaryViews(summary);

        // Update UI
        if (ticketList.isEmpty()) {
            recyclerViewTickets.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTickets.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
            ticketAdapter.updateTickets(ticketList);
        }
    }

    private void updateSummaryViews(JSONObject summary) throws JSONException {
        if (tvTotalTickets != null) {
            tvTotalTickets.setText("Total: " + summary.getInt("total_tickets"));
        }
        if (tvOpenTickets != null) {
            tvOpenTickets.setText("Open: " + summary.getInt("open_tickets"));
        }
        if (tvInProgressTickets != null) {
            tvInProgressTickets.setText("In Progress: " + summary.getInt("in_progress_tickets"));
        }
        if (tvResolvedTickets != null) {
            tvResolvedTickets.setText("Resolved: " + summary.getInt("resolved_tickets"));
        }
        if (tvClosedTickets != null) {
            tvClosedTickets.setText("Closed: " + summary.getInt("closed_tickets"));
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText("Error loading tickets: " + message);
    }

    @Override
    public void onTicketClick(Ticket ticket) {
        Intent intent = new Intent(this, ViewTicketMessage.class);
        intent.putExtra("ticket_id", ticket.getTicketId());
        startActivity(intent);
    }
}