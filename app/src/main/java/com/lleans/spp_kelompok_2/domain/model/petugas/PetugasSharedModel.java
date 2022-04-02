package com.lleans.spp_kelompok_2.domain.model.petugas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PetugasSharedModel extends ViewModel {
    private final MutableLiveData<PetugasData> data = new MutableLiveData<PetugasData>();

    public void updateData(PetugasData data) {
        this.data.setValue(data);
    }

    public LiveData<PetugasData> getData() {
        return this.data;
    }

}
