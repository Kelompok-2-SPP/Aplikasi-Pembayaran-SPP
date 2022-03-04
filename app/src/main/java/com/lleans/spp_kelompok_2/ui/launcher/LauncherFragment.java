package com.lleans.spp_kelompok_2.ui.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.ActivityLauncherFragmentBinding;

public class LauncherFragment extends AppCompatActivity {

    private ActivityLauncherFragmentBinding binding;
    private NavHostFragment navHostFragment;

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLauncherFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get AppBar and Toolbar
        appBar = binding.appbar;
        toolbar = binding.toolbar;
        title = binding.title;

        // Hide and unhide AppBar
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            title.setText(destination.getLabel());
            if ("homepage_petugas".equals(destination.getLabel()) || "homepage_siswa".equals(destination.getLabel()) || "login".equals(destination.getLabel())) {
                appBar.setVisibility(View.GONE);
            } else {
                appBar.setVisibility(View.VISIBLE);
                toolbar.setNavigationOnClickListener(v -> {
                    controller.navigateUp();
                });
            }
        });
    }

}