package com.lleans.spp_kelompok_2.ui.main.petugas;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.Petugas1HomepageBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.spp.SppDataList;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.auth.Logout;
import com.lleans.spp_kelompok_2.ui.main.petugas.aktivitas.AktivitasCardAdapter;
import com.lleans.spp_kelompok_2.ui.main.petugas.spp.SppCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Homepage extends Fragment implements UIListener {

    private Petugas1HomepageBinding binding;

    private SessionManager sessionManager;
    private NavController nav;

    public Homepage() {
        // Required empty public constructor
    }

    private void UILimiter(){
        binding.dataPetugas.setVisibility(View.GONE);
        binding.dashboard.setBackgroundResource(R.drawable.background_dashboard_petugas);
        binding.dashboard.getLayoutParams().height = (int) (120 * getContext().getResources().getDisplayMetrics().density);
    }

    private void getAktivitas() {
        isLoading(true);
        Call<PembayaranDataList> pembayaranDataListCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pembayaranDataListCall = apiInterface.getPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
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
        pembayaranDataListCall.enqueue(new Callback<PembayaranDataList>() {
            @Override
            public void onResponse(Call<PembayaranDataList> call, Response<PembayaranDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    AktivitasCardAdapter cardAdapter = new AktivitasCardAdapter(response.body().getDetails(), nav, true, false);
                    cardAdapter.setItemCount(3);
                    binding.recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView1.setAdapter(cardAdapter);
                } else {
                    try {
                        PembayaranDataList message = new Gson().fromJson(response.errorBody().charStream(), PembayaranDataList.class);
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
            public void onFailure(@NonNull Call<PembayaranDataList> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });

    }

    private void getSpp() {
        isLoading(true);
        Call<SppDataList> sppData;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sppData = apiInterface.getSpp(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                null,
                null,
                null,
                null,
                null,
                null
        );
        sppData.enqueue(new Callback<SppDataList>() {
            @Override
            public void onResponse(Call<SppDataList> call, Response<SppDataList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    SppCardAdapter cardAdapter = new SppCardAdapter(response.body().getDetails(), nav, true);
                    cardAdapter.setItemCount(1);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
                    getAktivitas();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Define navigation, Login killer fallback
        nav = Navigation.findNavController(view);
        UtilsUI.activityKiller(nav, getActivity());
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromHomepage", true);

        // Button listener
        binding.dataKelas.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_kelas_petugas));
        binding.histori.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_histori_petugas));
        binding.dataSiswa.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_kelas_petugas));
        binding.dataPetugas.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_petugas_petugas2));

        binding.sppSemua.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_spp_petugas));
        binding.aktivitas.setOnClickListener(v -> nav.navigate(R.id.action_homepage_petugas_to_aktivitasPetugas, bundle));
        binding.logout.setOnClickListener(v -> new Logout(getContext(), getActivity()));

        binding.refresher.setOnRefreshListener(this::getSpp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Petugas1HomepageBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());

        getSpp();

        // Change layout before it show
        binding.header.setText("Hai, " + sessionManager.getUserDetail().get(SessionManager.USERNAME));
        if(sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")){
            UILimiter();
        }

        return binding.getRoot();
    }

    // Abstract class for loadingBar
    @Override
    public void isLoading(Boolean isLoading) {
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