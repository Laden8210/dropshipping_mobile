package com.example.dropshipping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropshipping.R;
import com.example.dropshipping.model.Review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvReviewerName.setText(review.getReviewerName());
        holder.ratingBar.setRating(review.getRating());
        holder.tvReviewDate.setText(formatReviewDate(review.getReviewDate()));
        holder.tvReviewText.setText(review.getReviewText());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewerName, tvReviewDate, tvReviewText;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    private String formatReviewDate(String rawDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(rawDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            return rawDate;
        }
    }
}
