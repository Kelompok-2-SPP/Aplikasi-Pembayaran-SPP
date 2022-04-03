package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditSiswaBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSiswa extends Fragment {

    private Petugas4EditSiswaBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private List<SpinnerInterface> kelasList;
    private SiswaSharedModel sharedModel;
    private String nisn;

    public EditSiswa() {
        // Required empty public constructor
    }

    private void getBack(SiswaData data, boolean isDeleted) {
        if (isDeleted) {
            controller.popBackStack(R.id.editSiswa, true);
            controller.popBackStack(R.id.statussiswa_petugas, true);
            controller.popBackStack(R.id.siswa_petugas, false);
        } else {
            sharedModel.updateData(data);
            controller.navigateUp();
        }
    }

    private void setSpinnerAdapter(List<KelasData> data, int idKelas) {
        kelasList = new ArrayList<>();
        int located = 0;

        for (int x = 0; x < data.size(); x++) {
            SpinnerInterface a = new SpinnerInterface();
            a.setName("Kelas " + data.get(x).getNamaKelas() + "-Jurusan " + data.get(x).getJurusan() + "-Angkatan " + data.get(x).getAngkatan());
            a.setValue(data.get(x).getIdKelas());
            if (data.get(x).getIdKelas() == idKelas) {
                located = x;
            }
            kelasList.add(a);
        }

        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), kelasList, true);
        binding.kelas.setAdapter(adapter);
        binding.kelas.setSelection(located);
    }

    private void kelasSpinner(int idKelas) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<List<KelasData>>> kelasDataCall;

        kelasDataCall = apiInterface.getKelas(
                null,
                null,
                null,
                null,
                null,
                null);
        kelasDataCall.enqueue(new Callback<BaseResponse<List<KelasData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<KelasData>>> call, Response<BaseResponse<List<KelasData>>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    setSpinnerAdapter(response.body().getDetails(), idKelas);
                } else {
                    try {
                        BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(getContext(), message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false).show();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<KelasData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void deleteSiswa() {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SiswaData>> siswaDataCall;

        siswaDataCall = apiInterface.deleteSiswa(nisn);
        siswaDataCall.enqueue(new Callback<BaseResponse<SiswaData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SiswaData>> call, Response<BaseResponse<SiswaData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
                    getBack(response.body().getDetails(), true);
                } else {
                    try {
                        BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(getContext(), message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false).show();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<SiswaData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }


    private void editSiswa(String nisn, String newNisn, String nis, String password, String nama, Integer idKelas, String alamat, String noTelp) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SiswaData>> editSiswaCall;

        editSiswaCall = apiInterface.putSiswa(
                nisn,
                newNisn,
                nis,
                password,
                nama,
                idKelas,
                alamat,
                noTelp);
        editSiswaCall.enqueue(new Callback<BaseResponse<SiswaData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SiswaData>> call, Response<BaseResponse<SiswaData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
                    getBack(response.body().getDetails(), false);
                } else if (response.code() <= 500) {
                    BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                    UtilsUI.toaster(getContext(), message.getMessage());
                } else {
                    try {
                        UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<SiswaData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void diagSimpan() {
        String newNisn, nis, password, nama, alamat, noTelp;
        int idKelas;

        newNisn = binding.nisn.getText().toString();
        nis = binding.nis.getText().toString();
        password = binding.password.getText().toString();
        nama = binding.nama.getText().toString();
        alamat = binding.alamat.getText().toString();
        noTelp = binding.noTelp.getText().toString();
        idKelas = kelasList.get(binding.kelas.getSelectedItemPosition()).getValue();
        if (newNisn.isEmpty() || nis.isEmpty() || nama.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() || idKelas == 0) {
            UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
        } else {
            UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                if (password.isEmpty()) {
                    editSiswa(nisn, newNisn, nis, null, nama, idKelas, alamat, noTelp);
                } else {
                    editSiswa(nisn, newNisn, nis, password, nama, idKelas, alamat, noTelp);
                }
            }).show();
        }
    }

    private void diagHapus() {
        UtilsUI.dialog(getContext(), "Hapus data?", "Apakah anda yakin untuk menghapus data berikut, data transaksi di dalam siswa ini akan terhapus.", true).setPositiveButton("Ok", (dialog, which) -> {
            deleteSiswa();
        }).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.simpan.setOnClickListener(view1 -> diagSimpan());
        binding.hapus.setOnClickListener(view2 -> diagHapus());
    }

    private void getSharedModel() {
        sharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSiswa -> {
            this.nisn = detailsItemSiswa.getNisn();
            kelasSpinner(detailsItemSiswa.getIdKelas());
            binding.nisn.setText(detailsItemSiswa.getNisn());
            binding.nis.setText(detailsItemSiswa.getNis());
            binding.nama.setText(detailsItemSiswa.getNama());
            binding.alamat.setText(detailsItemSiswa.getAlamat());
            binding.noTelp.setText(detailsItemSiswa.getNoTelp());
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas4EditSiswaBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        getSharedModel();
        return binding.getRoot();
    }

}