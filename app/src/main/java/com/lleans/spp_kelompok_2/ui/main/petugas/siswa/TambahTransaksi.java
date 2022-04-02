package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTransaksi extends Fragment {

    private Petugas5TambahTransaksiBinding binding;
    private SessionManager sessionManager;
    private NavController controller;
    private ApiInterface apiInterface;

    private List<SpinnerInterface> sppList;
    private String nisn;
    private Long tglPembayaran, sppDate;

    public TambahTransaksi() {
        // Required empty public constructor
    }

    private void setSpinnerAdapter(List<SppData> data, int idSpp) {
        sppList = new ArrayList<>();
        int located = 0;

        for (int x = 0; x < data.size(); x++) {
            SpinnerInterface a = new SpinnerInterface();
            a.setName("Angkatan " + data.get(x).getAngkatan() + "-Tahun " + data.get(x).getTahun() + "-" + Utils.formatRupiah(data.get(x).getNominal()));
            a.setValue(data.get(x).getIdSpp());
            if (data.get(x).getIdSpp() == idSpp) {
                located = x;
            }
            sppList.add(a);
        }

        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), sppList, true);
        binding.spp.setAdapter(adapter);
        binding.jumlahBayar.addTextChangedListener(new MoneyTextWatcher(binding.jumlahBayar, Long.MAX_VALUE));
        binding.spp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.jumlahBayar.setText(String.valueOf(data.get(position).getNominal()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (located != 0) binding.spp.setSelection(located);
    }

    private void latestSpp(String nisn) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<SppData>> spplatestCall;

        spplatestCall = apiInterface.getLatestSpp(
                nisn,
                Utils.getCurrentDateAndTime("yyyy"));
        spplatestCall.enqueue(new Callback<BaseResponse<SppData>>() {
            @Override
            public void onResponse(Call<BaseResponse<SppData>> call, Response<BaseResponse<SppData>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    sppSpinner(response.body().getDetails().getIdSpp());
                } else {
                    sppSpinner(0);
                    if (response.code() == 404) {
                        UtilsUI.toaster(getContext(), "Spp dengan angkatan yang sama tidak ditemukan, coba tambahkan spp dengan angkatan yang sama");
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
            public void onFailure(Call<BaseResponse<SppData>> call, Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void sppSpinner(int idSpp) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<List<SppData>>> sppDataListCall;

        sppDataListCall = apiInterface.getSpp(
                null,
                null,
                null,
                null,
                null,
                null);
        sppDataListCall.enqueue(new Callback<BaseResponse<List<SppData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SppData>>> call, Response<BaseResponse<List<SppData>>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    setSpinnerAdapter(response.body().getDetails(), idSpp);
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
            public void onFailure(@NonNull Call<BaseResponse<List<SppData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    private void tambahPembayaran(Long jumlahBayar, int idSpp) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<PembayaranData>> postPembayaranCall;

        postPembayaranCall = apiInterface.postPembayaran(
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                nisn,
                Utils.parseLongtoStringDate(this.tglPembayaran, "yyyy-MM-dd"),
                Integer.valueOf(Utils.parseLongtoStringDate(this.sppDate, "MM")),
                Integer.valueOf(Utils.parseLongtoStringDate(this.sppDate, "yyyy")),
                idSpp,
                jumlahBayar
        );
        postPembayaranCall.enqueue(new Callback<BaseResponse<PembayaranData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PembayaranData>> call, Response<BaseResponse<PembayaranData>> response) {
                UtilsUI.isLoading(binding.refresher, false, false);
                if (response.body() != null && response.isSuccessful()) {
                    UtilsUI.toaster(getContext(), response.body().getMessage());
                    controller.navigateUp();
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
            public void onFailure(@NonNull Call<BaseResponse<PembayaranData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.tglBayar.setOnClickListener(v -> {
            int date = Integer.parseInt(Utils.parseLongtoStringDate(this.tglPembayaran, "dd"));
            int month = Integer.parseInt(Utils.parseLongtoStringDate(this.tglPembayaran, "MM"));
            int year = Integer.parseInt(Utils.parseLongtoStringDate(this.tglPembayaran, "yyyy"));

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view12, year1, month1, dayOfMonth) -> {
                this.tglPembayaran = Utils.parseServerStringtoLongDate(year1 + "-" + month1 + "-" + dayOfMonth, "yyyy-MM-dd");
                binding.tglBayar.setText(Utils.parseLongtoStringDate(this.tglPembayaran, "dd MMMM yyyy"));
            }, year, month, date);
            datePickerDialog.setTitle("Pilih Tgl. Bayar");
            datePickerDialog.show();
        });
        binding.sppBulanTahun.setOnClickListener(v -> {
            int month = Integer.parseInt(Utils.parseLongtoStringDate(this.sppDate, "MM"));
            int year = Integer.parseInt(Utils.parseLongtoStringDate(this.sppDate, "yyyy"));

            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), (selectedMonth, selectedYear) -> {
                this.sppDate = Utils.parseServerStringtoLongDate(String.valueOf(selectedMonth), "MM");
                binding.sppBulanTahun.setText(Utils.parseLongtoStringDate(this.sppDate, "MMMM yyyy"));
            }, year, month);
            builder.setActivatedMonth(month)
                    .setTitle("Pilih Bulan dan Tahun SPP")
                    .setMinYear(year - 1)
                    .setMaxYear(year + 1)
                    .setActivatedYear(year)
                    .build().show();
        });
        binding.simpan.setOnClickListener(v -> {
            int idSpp = sppList.get(binding.spp.getSelectedItemPosition()).getValue();

            if (idSpp == 0 && tglPembayaran == null && sppDate == null) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, sistem tunggakan akan berjalan secara otomatis sesuai dengan transaksi yang ditambahkan, pastikan data diatas sudah benar!.", true).setPositiveButton("Ok", (dialog, which) -> {
                    tambahPembayaran(Utils.unformatRupiah(binding.jumlahBayar.getText().toString()), idSpp);
                }).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas5TambahTransaksiBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        SiswaSharedModel sharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);
        this.tglPembayaran = Calendar.getInstance().getTimeInMillis();
        this.sppDate = Calendar.getInstance().getTimeInMillis();
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSiswa -> {
            this.nisn = detailsItemSiswa.getNisn();
            latestSpp(nisn);
        });
        binding.tglBayar.setText(Utils.parseLongtoStringDate(this.tglPembayaran, "dd MMMM yyyy"));
        binding.sppBulanTahun.setText(Utils.parseLongtoStringDate(this.sppDate, "MMMM yyyy"));
        return binding.getRoot();
    }

}