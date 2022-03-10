package com.lleans.spp_kelompok_2.domain;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String formatRupiah(long money) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        rupiahFormat.setMaximumFractionDigits(0);
        rupiahFormat.setRoundingMode(RoundingMode.FLOOR);
        return rupiahFormat.format(new BigDecimal(money));
    }

    public static long unformatRupiah(String money) {
        long parsed = 0;

        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
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
        SimpleDateFormat df = new SimpleDateFormat(format, new Locale("in", "ID"));
        return df.format(c);
    }

    public static Long parseDateStringToLong(String date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Long miliseconds = null;

        try {
            Date d = f.parse(date + "-07:00:00");
            miliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return miliseconds;
    }

    public static String parseDateLongToString(Long date) {
        Date dat = new Date(date);
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        return df2.format(dat);
    }

    public static String parseDateLongToServerString(Long date, String format) {
        Date dat = new Date(date);
        SimpleDateFormat df2 = new SimpleDateFormat(format);
        return df2.format(dat);
    }

    public static String kurangBayar(long totalSpp, long nominalBayar) {
        return "-" + formatRupiah(totalSpp - nominalBayar);
    }

    public static String formatDateStringToLocal(String date) {
        String[] arr = date.split("-");
        return arr[2] + " " + getMonth(Integer.parseInt(arr[1])) + " " + arr[0];
    }

    public static String formatYearMonthStringToLocal(String date) {
        String[] arr = date.split("-");
        return arr[1] + " " + getMonth(Integer.parseInt(arr[0]));
    }

    public static String getMonth(int month) {
        String[] months = {"none", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember"};
        return months[month];
    }
}
