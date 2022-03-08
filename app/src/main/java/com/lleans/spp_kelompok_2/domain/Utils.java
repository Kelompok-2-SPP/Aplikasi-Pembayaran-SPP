package com.lleans.spp_kelompok_2.domain;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.ui.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static void activityKiller(NavController nav, Activity activity) {
        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                activity.finish();
            }
        });
    }

    public static void exportToPNG(Context context, ConstraintLayout layout, String title) throws IOException {
        Uri uri = null;
        String dir = Environment.DIRECTORY_PICTURES;

        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = layout.getDrawingCache();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                OutputStream os = null;
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
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir, title + ".png");
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
                file.setReadable(true, false);
                fos.flush();
                fos.close();

                uri = Uri.fromFile(file);
            }
            Toast.makeText(context, "Rincian berhasil disimpan", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/png");

            context.startActivity(Intent.createChooser(intent, ""));
        } catch (Exception e) {
            Toast.makeText(context, "Rincian gagal disimpan " + e, Toast.LENGTH_SHORT).show();
        }
        layout.setDrawingCacheEnabled(false);
    }

    public static void nicknameBuilder(Context context, String name, TextView text, FrameLayout frame) {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);

        StringBuilder initials = new StringBuilder();

        for (String s : name.split(" ")) {
            initials.append(s.charAt(0));
        }

        text.setText(initials.toString());
        frame.setBackgroundTintList(ColorStateList.valueOf(androidColors[new Random(name.hashCode()).nextInt(androidColors.length)]));
    }

    public static String formatRupiah(long money) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        rupiahFormat.setMaximumFractionDigits(0);
        rupiahFormat.setRoundingMode(RoundingMode.FLOOR);
        return rupiahFormat.format(new BigDecimal(money));
    }

    public static long unformatRupiah(String money) {
        long parsed = 0;

        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        try {
            parsed = Long.parseLong(String.valueOf(new BigDecimal(String.valueOf(rupiahFormat.parse(money)))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsed;
    }

    public static Boolean statusPembayaran(long totalSpp, long nominalBayar) {
        return nominalBayar < totalSpp;
    }

    public static String getCurrentDateAndTime(String format) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, new Locale("id", "ID"));
        return df.format(c);
    }

    public static String kurangBayar(long totalSpp, long nominalBayar) {
        return "-" + formatRupiah(totalSpp - nominalBayar);
    }

    public static String formatDateStringToLocal(String date) {
        String arr[] = date.split("-");
        return arr[2] + " " + getMonth(Integer.parseInt(arr[1])) + " " + arr[0];
    }

    public static Long convertServerString(String sd) {
        Long parsed = null;

        String stringDate = sd.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", new Locale("id", "ID"));
        try {
            parsed = sdf.parse(stringDate).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsed;
    }

    public static String getMonth(int month) {
        String[] months = {"none", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember"};
        return months[month];
    }
}
