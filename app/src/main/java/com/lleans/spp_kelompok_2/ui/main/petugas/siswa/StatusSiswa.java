package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas3StatusSiswaBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.domain.model.tunggakan.TunggakanData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusSiswa extends Fragment {

    private Petugas3StatusSiswaBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private StatusSiswaCardAdapter cardAdapter;
    private String nisn;

    public StatusSiswa() {
        // Required empty public constructor
    }

    public void notFoundHandling(boolean check) {
        if (check) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.add.setVisibility(View.GONE);
            binding.notFound.getRoot().setVisibility(View.VISIBLE);
            UtilsUI.simpleAnimation(binding.notFound.getRoot());
        } else {
            binding.notFound.getRoot().setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void UILimiter() {
        binding.edit.setVisibility(View.GONE);
    }

    private void setAdapter(List<PembayaranData> data) {
        cardAdapter = new StatusSiswaCardAdapter(data, controller, apiInterface, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void showAddTransaksi(boolean isNone) {
        if (isNone) {
            UtilsUI.simpleAnimation(binding.add);
            binding.add.setVisibility(View.VISIBLE);
        }
    }

    private void getTunggakan() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<TunggakanData>> tunggakanDataCall;

        tunggakanDataCall = apiInterface.getTunggakan(nisn);
        tunggakanDataCall.enqueue(new Callback<BaseResponse<TunggakanData>>() {
            @Override
            public void onResponse(Call<BaseResponse<TunggakanData>> call, Response<BaseResponse<TunggakanData>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    getTransaksi();
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
            public void onFailure(@NonNull Call<BaseResponse<TunggakanData>> call, @NonNull Throwable t) {
                getTransaksi();
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void getTransaksi() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<List<PembayaranData>>> pembayaranDataCall;

        pembayaranDataCall = apiInterface.getPembayaran(
                null,
                null,
                nisn,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        pembayaranDataCall.enqueue(new Callback<BaseResponse<List<PembayaranData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PembayaranData>>> call, Response<BaseResponse<List<PembayaranData>>> response) {
                UtilsUI.isLoading(binding.refresher, true, false);
                if (response.body() != null && response.isSuccessful()) {
                    setAdapter(response.body().getDetails());
                } else {
                    if (response.code() == 404) {
                        UtilsUI.toaster(getContext(), "Transaksi tidak ditemukan, silahkan buat transaksi baru");
                        showAddTransaksi(true);
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
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<PembayaranData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void datePicker() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), (selectedMonth, selectedYear) -> {
            if (cardAdapter != null) {
                binding.tgl.setText(String.valueOf(selectedYear));
                cardAdapter.getFilter().filter(String.valueOf(selectedYear));
            }
        }, year, month);
        builder.setTitle("Pilih Tahun SPP")
                .setActivatedYear(year)
                .setMaxYear(year)
                .showYearOnly()
                .build().show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.refresher.setOnRefreshListener(this::getTransaksi);
        binding.edit.setOnClickListener(v -> controller.navigate(R.id.action_statussiswa_petugas_to_editSiswa));
        binding.add.setOnClickListener(v -> controller.navigate(R.id.action_statussiswa_petugas_to_tambahStatus));
        binding.calendar.setOnClickListener(v -> datePicker());
    }

    private void getSharedModel() {
        SiswaSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSiswa -> {
            this.nisn = detailsItemSiswa.getNisn();
            UtilsUI.nicknameBuilder(getActivity().getApplicationContext(), detailsItemSiswa.getNama(), binding.nick, binding.nickFrame);
            binding.nama.setText(detailsItemSiswa.getNama());
            binding.nisn.setText(String.valueOf(detailsItemSiswa.getNisn()));
            binding.nis.setText(detailsItemSiswa.getNis());
            binding.alamat.setText(detailsItemSiswa.getAlamat());
            binding.noTelp.setText(String.valueOf(detailsItemSiswa.getNoTelp()));
            getTunggakan();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = Petugas3StatusSiswaBinding.inflate(inflater, container, false);

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas"))
            UILimiter();
        getSharedModel();
        UtilsUI.simpleAnimation(binding.calendar);
        return binding.getRoot();
    }

}