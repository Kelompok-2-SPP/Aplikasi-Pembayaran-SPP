package com.lleans.spp_kelompok_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.databinding.LoginBinding;

public class Login extends Fragment {

    private LoginBinding binding;
    private String type;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController nav = Navigation.findNavController(view);

        if(type.equals("siswa")){
            binding.loginBtn.setOnClickListener(v -> nav.navigate(R.id.action_login_to_homepage2));
        }else {
            binding.loginBtn.setOnClickListener(v -> nav.navigate(R.id.action_login_to_homepage3));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        type = getActivity().getIntent().getStringExtra("type");
        binding = LoginBinding.inflate(inflater, container, false);

        if (type.equals("siswa")) {
            binding.header.setText("Login Siswa");
            binding.logindesc.setText("Masukkan NISN dan Password anda yang sudah terdaftar.");
            binding.usernameLabel.setText("NISN");
        }
        return binding.getRoot();
    }
}