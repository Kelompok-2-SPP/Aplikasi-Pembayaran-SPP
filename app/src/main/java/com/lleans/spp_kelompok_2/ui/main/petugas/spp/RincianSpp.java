package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

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
import com.lleans.spp_kelompok_2.databinding.Petugas3RincianSppBinding;
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class RincianSpp extends Fragment {

    private Petugas3RincianSppBinding binding;
    private SessionManager sessionManager;

    private DetailsItemSpp data;

    public RincianSpp() {
        // Required empty public constructor
    }

    private void UILimiter() {
        binding.edit.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController nav = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        bundle.putSerializable("spp", data);
        binding.edit.setOnClickListener(v -> nav.navigate(R.id.action_rincianSpp_petugas_to_editSpp, bundle));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        binding = Petugas3RincianSppBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getContext());
        if(sessionManager.getUserDetail().get(SessionManager.TYPE).equals("petugas")){
            UILimiter();
        }
        data = (DetailsItemSpp) bundle.getSerializable("spp");
        return binding.getRoot();
    }
}