package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class PembayaranSharedModel extends ViewModel {
    private final MutableLiveData<DetailsItemPembayaran> data = new MutableLiveData<DetailsItemPembayaran>();

    public void updateData(DetailsItemPembayaran data) {
        this.data.setValue(data);
    }

    public LiveData<DetailsItemPembayaran> getData() {
        return this.data;
    }
}
