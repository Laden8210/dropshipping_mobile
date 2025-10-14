package com.example.dropshipping.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.util.Messenger;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {


    private MaterialButton btnCancel, btnSave;
    private TextInputEditText etFirstName, etLastName, etEmail, etPhoneNumber, etBirthDate, etGender;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private TextInputLayout tilBirthDate, tilGender;
    private com.google.android.material.card.MaterialCardView cardPassword;

    private Calendar birthDateCalendar;
    private SimpleDateFormat dateFormatter;
    private boolean isGoogleAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        setupClickListeners();
        loadUserProfile();
    }

    private void initializeViews() {
        // Initialize formatter
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        birthDateCalendar = Calendar.getInstance();

        // Profile picture section

        // Personal information fields
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etBirthDate = findViewById(R.id.etBirthDate);
        etGender = findViewById(R.id.etGender);
        tilBirthDate = findViewById(R.id.tilBirthDate);
        tilGender = findViewById(R.id.tilGender);

        // Password fields
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cardPassword = findViewById(R.id.cardPassword);

        // Action buttons
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupClickListeners() {
        // Birth date picker
        etBirthDate.setOnClickListener(v -> showDatePickerDialog());

        // Gender selection
        etGender.setOnClickListener(v -> showGenderSelectionDialog());

        // Change photo button


        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserProfile() {
        try {
            JSONObject requestJson = new JSONObject();
            // No parameters needed for get-profile

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            JSONObject userData = response.getJSONObject("data").getJSONObject("user");
                            populateUserData(userData);
                        } else {
                            String errorMessage = response.optString("message", "Failed to load profile");
                            Messenger.showAlertDialog(EditProfileActivity.this, "Error", errorMessage, "OK").show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(EditProfileActivity.this, "Error", "Failed to parse profile data", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(EditProfileActivity.this, "Error", errorMessage, "OK").show();
                }
            }, "error", "auth/get-profile.php").execute(requestJson);

        } catch (Exception e) {
            e.printStackTrace();
            Messenger.showAlertDialog(this, "Error", "Failed to load profile", "OK").show();
        }
    }

    private void populateUserData(JSONObject userData) {
        try {
            // Set basic user information
            etFirstName.setText(userData.optString("first_name", ""));
            etLastName.setText(userData.optString("last_name", ""));
            etEmail.setText(userData.optString("email", ""));
            etPhoneNumber.setText(userData.optString("phone_number", ""));

            // Set birth date if available
            String birthDate = userData.optString("birth_date", "");
            if (!birthDate.equals("null") && !birthDate.isEmpty()) {
                etBirthDate.setText(birthDate);
            }

            // Set gender
            String gender = userData.optString("gender", "");
            if (!gender.isEmpty()) {
                etGender.setText(gender.substring(0, 1).toUpperCase() + gender.substring(1));
            }


            // Check if user uses Google auth
            isGoogleAuth = userData.optInt("is_google_auth", 0) == 1;
            if (isGoogleAuth) {
                // Hide password section for Google auth users
                cardPassword.setVisibility(View.GONE);
                // Disable email editing for Google auth users
                etEmail.setEnabled(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(etBirthDate.getText())) {
            try {
                birthDateCalendar.setTime(dateFormatter.parse(etBirthDate.getText().toString()));
            } catch (Exception e) {
                birthDateCalendar = Calendar.getInstance();
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    birthDateCalendar.set(year, month, dayOfMonth);
                    etBirthDate.setText(dateFormatter.format(birthDateCalendar.getTime()));
                },
                birthDateCalendar.get(Calendar.YEAR),
                birthDateCalendar.get(Calendar.MONTH),
                birthDateCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set maximum date to today
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showGenderSelectionDialog() {
        final String[] genders = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gender")
                .setItems(genders, (dialog, which) -> {
                    etGender.setText(genders[which]);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void saveProfileChanges() {
        if (!validateForm()) {
            return;
        }

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("first_name", etFirstName.getText().toString().trim());
            requestJson.put("last_name", etLastName.getText().toString().trim());
            requestJson.put("email", etEmail.getText().toString().trim());
            requestJson.put("phone_number", etPhoneNumber.getText().toString().trim());

            // Only include birth date if not empty
            String birthDate = etBirthDate.getText().toString().trim();
            if (!birthDate.isEmpty()) {
                requestJson.put("birth_date", birthDate);
            }

            // Only include gender if not empty
            String gender = etGender.getText().toString().trim();
            if (!gender.isEmpty()) {
                requestJson.put("gender", gender.toLowerCase());
            }

            // Include password fields only if provided and not using Google auth
            if (!isGoogleAuth) {
                String currentPassword = etCurrentPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (!currentPassword.isEmpty()) {
                    requestJson.put("current_password", currentPassword);
                }
                if (!newPassword.isEmpty()) {
                    requestJson.put("new_password", newPassword);
                }
                if (!confirmPassword.isEmpty()) {
                    requestJson.put("confirm_password", confirmPassword);
                }
            }

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    try {
                        JSONObject response = new JSONObject(responseData);
                        if (response.getString("status").equals("success")) {
                            Messenger.showAlertDialog(EditProfileActivity.this, "Error", "Profile Update Successfully", "OK").show();
                        } else {
                            String errorMessage = response.optString("message", "Failed to update profile");
                            Messenger.showAlertDialog(EditProfileActivity.this, "Error", errorMessage, "OK").show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Messenger.showAlertDialog(EditProfileActivity.this, "Error", "Failed to update profile", "OK").show();
                    }
                }

                @Override
                public void onPostError(String errorMessage) {
                    Messenger.showAlertDialog(EditProfileActivity.this, "Error", errorMessage, "OK").show();
                }
            }, "error", "auth/update-profile.php").execute(requestJson);

        } catch (Exception e) {
            e.printStackTrace();
            Messenger.showAlertDialog(this, "Error", "Failed to save profile changes", "OK").show();
        }
    }

    private boolean validateForm() {
        // Validate required fields
        if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        // Validate email format
        String email = etEmail.getText().toString().trim();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        // Validate password fields if not using Google auth
        if (!isGoogleAuth) {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // If any password field is filled, all must be filled
            boolean anyPasswordFilled = !currentPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty();
            boolean allPasswordsFilled = !currentPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty();

            if (anyPasswordFilled && !allPasswordsFilled) {
                Messenger.showAlertDialog(this, "Validation Error",
                        "Please fill all password fields to change your password", "OK").show();
                return false;
            }

            // Validate new password confirmation
            if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                etConfirmPassword.requestFocus();
                return false;
            }

            // Validate password strength
            if (!newPassword.isEmpty() && newPassword.length() < 6) {
                etNewPassword.setError("Password must be at least 6 characters");
                etNewPassword.requestFocus();
                return false;
            }
        }

        return true;
    }
}