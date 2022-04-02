package com.lleans.spp_kelompok_2.domain.model.spp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SppSharedModel extends ViewModel {
    private final MutableLiveData<SppData> data = new MutableLiveData<SppData>();

    public void updateData(SppData data) {
        this.data.setValue(data);
    }

    public LiveData<SppData> getData() {
        return this.data;
    }
}
