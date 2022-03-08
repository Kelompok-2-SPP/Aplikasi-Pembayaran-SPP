package com.lleans.spp_kelompok_2.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class CustomRequestPermission extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1;
    private final Activity activity;

    public CustomRequestPermission(Activity activity){
        this.activity = activity;
    }

    public void checkPermission() {
        ArrayList<String> arrPerm = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
        if (!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION);
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
