package com.lleans.spp_kelompok_2.domain;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.navigation.NavController;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.ui.MainActivity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static void activityKiller(NavController nav, Activity activity) {
        MainActivity.act.finish();
        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                activity.finish();
            }
        });
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
        return rupiahFormat.format(Double.valueOf(money));
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
