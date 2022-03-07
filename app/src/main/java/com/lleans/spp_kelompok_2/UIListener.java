package com.lleans.spp_kelompok_2;

import android.text.Spanned;

public interface UIListener {

    void isLoading(Boolean isLoading);

    void toaster(String text);

    void dialog(String title, Spanned message);
}
