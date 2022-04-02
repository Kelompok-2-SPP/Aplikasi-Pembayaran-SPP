package com.lleans.spp_kelompok_2.domain.model.siswa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SiswaSharedModel extends ViewModel {
    private final MutableLiveData<SiswaData> data = new MutableLiveData<SiswaData>();

    public void updateData(SiswaData data) {
        this.data.setValue(data);
    }

    public LiveData<SiswaData> getData() {
        return this.data;
    }
}
