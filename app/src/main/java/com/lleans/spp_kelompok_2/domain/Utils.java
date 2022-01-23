package com.lleans.spp_kelompok_2.domain;

import java.util.HashMap;

public class Utils {

    public static boolean isNumeric(String string) {

        if (string == null || string.equals("")) {
            return false;
        }

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void statusPembayaran(int totalSpp, int nominalBayar){
        if(nominalBayar < totalSpp){
            int sisaBayar = totalSpp-nominalBayar;

            return ;
        }
    }

    public static String getMonth(int month) {
        String[] months = {"none", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember"};
        return months[month];
    }
}
