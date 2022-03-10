package com.lleans.spp_kelompok_2.ui.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;

import com.lleans.spp_kelompok_2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class UtilsUI {

    public static void nicknameBuilder(Context context, String name, TextView text, FrameLayout frame) {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);

        StringBuilder initials = new StringBuilder();

        for (String s : name.split(" ")) {
            initials.append(s.charAt(0));
        }

        text.setText(initials.toString());
        frame.setBackgroundTintList(ColorStateList.valueOf(androidColors[new Random(name.hashCode()).nextInt(androidColors.length)]));
    }

    public static void exportToPNG(Activity activity, ConstraintLayout layout, String title) throws IOException {
        Uri uri = null;
        String dir = Environment.DIRECTORY_PICTURES;
        int grey = activity.getResources().getColor(R.color.neutral_lightGrey);

        layout.setBackgroundColor(grey);
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        CustomRequestPermission as = new CustomRequestPermission(activity);
        Bitmap bitmap = layout.getDrawingCache();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                OutputStream os = null;
                ContentValues contentValues = new ContentValues();

                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, title);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, dir);
                contentValues.put(MediaStore.Video.Media.IS_PENDING, 1);

                ContentResolver contentResolver = activity.getContentResolver();

                uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                os = contentResolver.openOutputStream(uri);

                bitmap.compress(Bitmap.CompressFormat.PNG, 0, os);

                contentValues.clear();
                contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
                contentResolver.update(uri, contentValues, null, null);
            } else {
                if (as.checkPermission()) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir, title + ".png");
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
                    file.setReadable(true, false);
                    fos.flush();
                    fos.close();

                    uri = Uri.fromFile(file);
                }
            }
            if(uri != null) {
                Toast.makeText(activity, "Rincian berhasil disimpan", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/png");

                activity.startActivity(Intent.createChooser(intent, ""));
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Rincian gagal disimpan, silahkan coba lagi, " + e, Toast.LENGTH_SHORT).show();
        }
        layout.setDrawingCacheEnabled(false);
        layout.setBackgroundColor(0x00000000);
    }

    public static void activityKiller(NavController nav, Activity activity) {
        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                activity.finish();
            }
        });
    }
}
