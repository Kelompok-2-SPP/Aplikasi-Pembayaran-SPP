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

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.DateSelector;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.spp.SppDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTransaksi extends Fragment implements UIListener {

    private Petugas4EditTransaksiBinding binding;
    private SessionManager sessionManager;
    private PembayaranSharedModel sharedModel;
    private NavController nav;

    private List<SpinnerInterface> sppList;
    private int idPembayaran, idSpp;
    private Long tglPembayaran, sppDate;

    public EditTransaksi() {
        // Required empty public constructor
    }

    private void sppSpinner(int idSpp) {
        isLoading(true);
        Call<SppDataList> sppDataListCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sppDataListCall = apiInterface.getSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null);
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

    // Delete pembayaran function
    private void deletePembayaran() {
        isLoading(true);
        Call<PembayaranData> pembayaranDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pembayaranDataCall = apiInterface.deletePembayaram(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idPembayaran
                );
        pembayaranDataCall.enqueue(new Callback<PembayaranData>() {
            @Override
            public void onResponse(Call<PembayaranData> call, Response<PembayaranData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    sharedModel.updateData(response.body().getDetails());
                    nav.navigate(R.id.action_editStatus_to_statussiswa_petugas);
                } else {
                    try {
                        PembayaranData message = new Gson().fromJson(response.errorBody().charStream(), PembayaranData.class);
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
            public void onFailure(@NonNull Call<PembayaranData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }


    private void editPembayaran(Long jumlahBayar) {
        isLoading(true);
        Call<PembayaranData> editPembayaranCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editPembayaranCall = apiInterface.putPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idPembayaran,
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                null,
                Utils.parseDateLongToServerString(this.tglPembayaran, "yyyy-MM-dd"),
                Integer.valueOf(Utils.parseDateLongToServerString(this.sppDate, "MM")),
                Integer.valueOf(Utils.parseDateLongToServerString(this.sppDate, "yyyy")),
                idSpp,
                jumlahBayar
        );
        editPembayaranCall.enqueue(new Callback<PembayaranData>() {
            @Override
            public void onResponse(Call<PembayaranData> call, Response<PembayaranData> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    toaster(response.body().getMessage());
                    sharedModel.updateData(response.body().getDetails());
                    nav.navigateUp();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = Navigation.findNavController(view);

        binding.tglBayar.setOnClickListener(v -> {
            datePicker();
        });

        binding.simpan.setOnClickListener(v -> {
            idSpp = sppList.get(binding.spp.getSelectedItemPosition()).getValue();

            if (idSpp != 0 && tglPembayaran != null && sppDate != null) {
                editPembayaran(Utils.unformatRupiah(binding.jumlahBayar.getText().toString()));
            } else {
                toaster("Data harus diisi!");
            }
        });

        binding.hapus.setOnClickListener(v -> {
            if (idPembayaran != 0) {
                deletePembayaran();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas4EditTransaksiBinding.inflate(inflater, container, false);
        isLoading(false);
        sessionManager = new SessionManager(getContext());
        sharedModel = new ViewModelProvider(requireActivity()).get(PembayaranSharedModel.class);

        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPembayaran -> {
            this.idPembayaran = detailsItemPembayaran.getIdPembayaran();
            if(detailsItemPembayaran.getTglBayar() != null) {
                this.tglPembayaran = Utils.parseDateStringToLong(detailsItemPembayaran.getTglBayar());
                binding.tglBayar.setText(Utils.formatDateStringToLocal(Utils.parseDateLongToString(Utils.parseDateStringToLong(detailsItemPembayaran.getTglBayar()))));
            }
            this.sppDate = Utils.parseDateStringToLong(detailsItemPembayaran.getTahunSpp() + "-" + detailsItemPembayaran.getBulanSpp() + "-01");
            this.idSpp = detailsItemPembayaran.getIdSpp();

            binding.jumlahBayar.addTextChangedListener(new MoneyTextWatcher(binding.jumlahBayar, detailsItemPembayaran.getSpp().getNominal()));
            binding.sppTahunBulan.setText(Utils.formatDateStringToLocal("-" + detailsItemPembayaran.getBulanSpp() + "-" + detailsItemPembayaran.getTahunSpp()));
            binding.jumlahBayar.setText(String.valueOf(detailsItemPembayaran.getJumlahBayar()));
            sppSpinner(detailsItemPembayaran.getIdSpp());
        });

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
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    // Abstract class for Dialog
    @Override
    public void dialog(String title, Spanned message) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(requireContext());
        as.setTitle(title).setMessage(message).setPositiveButton("Ok", null).show();
    }
}