package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CachedPembayaranSharedModel extends ViewModel {
    private final MutableLiveData<List<PembayaranData>> data = new MutableLiveData<List<PembayaranData>>();

    public void updateData(List<PembayaranData> data) {
        this.data.setValue(data);
    }

    public LiveData<List<PembayaranData>> getData() {
        return this.data;
    }
}
