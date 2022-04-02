package com.lleans.spp_kelompok_2.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class CustomRequestPermission extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 1;
    private final Context context;

    public CustomRequestPermission(Context context) {
        this.context = context;
    }

    public boolean checkPermission() {
        ArrayList<String> arrPerm = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);
            ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_PERMISSION);
        }
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    UtilsUI.toaster(context, "Akses diberikan");
                }
            }
        } else {
            UtilsUI.dialog(this.context, "Akses dibutuhkan !", "Akses dibutuhkan untuk menyimpan gambar.", true).setPositiveButton("Ok", (dialog, which) -> checkPermission()).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION);
        }
    }

}
