package com.lleans.spp_kelompok_2.domain.model.petugas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PetugasSharedModel extends ViewModel {
    private final MutableLiveData<DetailsItemPetugas> data = new MutableLiveData<DetailsItemPetugas>();

    public void updateData(DetailsItemPetugas data) {
        this.data.setValue(data);
    }

    public LiveData<DetailsItemPetugas> getData() {
        return this.data;
    }

}
