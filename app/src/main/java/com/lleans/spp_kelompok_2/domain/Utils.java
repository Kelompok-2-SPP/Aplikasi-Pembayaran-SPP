package com.lleans.spp_kelompok_2.domain;

import android.app.Activity;

import androidx.navigation.NavController;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.ui.MainActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static void activityKiller(NavController nav, Activity activity) {
        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                MainActivity.act.finish();
                activity.finish();
            }
        });
    }

    public static String formatRupiah(int money) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return rupiahFormat.format(Double.valueOf(money));
    }

    public static Boolean statusPembayaran(int totalSpp, int nominalBayar) {
        return nominalBayar < totalSpp;
    }

    public static String getCurrentDateAndTime(String format) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, new Locale("id", "ID"));
        return df.format(c);
    }

    public static String kurangBayar(int totalSpp, int nominalBayar) {
        return "- " + formatRupiah(totalSpp - nominalBayar);
    }

    public static Long convertServerString(String sd){
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
