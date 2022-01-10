package com.lleans.spp_kelompok_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lleans.spp_kelompok_2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.petugasLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, LauncherFragment.class);
                inte.putExtra("type", "petugas");
                MainActivity.this.startActivity(inte);
            }
        });
        binding.siswaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, LauncherFragment.class);
                inte.putExtra("type", "siswa");
                MainActivity.this.startActivity(inte);
            }
        });
    }
}