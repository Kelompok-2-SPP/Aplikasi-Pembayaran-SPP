package com.lleans.spp_kelompok_2.ui.utils.spinner;

import java.io.Serializable;

public class SpinnerInterface implements Serializable {

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
