package com.example.vin.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vin.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

      //  final TextView textView = binding.textHome;
    //    profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email_ = sharedPreferences.getString("email","");
        String phone_ = sharedPreferences.getString("phone","");
        String name_ = sharedPreferences.getString("name","");

        final EditText email = binding.UserEmail;
        email.setText(email_);

        final EditText phone = binding.UserPhone;
        phone.setText(phone_);

        final EditText name = binding.UserName;
        name.setText(name_);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}