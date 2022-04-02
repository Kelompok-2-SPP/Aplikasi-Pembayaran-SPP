package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Petugas2HistoriBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
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

public class Histori extends Fragment {

    private Petugas2HistoriBinding binding;
    private SessionManager sessionManager;
    private NavController controller;
    private ApiInterface apiInterface;

    private HistoriCardAdapter cardAdapter;

    public Histori() {
        // Required empty public constructor
    }

    private void notFoundHandling(boolean check) {
        if (check) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.notFound.getRoot().setVisibility(View.VISIBLE);
            UtilsUI.simpleAnimation(binding.notFound.getRoot());
        }
    }

    private void setAdapter(List<PembayaranData> data) {
        cardAdapter = new HistoriCardAdapter(data, controller);
        notFoundHandling(cardAdapter.getItemCount() == 0);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void getHistori() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<List<PembayaranData>>> historiDataCall;

        historiDataCall = apiInterface.getPembayaran(
                null,
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        historiDataCall.enqueue(new Callback<BaseResponse<List<PembayaranData>>>() {
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
        controller = Navigation.findNavController(view);

        binding.refresher.setOnRefreshListener(this::getHistori);
        binding.calendar.setOnClickListener(v -> {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH);

            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), (selectedMonth, selectedYear) -> {
                if (cardAdapter != null) {
                    binding.tgl.setText(Utils.parseLongtoStringDate(Utils.parseServerStringtoLongDate(selectedYear + "-" + (selectedMonth + 1), "yyyy-MM"), "MMMM yyyy"));
                    cardAdapter.getFilter().filter(String.valueOf(Utils.parseServerStringtoLongDate(selectedYear + "-" + (selectedMonth + 1), "yyyy-MM")));
                    notFoundHandling(cardAdapter.getItemCount() == 0);
                }
            }, year, month);
            builder.setActivatedMonth(month)
                    .setTitle("Pilih Bulan dan Tahun")
                    .setMinYear(year - 1)
                    .setMaxYear(year)
                    .setActivatedYear(year)
                    .build().show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas2HistoriBinding.inflate(inflater, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        getHistori();
        UtilsUI.simpleAnimation(binding.calendar);
        return binding.getRoot();
    }

}