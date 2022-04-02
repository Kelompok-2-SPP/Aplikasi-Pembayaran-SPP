package com.lleans.spp_kelompok_2.domain;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Utils {

    final static Locale local = new Locale("in", "ID");
    final static NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(local);

    public static String formatRupiah(long money) {
        rupiahFormat.setMaximumFractionDigits(0);
        rupiahFormat.setRoundingMode(RoundingMode.FLOOR);
        return rupiahFormat.format(new BigDecimal(money));
    }

    public static Long unformatRupiah(String money) {
        try {
            return (Long) rupiahFormat.parse(money);
        } catch (ParseException e) {
            Log.e("Debug", e.getMessage(), e);
        }
        return 0L;
    }

    public static String kurangBayar(Long totalSpp, Long nominalBayar) {
        if (nominalBayar == null) nominalBayar = 0L;
        return ("-" + formatRupiah(totalSpp - nominalBayar));
    }

    public static Boolean statusPembayaran(Long totalSpp, Long nominalBayar) {
        if (nominalBayar == null) nominalBayar = 0L;
        return nominalBayar < totalSpp;
    }

    public static String getCurrentDateAndTime(String format) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, local);
        return df.format(c);
    }

    public static Long parseServerStringtoLongDate(String date, String format) {
        String clean = date.replaceAll("T", " ").replaceAll(".000Z", "");
        SimpleDateFormat sdf = new SimpleDateFormat(format, local);

        try {
            Date d = sdf.parse(clean);
            return Objects.requireNonNull(d).getTime();
        } catch (ParseException e) {
            Log.e("Debug", e.getMessage(), e);
        }
        return 0L;
    }

    public static String parseLongtoStringDate(Long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, local);

        try {
            return sdf.format(date);
        } catch (Exception e) {
            Log.e("Debug", e.getMessage(), e);
        }
        return "";
    }
}
