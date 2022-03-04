package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.siswa.DetailsItemSiswa;

import java.util.List;

public class SiswaCardAdapter extends RecyclerView.Adapter<SiswaCardAdapter.SiswaCardViewHolder> {

    private final List<DetailsItemSiswa> listdata;
    private final DetailsItemKelas kelas;
    private final NavController navController;

    public SiswaCardAdapter(List<DetailsItemSiswa> list, NavController navController, DetailsItemKelas kelas) {
        this.listdata = list;
        this.navController = navController;
        this.kelas = kelas;
    }

    @NonNull
    @Override
    public SiswaCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_main, parent, false);
        return new SiswaCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SiswaCardViewHolder holder, int position) {
        DetailsItemSiswa data = listdata.get(position);
        holder.name.setText(data.getNama());
        holder.nisn.setText(data.getNisn());
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("siswa", data);
            bundle.putSerializable("kelas", kelas);
            navController.navigate(R.id.action_siswa_petugas_to_detail_siswa, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class SiswaCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, nisn;
        CardView cardView;

        public SiswaCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            nisn = itemView.findViewById(R.id.secondaryText);

            cardView = itemView.findViewById(R.id.card);
        }
    }
}
