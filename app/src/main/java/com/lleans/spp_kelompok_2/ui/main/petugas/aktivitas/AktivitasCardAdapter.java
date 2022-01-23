package com.lleans.spp_kelompok_2.ui.main.petugas.aktivitas;

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
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;

import java.util.List;

public class AktivitasCardAdapter extends RecyclerView.Adapter<AktivitasCardAdapter.AktivitasCardViewHolder> {

    private final List<DetailsItemPembayaran> listdata;
    private final NavController navController;

    public AktivitasCardAdapter(List<DetailsItemPembayaran> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }

    @NonNull
    @Override
    public AktivitasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_aktivitas, parent, false);
        return new AktivitasCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AktivitasCardViewHolder holder, int position) {
        DetailsItemPembayaran data = listdata.get(position);
        holder.name.setText(data.getSiswa().getNama());
        holder.kelas.setText(data.getSiswa().getIdKelas());
        // holder.status.setText(data.getStatus());
        holder.nominalkurang.setText(data.getJumlahBayar());
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            navController.navigate(R.id.action_aktivitas_petugas_to_rincianTransaksi_siswa, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class AktivitasCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, kelas, status, nominalkurang;
        CardView cardView;

        public AktivitasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.aktivitas_name);
            kelas = itemView.findViewById(R.id.aktivitas_kelas);
            status = itemView.findViewById(R.id.aktivitas_status);
            nominalkurang = itemView.findViewById(R.id.nominalkurang);
            cardView = itemView.findViewById(R.id.card_aktivitas);
        }
    }
}
