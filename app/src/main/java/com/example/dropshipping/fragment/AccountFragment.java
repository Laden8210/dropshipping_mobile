package com.example.dropshipping.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dropshipping.R;
import com.example.dropshipping.view.CustomerSupportActivity;

public class AccountFragment extends Fragment {

        private LinearLayout btnContactUs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        btnContactUs = view.findViewById(R.id.btnContactUs);

        btnContactUs.setOnClickListener(v -> {
            // Navigate to CustomerSupportActivity
            if (getActivity() != null) {
                getActivity().startActivity(new Intent(getActivity(), CustomerSupportActivity.class));
            }
        });
        return view;
    }
}