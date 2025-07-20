package com.example.dropshipping.model;

public class Review {
    private String reviewerName;
    private float rating;
    private String reviewDate;
    private String reviewText;

    // Getters and setters
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
}