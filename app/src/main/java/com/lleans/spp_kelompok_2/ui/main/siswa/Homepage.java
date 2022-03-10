package com.lleans.spp_kelompok_2.ui.main.siswa;

import android.content.res.ColorStateList;
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
import com.lleans.spp_kelompok_2.databinding.Siswa1HomepageBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranDataList;
import com.lleans.spp_kelompok_2.domain.model.tunggakan.TunggakanData;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.auth.Logout;
import com.lleans.spp_kelompok_2.ui.main.siswa.transaksi.TransaksiCardAdapter;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Homepage extends Fragment implements UIListener {

    private Siswa1HomepageBinding binding;

    private NavController nav;
    private SessionManager sessionManager;

    private int green, orange;

    public Homepage() {
        // Required empty public constructor
    }

    private void getTunggakan() {
        isLoading(true);
        Call<TunggakanData> tunggakanDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        tunggakanDataCall = apiInterface.getTunggakan(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                sessionManager.getUserDetail().get(SessionManager.ID));
        tunggakanDataCall.enqueue(new Callback<TunggakanData>() {
            @Override
            public void onResponse(Call<TunggakanData> call, Response<TunggakanData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getDetails().getJumlahTunggakan() == 0) {
                        binding.cardTunggakan.setBackgroundTintList(ColorStateList.valueOf(green));
                        binding.totalTunggakan.setText("LUNAS");
                        getTransaksi();
                    } else if (response.body().getDetails().getJumlahTunggakan() > 1) {
                        binding.cardTunggakan.setBackgroundTintList(ColorStateList.valueOf(orange));
                        binding.totalTunggakan.setText(Utils.formatRupiah(response.body().getDetails().getTotalTunggakan()));
                    } else {
                        binding.totalTunggakan.setText(Utils.formatRupiah(response.body().getDetails().getTotalTunggakan()));
                    }
                    binding.cardTunggakan.setVisibility(View.VISIBLE);
                } else {
                    try {
                        TunggakanData message = new Gson().fromJson(response.errorBody().charStream(), TunggakanData.class);
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
            public void onFailure(@NonNull Call<TunggakanData> call, @NonNull Throwable t) {
                isLoading(false);
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    private void getTransaksi() {
        isLoading(true);
        Call<PembayaranDataList> pembayaranDataCall;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pembayaranDataCall = apiInterface.getPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
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
        pembayaranDataCall.enqueue(new Callback<PembayaranDataList>() {
            @Override
            public void onResponse(Call<PembayaranDataList> call, Response<PembayaranDataList> response) {
                isLoading(false);
                if (response.body() != null && response.isSuccessful()) {
                    TransaksiCardAdapter cardAdapter = new TransaksiCardAdapter(response.body().getDetails(), nav, true);
                    cardAdapter.setItemCount(3);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(cardAdapter);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Define navigation, Login killer fallback
        nav = Navigation.findNavController(view);
        UtilsUI.activityKiller(nav, getActivity());
        green = view.getResources().getColor(R.color.green);
        orange = view.getResources().getColor(R.color.orange);

        // Button listener
        binding.cardTunggakan.setOnClickListener(v -> nav.navigate(R.id.action_homepage2_to_transaksi));
        binding.transaksi.setOnClickListener(v -> nav.navigate(R.id.action_homepage2_to_transaksi));
        binding.logout.setOnClickListener(v -> new Logout(getContext(), getActivity()));
        binding.refresher.setOnRefreshListener(this::getTransaksi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = Siswa1HomepageBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());

        getTunggakan();

        // Change layout before it show
        binding.header.setText("Hai, " + sessionManager.getUserDetail().get(SessionManager.USERNAME));

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