package com.example.dropshipping.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviews;
    private Context context;
    private List<String> englishBadWords;
    private List<String> tagalogBadWords;
    private Pattern wordBoundaryPattern;

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        initializeBadWords();
        this.wordBoundaryPattern = Pattern.compile("\\b");
    }

    private void initializeBadWords() {
        Resources resources = context.getResources();

        // Load English bad words
        String[] englishArray = resources.getStringArray(R.array.english_bad_words);
        englishBadWords = new ArrayList<>(Arrays.asList(englishArray));

        // Load Tagalog bad words
        String[] tagalogArray = resources.getStringArray(R.array.tagalog_bad_words);
        tagalogBadWords = new ArrayList<>(Arrays.asList(tagalogArray));
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

        // Filter bad words from reviewer name
        String filteredName = filterBadWords(review.getReviewerName());
        holder.tvReviewerName.setText(filteredName);

        holder.ratingBar.setRating(review.getRating());
        holder.tvReviewDate.setText(formatReviewDate(review.getReviewDate()));

        // Filter bad words from review text
        String filteredReview = filterBadWords(review.getReviewText());
        holder.tvReviewText.setText(filteredReview);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
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


    private String filterBadWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String filteredText = text;

        // Combine all bad words for checking
        List<String> allBadWords = new ArrayList<>();
        allBadWords.addAll(englishBadWords);
        allBadWords.addAll(tagalogBadWords);


        for (String badWord : allBadWords) {

            String regex = "(?i)\\b" + Pattern.quote(badWord) + "\\b";
            Pattern pattern = Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(filteredText);

            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String matchedWord = matcher.group();
                String censoredWord = censorWord(matchedWord);
                matcher.appendReplacement(sb, censoredWord);
            }
            matcher.appendTail(sb);
            filteredText = sb.toString();
        }

        return filteredText;
    }

    private String censorWord(String word) {
        if (word == null || word.length() <= 1) {
            return word;
        }

        StringBuilder censored = new StringBuilder();
        censored.append(word.charAt(0)); // Keep first letter

        // Process the rest of the word
        for (int i = 1; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (isVowel(currentChar)) {
                censored.append('*');
            } else {
                censored.append(currentChar);
            }
        }

        return censored.toString();
    }

    private boolean isVowel(char c) {
        return "aeiouAEIOU".indexOf(c) != -1;
    }


    public boolean containsBadWords(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        String lowerCaseText = text.toLowerCase();

        for (String badWord : englishBadWords) {
            if (lowerCaseText.contains(badWord.toLowerCase())) {
                return true;
            }
        }

        for (String badWord : tagalogBadWords) {
            if (lowerCaseText.contains(badWord.toLowerCase())) {
                return true;
            }
        }

        return false;
    }


    public List<String> getDetectedBadWords(String text) {
        List<String> detectedWords = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return detectedWords;
        }

        String lowerCaseText = text.toLowerCase();

        for (String badWord : englishBadWords) {
            if (lowerCaseText.contains(badWord.toLowerCase())) {
                detectedWords.add(badWord);
            }
        }

        for (String badWord : tagalogBadWords) {
            if (lowerCaseText.contains(badWord.toLowerCase())) {
                detectedWords.add(badWord);
            }
        }

        return detectedWords;
    }
}