package com.lleans.spp_kelompok_2.domain.model.kelas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KelasSharedModel extends ViewModel {
    private final MutableLiveData<KelasData> data = new MutableLiveData<KelasData>();

    public void updateData(KelasData data) {
        this.data.setValue(data);
    }

    public LiveData<KelasData> getData() {
        return this.data;
    }
}
