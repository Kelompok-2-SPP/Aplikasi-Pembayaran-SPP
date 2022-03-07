package com.lleans.spp_kelompok_2.domain.model.kelas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KelasSharedModel extends ViewModel {
    private final MutableLiveData<DetailsItemKelas> data = new MutableLiveData<DetailsItemKelas>();

    public void updateData(DetailsItemKelas data) {
        this.data.setValue(data);
    }

    public LiveData<DetailsItemKelas> getData() {
        return this.data;
    }
}
