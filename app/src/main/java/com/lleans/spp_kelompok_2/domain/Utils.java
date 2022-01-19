package com.lleans.spp_kelompok_2.domain;

import java.util.HashMap;

public class Utils {

    static boolean isNumeric(String string) {

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

    static void statusPembayaran(int totalSpp, int nominalBayar){
        if(nominalBayar < totalSpp){
            int sisaBayar = totalSpp-nominalBayar;

            return ;
        }
    }
}
