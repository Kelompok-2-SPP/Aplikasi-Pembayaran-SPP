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

public class LauncherFragment extends AppCompatActivity {

    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_fragment);

        AppBarLayout appBarLayout = findViewById(R.id.AppBar);
        Toolbar toolbar = findViewById(R.id.ToolbarTitle);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            toolbar.setTitle(destination.getLabel());
            if ("homepage_petugas".equals(destination.getLabel())) {
                appBarLayout.setVisibility(View.GONE);
            } else if ("homepage_siswa".equals(destination.getLabel())) {
                appBarLayout.setVisibility(View.GONE);
            } else if ("login".equals(destination.getLabel())) {
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