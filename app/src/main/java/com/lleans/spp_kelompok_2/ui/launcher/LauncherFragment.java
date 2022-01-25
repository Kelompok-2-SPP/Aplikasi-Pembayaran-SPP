package com.lleans.spp_kelompok_2.ui.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.ActivityLauncherFragmentBinding;

public class LauncherFragment extends AppCompatActivity {

    private ActivityLauncherFragmentBinding binding;
    private NavHostFragment navHostFragment;

    private AppBarLayout  appBarLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLauncherFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get AppBar and Toolbar
        appBarLayout = binding.AppBar;
        toolbar = binding.ToolbarTitle;

        // Hide and unhide AppBar
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            toolbar.setTitle(destination.getLabel());
            if ("homepage_petugas".equals(destination.getLabel()) || "homepage_siswa".equals(destination.getLabel()) || "login".equals(destination.getLabel())) {
                appBarLayout.setVisibility(View.GONE);
            } else {
                appBarLayout.setVisibility(View.VISIBLE);
                toolbar.setNavigationOnClickListener(v -> {
                    controller.navigateUp();
                });
            }
        });
    }

}