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

    private final int REQUEST_PERMISSION = 1;

    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate binding, make MainActivity public
        act = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        intent = new Intent(MainActivity.this, LauncherFragment.class);
        setContentView(binding.getRoot());

        checkPermission();

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


    private void checkPermission() {
        ArrayList<String> arrPerm = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
        if(!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    String permission = permissions[i];
                    if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Akses diberikan", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Akses diberikan", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (Manifest.permission.MANAGE_EXTERNAL_STORAGE.equals(permission)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Akses diberikan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                showExplanation();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION);
            }
        }
    }

    private void showExplanation() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Akses Dibutuhkan !")
                .setMessage("Akses dibutuhkan untuk menyimpan gambar")
                .setPositiveButton(android.R.string.ok, (dialog, id) -> checkPermission());
        builder.create().show();
    }
}