package com.example.dropshipping.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dropshipping.MainActivity;
import com.example.dropshipping.R;
import com.example.dropshipping.util.Messenger;
import com.example.dropshipping.view.AddressBookActivity;
import com.example.dropshipping.view.CustomerSupportActivity;
import com.example.dropshipping.view.EditProfileActivity;

import com.google.android.material.button.MaterialButton;

public class AccountFragment extends Fragment {

    private LinearLayout btnContactUs, btnAboutApp, btnAddressBook;
    private MaterialButton btnEditProfile, btnLogout;
    private TextView tvUserName, tvUserEmail, tvAppVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize all views
        initViews(view);

        // Set up click listeners
        setupClickListeners();

        // Load user data
        loadUserData();

        return view;
    }

    private void initViews(View view) {
        // Buttons
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        // LinearLayout clickable items
        btnContactUs = view.findViewById(R.id.btnContactUs);
        btnAboutApp = view.findViewById(R.id.btnAboutApp);
        btnAddressBook = view.findViewById(R.id.btnAddressBook);

        // TextViews
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvAppVersion = view.findViewById(R.id.tvAppVersion);
    }

    private void setupClickListeners() {
        // Edit Profile Button
        btnEditProfile.setOnClickListener(v -> navigateToEditProfile());

        // Logout Button
        btnLogout.setOnClickListener(v -> performLogout());

        // Contact Us
        btnContactUs.setOnClickListener(v -> navigateToContactUs());



        // About App
        btnAboutApp.setOnClickListener(v -> showAboutApp());

        // Address Book
        btnAddressBook.setOnClickListener(v -> navigateToAddressBook());
    }

    private void loadUserData() {
        // TODO: Load actual user data from your database or shared preferences
        // For now, using placeholder data
        tvUserName.setText("John Doe");
        tvUserEmail.setText("john.doe@example.com");

        // Set app version (example)
        try {
            String versionName = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0)
                    .versionName;
            tvAppVersion.setText("v" + versionName);
        } catch (Exception e) {
            tvAppVersion.setText("v1.0.0");
        }
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void performLogout() {

        Messenger.showAlertDialog(getContext(), "Logout",
                "Are you sure you want to logout?", "Yes", "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


    }

    private void navigateToContactUs() {
        Intent intent = new Intent(getActivity(), CustomerSupportActivity.class);
        startActivity(intent);
    }



    private void showAboutApp() {
        // Create a custom dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_about_app, null);
        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Customize dialog window
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Set close button action
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Set version text
        TextView tvVersion = dialogView.findViewById(R.id.tvVersion);
        try {
            String versionName = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0)
                    .versionName;
            tvVersion.setText(getString(R.string.version, versionName));
        } catch (Exception e) {
            tvVersion.setText(getString(R.string.version, "1.0.0"));
        }
    }
    private void navigateToAddressBook() {
        Intent intent = new Intent(getActivity(), AddressBookActivity.class);
        startActivity(intent);
    }
}