package com.lleans.spp_kelompok_2.domain.model.siswa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SiswaSharedModel extends ViewModel {
    private final MutableLiveData<DetailsItemSiswa> data = new MutableLiveData<DetailsItemSiswa>();

    public void updateData(DetailsItemSiswa data) {
        this.data.setValue(data);
    }

    public LiveData<DetailsItemSiswa> getData() {
        return this.data;
    }
}
