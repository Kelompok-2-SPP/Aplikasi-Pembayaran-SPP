package com.lleans.spp_kelompok_2.domain;

import android.app.Activity;

import androidx.navigation.NavController;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.ui.MainActivity;

public class Utils {

    public static void activityKiller(NavController nav, Activity activity) {
        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                MainActivity.act.finish();
                activity.finish();
            }
        });
    }

    public static Boolean statusPembayaran(int totalSpp, int nominalBayar) {
        return nominalBayar < totalSpp;
    }

    public static int kurangBayara(int totalSpp, int nominalBayar){
        return totalSpp - nominalBayar;
    }

    public static String getMonth(int month) {
        String[] months = {"none", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember"};
        return months[month];
    }
}
