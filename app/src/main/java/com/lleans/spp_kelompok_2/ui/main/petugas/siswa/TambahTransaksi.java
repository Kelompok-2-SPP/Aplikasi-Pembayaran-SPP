package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas5TambahTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahTransaksi extends Fragment implements UIListener {

    private Petugas5TambahTransaksiBinding binding;
    private SessionManager sessionManager;
    private SiswaSharedModel sharedModel;
    private NavController nav;

    private List<SpinnerInterface> sppList;
    private int idSpp;
    private String nisn;
    private Long tglPembayaran, sppDate;
    private DetailsItemSpp sppData;

    public TambahTransaksi() {
        // Required empty public constructor
    }

    private void sppSpinner(String nisn) {
        isLoading(true);
        Call<SppDataList> sppDataListCall;
        Call<SppData> spplatestCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sppDataListCall = apiInterface.getSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null);
        spplatestCall = apiInterface.getLatestSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                nisn,
                Utils.getCurrentDateAndTime("yyyy"));
        spplatestCall.enqueue(new Callback<SppData>() {
            @Override
            public void onResponse(Call<SppData> call, Response<SppData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    idSpp = response.body().getDetails().getIdSpp();
                } else {
                    try {
                        SppDataList message = new Gson().fromJson(response.errorBody().charStream(), SppDataList.class);
                        toaster(message.getMessage());
                    } catch (Exception e) {
                        try {
                            dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SppData> call, Throwable t) {

            }
        });
        sppDataListCall.enqueue(new Callback<SppDataList>() {
            @Override
            public void onResponse(Call<SppDataList> call, Response<SppDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    sppList = new ArrayList<>();
                    int located = 0;

                    for (int x = 0; x < response.body().getDetails().size(); x++) {
                        SpinnerInterface a = new SpinnerInterface();
                        a.setName("Angkatan " + response.body().getDetails().get(x).getAngkatan() + "-Tahun " + response.body().getDetails().get(x).getTahun() + "-" + Utils.formatRupiah(response.body().getDetails().get(x).getNominal()));
                        a.setValue(response.body().getDetails().get(x).getIdSpp());
                        if (response.body().getDetails().get(x).getIdSpp() == idSpp) {
                            located = x;
                        }
                        sppList.add(a);
                    }

                    SpinnerAdapter adapter = new SpinnerAdapter(getContext(), sppList, true);
                    binding.spp.setAdapter(adapter);
                    sppData = response.body().getDetails().get(located);
                    binding.jumlahBayar.addTextChangedListener(new MoneyTextWatcher(binding.jumlahBayar, sppData.getNominal()));
                    binding.jumlahBayar.setText("0");
                    binding.spp.setSelection(located);
                } else {
                    try {
                        SppDataList message = new Gson().fromJson(response.errorBody().charStream(), SppDataList.class);
                        toaster(message.getMessage());
                    } catch (Exception e) {
                        try {
                            dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<SppDataList> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    private void postPembayaran(Long jumlahBayar) {
        isLoading(true);
        Call<PembayaranData> postPembayaranCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        postPembayaranCall = apiInterface.postPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                nisn,
                Utils.parseDateLongToServerString(this.tglPembayaran, "yyyy-MM-dd"),
                Integer.valueOf(Utils.parseDateLongToServerString(this.sppDate, "MM")),
                Integer.valueOf(Utils.parseDateLongToServerString(this.sppDate, "yyyy")),
                idSpp,
                jumlahBayar
        );
        postPembayaranCall.enqueue(new Callback<PembayaranData>() {
            @Override
            public void onResponse(Call<PembayaranData> call, Response<PembayaranData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    nav.navigateUp();
                } else if (response.code() <= 500) {
                    PembayaranData message = new Gson().fromJson(response.errorBody().charStream(), PembayaranData.class);
                    toaster(message.getMessage());
                } else {
                    try {
                        dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<PembayaranData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    private void datePicker() {
        MaterialDatePicker<Long> mat = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih Tgl. Bayar").setSelection(this.tglPembayaran).build();
        mat.show(getChildFragmentManager(), "TAG");
        mat.addOnPositiveButtonClickListener(selection -> {
            this.tglPembayaran = selection;
            binding.tglBayar.setText(Utils.formatDateStringToLocal(Utils.parseDateLongToString(this.tglPembayaran)));
        });
    }

    private void monthPicker() {
        MaterialDatePicker<Long> mat = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih Tahun Bulan SPP").setSelection(this.sppDate).build();
        mat.show(getChildFragmentManager(), "TAG");
        mat.addOnPositiveButtonClickListener(selection -> {
            this.sppDate = selection;
            binding.sppBulanTahun.setText(Utils.formatYearMonthStringToLocal(Utils.parseDateLongToServerString(this.sppDate, "MM-yyyy")));
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.tglBayar.setOnClickListener(v -> {
            datePicker();
        });

        binding.sppBulanTahun.setOnClickListener(v -> {
            monthPicker();
        });

        binding.simpan.setOnClickListener(v -> {
            idSpp = sppList.get(binding.spp.getSelectedItemPosition()).getValue();

            if (idSpp != 0 && tglPembayaran != null && sppDate != null) {
                postPembayaran(Utils.unformatRupiah(binding.jumlahBayar.getText().toString()));
            } else {
                toaster("Data harus diisi!");
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas5TambahTransaksiBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        sharedModel = new ViewModelProvider(requireActivity()).get(SiswaSharedModel.class);
        this.tglPembayaran = Calendar.getInstance().getTimeInMillis();
        this.sppDate = Calendar.getInstance().getTimeInMillis();

        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemSiswa -> {
            this.nisn = detailsItemSiswa.getNisn();
            sppSpinner(this.nisn);
        });

        binding.tglBayar.setText(Utils.formatDateStringToLocal(Utils.parseDateLongToString(this.tglPembayaran)));
        binding.sppBulanTahun.setText(Utils.formatYearMonthStringToLocal(Utils.parseDateLongToServerString(this.sppDate, "MM-yyyy")));
        return binding.getRoot();
    }

    // Abstract class for loadingBar
    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setEnabled(isLoading);
        binding.refresher.setRefreshing(isLoading);
    }

    // Abstract class for Toast
    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(getContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}