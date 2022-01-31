package com.lleans.spp_kelompok_2.ui.main.siswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lleans.spp_kelompok_2.UIListener;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.HomepageSiswaBinding;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.ui.auth.Logout;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class Homepage extends Fragment implements UIListener {

    private HomepageSiswaBinding binding;

    private NavController nav;
    private SessionManager sessionManager;

    public Homepage() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Define navigation, Login killer fallback
        nav = Navigation.findNavController(view);
        Utils.activityKiller(nav, getActivity());

        // Button listener
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

        // Change layout before it show
        binding.header.setText("Hai, " + sessionManager.getUserDetail().get(SessionManager.USERNAME));

        return binding.getRoot();
    }

    @Override
    public void isLoading(Boolean isLoading) {
        binding.refresher.setRefreshing(isLoading);
    }

    @Override
    public void toaster(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialog(String title, String message) {

    }
}