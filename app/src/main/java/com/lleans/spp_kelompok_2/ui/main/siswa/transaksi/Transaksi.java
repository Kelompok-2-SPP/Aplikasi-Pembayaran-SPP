package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

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

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Siswa2TransaksiBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.CachedPembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
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

public class Transaksi extends Fragment {

    private Siswa2TransaksiBinding binding;
    private SessionManager sessionManager;
    private NavController navController;
    private ApiInterface apiInterface;

    private TransaksiCardAdapter cardAdapter;

    public Transaksi() {
        // Required empty public constructor
    }

    public void notFoundHandling(boolean check) {
        if (check) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.notFound.getRoot().setVisibility(View.VISIBLE);
            UtilsUI.simpleAnimation(binding.notFound.getRoot());
        } else {
            binding.notFound.getRoot().setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(List<PembayaranData> data) {
        notFoundHandling(data.size() == 0 || data == null);
        cardAdapter = new TransaksiCardAdapter(data, navController, false, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void getTransaksi(String tglDibayar, Integer bulanDibayar, Integer tahun_dibayar) {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<List<PembayaranData>>> pembayaranDataCall;

        pembayaranDataCall = apiInterface.getPembayaran(
                null,
                null,
                sessionManager.getUserDetail().get(SessionManager.ID),
                tglDibayar, bulanDibayar, tahun_dibayar,
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
                        notFoundHandling(true);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.calendar.setOnClickListener(v -> {
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
        });
        binding.refresher.setOnRefreshListener(() -> {
            getTransaksi(null, null, null);
        });
    }

    private void isCached() {
        CachedPembayaranSharedModel cached = new ViewModelProvider(requireActivity()).get(CachedPembayaranSharedModel.class);
        cached.getData().observe(getViewLifecycleOwner(), pembayaranData -> {
            if (pembayaranData != null) {
                setAdapter(pembayaranData);
            } else {
                getTransaksi(null, null, null);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Siswa2TransaksiBinding.inflate(inflater, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        isCached();
        UtilsUI.simpleAnimation(binding.calendar);
        return binding.getRoot();
    }

}