package com.lleans.spp_kelompok_2.ui.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.appbar.AppBarLayout;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.ActivityLauncherFragmentBinding;

public class LauncherFragment extends AppCompatActivity {

    private ActivityLauncherFragmentBinding binding;
    private NavHostFragment navHostFragment;

    private AppBarLayout appBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLauncherFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get AppBar and Toolbar
        appBar = binding.appbar;
        toolbar = binding.toolbar;

        // Hide and unhide AppBar
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            toolbar.setTitle(destination.getLabel());
            if (destination.getId() == R.id.homepage_petugas || destination.getId() == R.id.homepage_siswa || destination.getId() == R.id.login) {
                appBar.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                appBar.setVisibility(View.VISIBLE);
                toolbar.setNavigationOnClickListener(v -> {
                    controller.navigateUp();
                });
            }
        });
    }

}