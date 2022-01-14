package com.lleans.spp_kelompok_2.ui.main.siswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.HomepageSiswaBinding;
import com.lleans.spp_kelompok_2.ui.MainActivity;
import com.lleans.spp_kelompok_2.ui.login.Logout;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class Homepage extends Fragment {

    private HomepageSiswaBinding binding;
    private SessionManager sessionManager;

    public Homepage() {
        // Required empty public constructor
    }

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

        // Cari id navigation di nav graph
        binding.detailSpp.setOnClickListener(v -> nav.navigate(R.id.action_homepage_siswa_to_rincianSpp_petugas));
        binding.lihatSemuaTransaksi.setOnClickListener(v -> nav.navigate(R.id.action_homepage2_to_transaksi));
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