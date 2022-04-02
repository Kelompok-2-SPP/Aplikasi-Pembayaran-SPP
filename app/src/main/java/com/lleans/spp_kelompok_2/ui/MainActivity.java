package com.lleans.spp_kelompok_2.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lleans.spp_kelompok_2.databinding.ActivityMainBinding;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        intent = new Intent(this, LauncherFragment.class);
        SessionManager sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.isLoggedIn()) {
            moveActivity(sessionManager.getUserDetail().get(SessionManager.TYPE));
        }

        setContentView(binding.getRoot());
        binding.petugas.setOnClickListener(v -> moveActivity("petugas"));
        binding.siswa.setOnClickListener(v -> moveActivity("siswa"));
    }

    private void moveActivity(String type) {
        intent.putExtra("type", type);
        MainActivity.this.startActivity(intent);
    }
}