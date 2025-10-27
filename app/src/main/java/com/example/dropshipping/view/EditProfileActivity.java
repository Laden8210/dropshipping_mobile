package com.example.dropshipping.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dropshipping.R;
import com.example.dropshipping.api.ApiAddress;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.util.Messenger;
import com.example.dropshipping.util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final String TAG = "EditProfileActivity";

    private MaterialButton btnCancel, btnSave, btnChangePhoto;
    private TextInputEditText etFirstName, etLastName, etEmail, etPhoneNumber, etBirthDate, etGender;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private TextInputLayout tilBirthDate, tilGender;
    private com.google.android.material.card.MaterialCardView cardPassword;
    private ShapeableImageView ivProfilePicture;

    private Calendar birthDateCalendar;
    private SimpleDateFormat dateFormatter;
    private boolean isGoogleAuth = false;
    private Uri selectedImageUri;
    private String currentAvatarUrl = "";

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
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);

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
        btnChangePhoto.setOnClickListener(v -> openImagePicker());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveProfileChanges());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Load the selected image into the ImageView
                Glide.with(this)
                        .load(selectedImageUri)
                        .into(ivProfilePicture);

                Log.d(TAG, "Image selected: " + selectedImageUri.toString());
            }
        }
    }

    private void uploadProfilePictureWithCallback(ProfilePictureUploadCallback callback) {
        if (selectedImageUri == null) {
            callback.onUploadSuccess();
            return;
        }

        try {
            // Create a temporary file from the URI
            File file = createTempFileFromUri(selectedImageUri);
            if (file == null) {
                callback.onUploadFailure("Could not create file from selected image");
                return;
            }

            Log.d(TAG, "Uploading file: " + file.getAbsolutePath() + ", size: " + file.length());

            OkHttpClient client = new OkHttpClient();

            // Get the auth token (you'll need to implement this method)
            String authToken = SessionManager.getInstance(this).getToken();
            if (authToken == null) {
                callback.onUploadFailure("Authentication token not found");
                return;
            }

            // Create multipart request body
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("profile_picture",
                            getFileName(selectedImageUri),
                            RequestBody.create(MediaType.parse("image/*"), file))
                    .build();

            // Create request with Authorization header
            Request request = new Request.Builder()
                    .url(ApiAddress.url + "auth/upload-profile-picture.php")
                    .header("Authorization", "Bearer " + authToken)
                    .post(requestBody)
                    .build();

            // Show loading
            runOnUiThread(() -> Messenger.showProgressDialog(this, "Uploading profile picture..."));

            // Execute request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Upload failed: " + e.getMessage());
                    // Delete temporary file
                    if (file.exists()) {
                        file.delete();
                    }
                    callback.onUploadFailure(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // Delete temporary file
                    if (file.exists()) {
                        file.delete();
                    }

                    runOnUiThread(() -> Messenger.dismissProgressDialog());

                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.d(TAG, "Upload response: " + responseData);

                            JSONObject jsonResponse = new JSONObject(responseData);

                            if (jsonResponse.getString("status").equals("success")) {
                                currentAvatarUrl = jsonResponse.getJSONObject("data")
                                        .getString("avatar_url");
                                callback.onUploadSuccess();
                            } else {
                                String errorMessage = jsonResponse.optString("message", "Upload failed");
                                callback.onUploadFailure(errorMessage);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Parse error: " + e.getMessage());
                            callback.onUploadFailure("Failed to parse response");
                        }
                    } else {
                        callback.onUploadFailure("Server returned code: " + response.code());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Upload exception: " + e.getMessage());
            callback.onUploadFailure(e.getMessage());
        }
    }

    private File createTempFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return null;
            }

            // Create a temporary file
            File tempFile = new File(getCacheDir(), "temp_profile_picture_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return tempFile;

        } catch (IOException e) {
            Log.e(TAG, "Error creating temp file: " + e.getMessage());
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file name: " + e.getMessage());
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void loadUserProfile() {
        try {
            JSONObject requestJson = new JSONObject();

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

            // Load profile picture
            if (userData.has("avatar_url") && !userData.isNull("avatar_url")) {
                currentAvatarUrl = userData.optString("avatar_url", "");
                if (!currentAvatarUrl.isEmpty()) {
                    Glide.with(this)
                            .load(ApiAddress.userUrl + currentAvatarUrl)
                            .into(ivProfilePicture);
                }
            }

            // Check if user uses Google auth
            isGoogleAuth = userData.optInt("is_google_auth", 0) == 1;
            if (isGoogleAuth) {
                // Hide password section for Google auth users
                cardPassword.setVisibility(android.view.View.GONE);
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

        // Upload profile picture first if selected
        if (selectedImageUri != null) {
            uploadProfilePictureAndThenSave();
        } else {
            saveProfileData();
        }
    }

    private void uploadProfilePictureAndThenSave() {
        uploadProfilePictureWithCallback(new ProfilePictureUploadCallback() {
            @Override
            public void onUploadSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this,
                            "Profile picture uploaded successfully",
                            Toast.LENGTH_SHORT).show();
                    saveProfileData();
                });
            }

            @Override
            public void onUploadFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this,
                            "Profile picture upload failed: " + errorMessage,
                            Toast.LENGTH_SHORT).show();
                    // Still save other profile data even if picture upload fails
                    saveProfileData();
                });
            }
        });
    }

    private void saveProfileData() {
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
                            Messenger.showAlertDialog(EditProfileActivity.this, "Success", "Profile Updated Successfully", "OK")
                                    .setOnDismissListener(dialog -> finish());
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

    // Interface for profile picture upload callback
    interface ProfilePictureUploadCallback {
        void onUploadSuccess();
        void onUploadFailure(String errorMessage);
    }
}