package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

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

import java.util.List;

public class KelasCardAdapter extends RecyclerView.Adapter<KelasCardAdapter.KelasCardViewHolder> {

    private final List<DetailsItemKelas> listdata;
    private final NavController navController;

    public KelasCardAdapter(List<DetailsItemKelas> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }


    @NonNull
    @Override
    public KelasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_kelas, parent, false);
        return new KelasCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KelasCardViewHolder holder, int position) {
        DetailsItemKelas data = listdata.get(position);
        holder.nama_kelas.setText(data.getNamaKelas());
        //holder.jumlah_kelas.setText(data.get);
        holder.cardView.setOnClickListener(v -> {
            DetailsItemKelas d = listdata.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", d);
            navController.navigate(R.id.action_kelas_petugas_to_siswa_petugas, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class KelasCardViewHolder extends RecyclerView.ViewHolder {
        TextView nama_kelas, jumlah_kelas;
        CardView cardView;

        public KelasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_kelas = itemView.findViewById(R.id.kelas_name);
            jumlah_kelas = itemView.findViewById(R.id.kelas_jumlah);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
