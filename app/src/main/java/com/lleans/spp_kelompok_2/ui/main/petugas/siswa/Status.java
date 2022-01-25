package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.StatusSiswaBinding;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.siswa.DetailsItemSiswa;

public class Status extends Fragment {

    private StatusSiswaBinding binding;

    private DetailsItemKelas kelas;
    private DetailsItemSiswa siswa;

    public Status() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController nav = Navigation.findNavController(view);
        Bundle data = new Bundle();
        data.putSerializable("kelas", kelas);
        data.putSerializable("siswa", siswa);
        binding.btnEdit.setOnClickListener(v -> nav.navigate(R.id.action_statussiswa_petugas_to_editSiswa, data));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = StatusSiswaBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        kelas = (DetailsItemKelas) bundle.getSerializable("kelas");
        siswa = (DetailsItemSiswa) bundle.getSerializable("siswa");
        return binding.getRoot();
    }
}