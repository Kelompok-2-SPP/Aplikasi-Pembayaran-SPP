package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas2SppBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
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

public class Spp extends Fragment {

    private Petugas2SppBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private SppCardAdapter cardAdapter;
    private int year, month;

    public Spp() {
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

    private void UILimiter() {
        binding.add.setVisibility(View.GONE);
    }

    private void setAdapter(List<SppData> data) {
        cardAdapter = new SppCardAdapter(data, controller, false, this);
        notFoundHandling(cardAdapter.getItemCount() == 0);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void getSpp() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<List<SppData>>> sppData;

        sppData = apiInterface.getSpp(
                null,
                null,
                null,
                null,
                null,
                null
        );
        sppData.enqueue(new Callback<BaseResponse<List<SppData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SppData>>> call, Response<BaseResponse<List<SppData>>> response) {
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
            public void onFailure(@NonNull Call<BaseResponse<List<SppData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void monthYearPicker() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), (selectedMonth, selectedYear) -> {
            if (cardAdapter != null) {
                this.year = selectedYear;
                binding.tgl.setText(String.valueOf(selectedYear));
                cardAdapter.getFilter().filter(String.valueOf(selectedYear));
            }
        }, this.year, this.month);
        builder.setTitle("Pilih Tahun SPP")
                .setActivatedYear(this.year)
                .setMaxYear(this.year)
                .showYearOnly()
                .build().show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.refresher.setOnRefreshListener(this::getSpp);
        binding.add.setOnClickListener(v -> controller.navigate(R.id.action_spp_petugas_to_tambahspp_petugas));
        binding.calendar.setOnClickListener(v -> monthYearPicker());
    }

    private void setupSpp() {
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.month = Calendar.getInstance().get(Calendar.MONTH);
        getSpp();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas2SppBinding.inflate(inflater, container, false);

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")) {
            UILimiter();
        }
        setupSpp();
        UtilsUI.simpleAnimation(binding.add);
        UtilsUI.simpleAnimation(binding.calendar);
        return binding.getRoot();
    }

}