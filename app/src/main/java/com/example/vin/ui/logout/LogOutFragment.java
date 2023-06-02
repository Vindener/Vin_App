package com.example.vin.ui.logout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vin.R;
import com.example.vin.addition.ConfirmationDialogFragment;


public class LogOutFragment extends Fragment {
    private static final int REQUEST_CONFIRMATION = 1;
    private void openConfirmationDialog() {
        ConfirmationDialogFragment dialog = new ConfirmationDialogFragment();
        dialog.setTargetFragment(this, REQUEST_CONFIRMATION);
        dialog.show(getFragmentManager(), "ConfirmationDialogFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_out, container, false);
        super.onViewCreated(view, savedInstanceState);
        openConfirmationDialog();

        // Inflate the layout for this fragment
        return view;
    }
}