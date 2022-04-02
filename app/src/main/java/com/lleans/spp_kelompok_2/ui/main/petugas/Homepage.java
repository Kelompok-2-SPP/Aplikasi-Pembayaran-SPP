package com.lleans.spp_kelompok_2.ui.main.petugas;

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
import com.lleans.spp_kelompok_2.databinding.Petugas1HomepageBinding;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.CachedPembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.auth.Logout;
import com.lleans.spp_kelompok_2.ui.main.petugas.aktivitas.AktivitasCardAdapter;
import com.lleans.spp_kelompok_2.ui.main.petugas.spp.SppCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Homepage extends Fragment {

    private Petugas1HomepageBinding binding;
    private NavController controller;
    private ApiInterface apiInterface;

    private CachedPembayaranSharedModel cachedPembayaranSharedModel;

    public Homepage() {
        // Required empty public constructor
    }

    private void UILimiter() {
        binding.dataPetugas.setVisibility(View.GONE);
        binding.dashboard.setBackgroundResource(R.drawable.background_dashboard_petugas);
        binding.dashboard.getLayoutParams().height = (int) (120 * getActivity().getApplicationContext().getResources().getDisplayMetrics().density);
    }

    private void setSppAdapter(List<SppData> data) {
        SppCardAdapter cardAdapter = new SppCardAdapter(data, controller, true);
        cardAdapter.setItemCount(1);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(cardAdapter);
    }

    private void setAktivitasAdpter(List<PembayaranData> data) {
        AktivitasCardAdapter cardAdapter = new AktivitasCardAdapter(data, controller, "homepage");

        cachedPembayaranSharedModel.updateData(data);
        cardAdapter.setItemCount(3);
        binding.recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView1.setAdapter(cardAdapter);
    }

    private void getAktivitas() {
        Call<BaseResponse<List<PembayaranData>>> pembayaranDataListCall;

        pembayaranDataListCall = apiInterface.getPembayaran(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        pembayaranDataListCall.enqueue(new Callback<BaseResponse<List<PembayaranData>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PembayaranData>>> call, Response<BaseResponse<List<PembayaranData>>> response) {
                UtilsUI.isLoading(binding.refresher, true, false);
                if (response.body() != null && response.isSuccessful()) {
                    setAktivitasAdpter(response.body().getDetails());
                } else {
                    try {
                        BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(getContext(), message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<PembayaranData>>> call, @NonNull Throwable t) {
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false);
            }
        });

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
                if (response.body() != null && response.isSuccessful()) {
                    setSppAdapter(response.body().getDetails());
                    getAktivitas();
                } else {
                    try {
                        BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(getContext(), message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(getContext(), "Something went wrong!", response.errorBody().string(), false);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<List<SppData>>> call, @NonNull Throwable t) {
                getAktivitas();
                UtilsUI.isLoading(binding.refresher, true, false);
                UtilsUI.dialog(getContext(), "Something went wrong!", t.getLocalizedMessage(), false);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        UtilsUI.activityKiller(getActivity(), controller);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromHomepage", true);
        binding.dataKelas.setOnClickListener(v -> controller.navigate(R.id.action_homepage_petugas_to_kelas_petugas));
        binding.histori.setOnClickListener(v -> controller.navigate(R.id.action_homepage_petugas_to_histori_petugas));
        binding.dataSiswa.setOnClickListener(v -> controller.navigate(R.id.action_homepage_petugas_to_kelas_petugas));
        binding.dataPetugas.setOnClickListener(v -> controller.navigate(R.id.action_homepage_petugas_to_petugas_petugas2));
        binding.sppSemua.setOnClickListener(v -> controller.navigate(R.id.action_homepage_petugas_to_spp_petugas));
        binding.aktivitas.setOnClickListener(v -> controller.navigate(R.id.action_homepage_petugas_to_aktivitasPetugas, bundle));
        binding.logout.setOnClickListener(v -> new Logout(getContext(), getActivity()));
        binding.refresher.setOnRefreshListener(this::getSpp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = Petugas1HomepageBinding.inflate(inflater, container, false);

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        cachedPembayaranSharedModel = new ViewModelProvider(requireActivity()).get(CachedPembayaranSharedModel.class);
        apiInterface = ApiClient.getClient(sessionManager.getUserDetail().get(SessionManager.TOKEN)).create(ApiInterface.class);
        getSpp();
        binding.header.setText("Hai, " + sessionManager.getUserDetail().get(SessionManager.USERNAME));
        if (sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")) {
            UILimiter();
        }
        return binding.getRoot();
    }

}