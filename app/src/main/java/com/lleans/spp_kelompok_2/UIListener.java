package com.lleans.spp_kelompok_2;

public interface UIListener {

    void isLoading(Boolean isLoading);

    void toaster(String text);

    void dialog(String title, String message);
}
