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
import android.widget.DatePicker;

import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas4EditTransaksiBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerAdapter;
import com.lleans.spp_kelompok_2.ui.utils.spinner.SpinnerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTransaksi extends Fragment {

    private Petugas4EditTransaksiBinding binding;
    private SessionManager sessionManager;
    private NavController controller;
    private ApiInterface apiInterface;

    private List<SpinnerInterface> sppList;
    private PembayaranSharedModel sharedModel;
    private int idPembayaran;
    private Long tglPembayaran, sppDate;

    private void UILimiter(String type) {
        if (type.equals("petugas")) {
            binding.hapus.setVisibility(View.GONE);
        }
    }

    public EditTransaksi() {
        // Required empty public constructor
    }

    private void getBack(PembayaranData data, boolean isDeleted) {
        if (isDeleted) {
            controller.navigate(R.id.action_editStatus_to_statussiswa_petugas);
            controller.popBackStack(R.id.rincianTransaksi_siswa, true);
            controller.popBackStack(R.id.editStatus, true);
        } else {
            sharedModel.updateData(data);
            controller.navigateUp();
        }
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
        binding.spp.setSelection(located);
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

    private void deletePembayaran() {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<PembayaranData>> pembayaranDataCall;

        pembayaranDataCall = apiInterface.deletePembayaram(idPembayaran);
        pembayaranDataCall.enqueue(new Callback<BaseResponse<PembayaranData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PembayaranData>> call, Response<BaseResponse<PembayaranData>> response) {
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
            public void onFailure(@NonNull Call<BaseResponse<PembayaranData>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, false, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false).show();
            }
        });
    }


    private void editPembayaran(Long jumlahBayar, int idSpp) {
        UtilsUI.isLoading(binding.refresher, false, true);
        Call<BaseResponse<PembayaranData>> editPembayaranCall;

        editPembayaranCall = apiInterface.putPembayaran(
                idPembayaran,
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                null,
                Utils.parseLongtoStringDate(this.tglPembayaran, "yyyy-MM-dd"),
                Integer.valueOf(Utils.parseLongtoStringDate(this.sppDate, "MM")),
                Integer.valueOf(Utils.parseLongtoStringDate(this.sppDate, "yyyy")),
                idSpp,
                jumlahBayar
        );
        editPembayaranCall.enqueue(new Callback<BaseResponse<PembayaranData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PembayaranData>> call, Response<BaseResponse<PembayaranData>> response) {
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
        binding.simpan.setOnClickListener(v -> {
            int idSpp = sppList.get(binding.spp.getSelectedItemPosition()).getValue();

            if (idSpp == 0 && tglPembayaran == null && sppDate == null) {
                UtilsUI.toaster(getContext(), "Data tidak boleh kosong!");
            } else {
                UtilsUI.dialog(getContext(), "Simpan data?", "Apakah anda yakin untuk menyimpan data berikut, pastikan data sudah benar.", true).setPositiveButton("Ok", (dialog, which) -> {
                    editPembayaran(Utils.unformatRupiah(binding.jumlahBayar.getText().toString()), idSpp);
                }).show();
            }
        });

        binding.hapus.setOnClickListener(v -> {
            UtilsUI.dialog(getActivity(), "Hapus data?", "Apakah anda yakin untuk menghapus data berikut, data transaksi berikut mungkin akan muncul kembali dikarenakan sistem tunggakan mengambil data transaksi paling terbaru.", true).setPositiveButton("Ok", (dialog, which) -> {
                if (idPembayaran != 0) deletePembayaran();
            }).show();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas4EditTransaksiBinding.inflate(inflater, container, false);

        UtilsUI.isLoading(binding.refresher, false, false);
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        UILimiter(sessionManager.getUserDetail().get(SessionManager.TYPE));
        sharedModel = new ViewModelProvider(requireActivity()).get(PembayaranSharedModel.class);
        sharedModel.getData().observe(getViewLifecycleOwner(), detailsItemPembayaran -> {
            this.idPembayaran = detailsItemPembayaran.getIdPembayaran();
            if (detailsItemPembayaran.getTglBayar() != null) {
                this.tglPembayaran = Utils.parseServerStringtoLongDate(detailsItemPembayaran.getTglBayar(), "yyyy-MM-dd");
                binding.tglBayar.setText(Utils.parseLongtoStringDate(Utils.parseServerStringtoLongDate(detailsItemPembayaran.getTglBayar(), "yyyy-MM-dd"), "dd MMMM yyyy"));
            } else {
                this.tglPembayaran = Calendar.getInstance().getTimeInMillis();
            }
            this.sppDate = Utils.parseServerStringtoLongDate(detailsItemPembayaran.getTahunSpp() + "-" + detailsItemPembayaran.getBulanSpp(), "yyyy-MM");
            sppSpinner(detailsItemPembayaran.getIdSpp());
            binding.jumlahBayar.addTextChangedListener(new MoneyTextWatcher(binding.jumlahBayar, detailsItemPembayaran.getSpp().getNominal()));
            binding.sppTahunBulan.setText(Utils.parseLongtoStringDate(Utils.parseServerStringtoLongDate(detailsItemPembayaran.getTahunSpp() + "-" + detailsItemPembayaran.getBulanSpp(), "yyyy-MM"), "MMMM yyyy"));
            binding.jumlahBayar.setText(String.valueOf(detailsItemPembayaran.getJumlahBayar()));
        });
        return binding.getRoot();
    }

}