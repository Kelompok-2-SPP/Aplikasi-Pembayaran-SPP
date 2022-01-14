package com.lleans.spp_kelompok_2.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.databinding.ActivityMainBinding;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Intent intent;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        intent = new Intent(MainActivity.this, LauncherFragment.class);
        setContentView(binding.getRoot());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.splashScreen.setVisibility(View.GONE);
            }
        }, 200);

        SessionManager sessionManager = new SessionManager(MainActivity.this);
        if(sessionManager.isLoggedIn()){
            moveActivity("petugas");
        }
        binding.petugasLoginBtn.setOnClickListener(v -> moveActivity("petugas"));
        binding.siswaLoginBtn.setOnClickListener(v -> moveActivity("siswa"));
    }

    private void moveActivity(String type){
        intent.putExtra("type", type);
        MainActivity.this.startActivity(intent);
    }
}