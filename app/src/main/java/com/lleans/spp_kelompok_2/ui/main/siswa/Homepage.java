package com.lleans.spp_kelompok_2.ui.main.siswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.HomepageSiswaBinding;
import com.lleans.spp_kelompok_2.ui.MainActivity;
import com.lleans.spp_kelompok_2.ui.login.Logout;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class Homepage extends Fragment {

    private HomepageSiswaBinding binding;
    private SessionManager sessionManager;

    private int totalTunggakan;

    public Homepage() {
        // Required empty public constructor
    }

//    private void getHomepageData() {
//        Call<PembayaranData> Pembayaran;
//        Call<SppData> Spp;
//        ApiInterface apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
//        Pembayaran = apiInterface.getPembayaran(
//                null,
//                null,
//                sessionManager.getUserDetail().get(SessionManager.ID),
//                null,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        Pembayaran.enqueue(new Callback<PembayaranData>() {
//            @Override
//            public void onResponse(Call<PembayaranData> call, Response<PembayaranData> response) {
//                if (response.body() != null && response.isSuccessful()) {
//                    // Hitung total tunggakan
//                    for(DetailsItemPembayaran data : response.body().getDetails()){
//                        SppPembayaran spp = data.getSpp();
//                        if(data.getJumlahBayar() < spp.getNominal()){
//                            totalTunggakan =+ spp.getNominal()-data.getJumlahBayar();
//                        }
//                    }
//
//                    // Recycler view pembayaran
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PembayaranData> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Contoh navigation
        final NavController nav = Navigation.findNavController(view);

        nav.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.login) {
                MainActivity.act.finish();
                getActivity().finish();
            }
        });

//        if(totalTunggakan == 0){
//            binding.cardTunggakan.setImageResource(R.drawable.cardss_hijau);
//            binding.totalTunggakan.setText("LUNAS");
//        } else

        // Cari id navigation di nav graph
        binding.detailSpp.setOnClickListener(v -> nav.navigate(R.id.action_homepage_siswa_to_rincianSpp_petugas));
        binding.lihatSemuaTransaksi.setOnClickListener(v -> nav.navigate(R.id.action_homepage2_to_transaksi));
        binding.imageView8.setOnClickListener(v -> nav.navigate(R.id.action_transaksi_siswa_to_rincianTransaksi_siswa));
        binding.logout.setOnClickListener(v -> new Logout(getContext(), getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = HomepageSiswaBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        binding.header.setText("Hai, " + sessionManager.getUserDetail().get(SessionManager.USERNAME));

        return binding.getRoot();
    }
}