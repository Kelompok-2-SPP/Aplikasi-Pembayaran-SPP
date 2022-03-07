package com.lleans.spp_kelompok_2.domain.model.spp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SppSharedModel extends ViewModel {
    private final MutableLiveData<DetailsItemSpp> data = new MutableLiveData<DetailsItemSpp>();

    public void updateData(DetailsItemSpp data) {
        this.data.setValue(data);
    }

    public LiveData<DetailsItemSpp> getData() {
        return this.data;
    }
}
