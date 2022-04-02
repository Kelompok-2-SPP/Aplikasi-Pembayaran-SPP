package com.lleans.spp_kelompok_2.ui.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class UtilsUI {

    public static void toaster(Context context, String text) {
        Toast toast = new Toast(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);

        TextView txt = customView.findViewById(R.id.toastMessage);
        txt.setText(text);

        toast.setView(customView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void simpleAnimation(View view) {
        AnimationSet anim = new AnimationSet(true);
        AlphaAnimation ad = new AlphaAnimation(0.0f, 1.0f);
        ad.setDuration(450);
        TranslateAnimation as = new TranslateAnimation(0, 0, 200, 0);
        as.setDuration(250);

        anim.addAnimation(ad);
        anim.addAnimation(as);
        view.startAnimation(anim);
    }

    public static void isLoading(SwipeRefreshLayout swp, boolean isActivated, boolean isLoading) {
        swp.setEnabled(isActivated);
        swp.setRefreshing(isLoading);
    }

    public static MaterialAlertDialogBuilder dialog(Context context, String title, String desc, boolean isConfirm) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(context);
        as.setTitle(title)
                .setMessage(Html.fromHtml(desc))
                .setPositiveButton("Ok", null);
        if (isConfirm) as.setNegativeButton("Cancel", null);
        return as;
    }

    public static void nicknameBuilder(Context context, String name, TextView text, FrameLayout frame) {
        Resources res = context.getResources();
        int[] androidColors = {
                res.getColor(R.color.red),
                res.getColor(R.color.orange),
                res.getColor(R.color.purple),
                res.getColor(R.color.green)
        };

        StringBuilder initials = new StringBuilder();

        for (String s : name.split(" ")) {
            initials.append(s.charAt(0));
        }

        text.setText(initials.toString());
        frame.setBackgroundTintList(ColorStateList.valueOf(androidColors[new Random(name.hashCode()).nextInt(androidColors.length)]));
    }

    public static void exportToPNG(Context context, ConstraintLayout layout, String title) throws IOException {
        Uri uri = null;
        String dir = Environment.DIRECTORY_PICTURES;
        int grey = context.getResources().getColor(R.color.neutral_lightGrey);

        layout.setBackgroundColor(grey);
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        CustomRequestPermission as = new CustomRequestPermission(context);
        Bitmap bitmap = layout.getDrawingCache();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                OutputStream os;
                ContentValues contentValues = new ContentValues();

                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, title);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, dir);
                contentValues.put(MediaStore.Video.Media.IS_PENDING, 1);

                ContentResolver contentResolver = context.getContentResolver();

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
                    file.setReadable(true, true);
                    fos.flush();
                    fos.close();

                    uri = Uri.fromFile(file);
                }
            }
            if (uri != null) {
                toaster(context, "Rincian berhasil disimpan");

                Intent intent = new Intent(Intent.ACTION_SEND)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Intent.EXTRA_STREAM, uri)
                        .setType("image/png");

                context.startActivity(Intent.createChooser(intent, ""));
            }
        } catch (Exception e) {
            toaster(context, "Rincian gagal disimpan, silahkan coba lagi, " + e);
        }
        layout.setDrawingCacheEnabled(false);
        layout.setBackgroundColor(0x00000000);
    }

    public static void activityKiller(Activity activity, NavController nav) {
        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                activity.finish();
            }
        });
    }
}
