package com.lleans.spp_kelompok_2.ui.launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.ActivityLauncherFragmentBinding;

import java.util.Objects;

public class LauncherFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLauncherFragmentBinding  binding = ActivityLauncherFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarLayout appBar = binding.appbar;
        Toolbar toolbar = binding.toolbar;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        Objects.requireNonNull(navHostFragment).getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            toolbar.setTitle(destination.getLabel());
            if (destination.getId() == R.id.homepage_petugas || destination.getId() == R.id.homepage_siswa || destination.getId() == R.id.login) {
                appBar.setVisibility(View.GONE);
            } else {
                appBar.setVisibility(View.VISIBLE);
                toolbar.setNavigationOnClickListener(v -> controller.navigateUp());
            }
        });
    }

}