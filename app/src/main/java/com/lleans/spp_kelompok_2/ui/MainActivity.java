package com.lleans.spp_kelompok_2.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lleans.spp_kelompok_2.databinding.ActivityMainBinding;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Intent intent;

    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate binding, make MainActivity public
        act = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        intent = new Intent(this, LauncherFragment.class);
        setContentView(binding.getRoot());

        SessionManager sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.isLoggedIn()) {
            moveActivity("siswa");
        }

        // Button listener
        binding.petugas.setOnClickListener(v -> moveActivity("petugas"));
        binding.siswa.setOnClickListener(v -> moveActivity("siswa"));
    }

    // Function to move activity LauncherFragment
    private void moveActivity(String type) {
        intent.putExtra("type", type);
        MainActivity.this.startActivity(intent);
    }
}