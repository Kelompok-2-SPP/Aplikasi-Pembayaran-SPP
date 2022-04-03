package com.lleans.spp_kelompok_2.ui.main.siswa;

import android.content.res.ColorStateList;
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
import com.lleans.spp_kelompok_2.databinding.Siswa1HomepageBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.CachedPembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.tunggakan.TunggakanData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.auth.Logout;
import com.lleans.spp_kelompok_2.ui.main.siswa.transaksi.TransaksiCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Homepage extends Fragment {

    private Siswa1HomepageBinding binding;
    private NavController controller;
    private SessionManager sessionManager;
    private ApiInterface apiInterface;
    private CachedPembayaranSharedModel cachedPembayaranSharedModel;

    private int green, orange;

    public Homepage() {
        // Required empty public constructor
    }

    private void setAdapter(List<PembayaranData> data) {
        TransaksiCardAdapter cardAdapter = new TransaksiCardAdapter(data, controller, true, null);

        cachedPembayaranSharedModel.updateData(data);
        cardAdapter.setItemCount(3);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void setCard(int jumlahTunggalan, Long totalTunggakan) {
        if (jumlahTunggalan == 0) {
            binding.cardTunggakan.setBackgroundTintList(ColorStateList.valueOf(green));
            binding.totalTunggakan.setText("LUNAS");
        } else if (jumlahTunggalan > 1) {
            binding.cardTunggakan.setBackgroundTintList(ColorStateList.valueOf(orange));
            binding.totalTunggakan.setText(Utils.formatRupiah(totalTunggakan));
        } else {
            binding.totalTunggakan.setText(Utils.formatRupiah(totalTunggakan));
        }
        UtilsUI.simpleAnimation(binding.cardTunggakan);
        binding.cardTunggakan.setVisibility(View.VISIBLE);
    }

    private void getTunggakan() {
        UtilsUI.isLoading(binding.refresher, true, true);
        Call<BaseResponse<TunggakanData>> tunggakanDataCall;

        tunggakanDataCall = apiInterface.getTunggakan(sessionManager.getUserDetail().get(SessionManager.ID));
        tunggakanDataCall.enqueue(new Callback<BaseResponse<TunggakanData>>() {
            @Override
            public void onResponse(Call<BaseResponse<TunggakanData>> call, Response<BaseResponse<TunggakanData>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    setCard(response.body().getDetails().getJumlahTunggakan(), response.body().getDetails().getTotalTunggakan());
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
        Call<BaseResponse<List<PembayaranData>>> pembayaranDataCall;

        pembayaranDataCall = apiInterface.getPembayaran(
                null,
                null,
                sessionManager.getUserDetail().get(SessionManager.ID),
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
                        UtilsUI.toaster(getContext(), "Transaksi tidak ditemukan");
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

        UtilsUI.activityKiller(getActivity(), controller);
        green = view.getResources().getColor(R.color.green);
        orange = view.getResources().getColor(R.color.orange);
        binding.cardTunggakan.setOnClickListener(v -> controller.navigate(R.id.action_homepage2_to_transaksi));
        binding.transaksi.setOnClickListener(v -> controller.navigate(R.id.action_homepage2_to_transaksi));
        binding.logout.setOnClickListener(v -> new Logout(getContext(), getActivity()));
        binding.refresher.setOnRefreshListener(this::getTransaksi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Siswa1HomepageBinding.inflate(inflater, container, false);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        cachedPembayaranSharedModel = new ViewModelProvider(requireActivity()).get(CachedPembayaranSharedModel.class);
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        getTunggakan();
        binding.header.setText("Hai, " + sessionManager.getUserDetail().get(SessionManager.USERNAME));
        return binding.getRoot();
    }

}