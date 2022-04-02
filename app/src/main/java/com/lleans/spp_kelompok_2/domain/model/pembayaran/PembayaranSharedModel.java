package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class PembayaranSharedModel extends ViewModel {
    private final MutableLiveData<PembayaranData> data = new MutableLiveData<PembayaranData>();

    public void updateData(PembayaranData data) {
        this.data.setValue(data);
    }

    public LiveData<PembayaranData> getData() {
        return this.data;
    }
}
