package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

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

public class HistoriCardAdapter extends RecyclerView.Adapter<HistoriCardAdapter.HistoriCardViewHolder> {

    private final List<DetailsItemPembayaran> listdata;
    private final NavController navController;

    public HistoriCardAdapter(List<DetailsItemPembayaran> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }

    @NonNull
    @Override
    public HistoriCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_histori, parent, false);
        return new HistoriCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoriCardViewHolder holder, int position) {
        DetailsItemPembayaran data = listdata.get(position);
        holder.nama_histori.setText(data.getSiswa().getNama());
        holder.kelas_histori.setText(String.valueOf(data.getSiswa().getIdKelas()));
        holder.nominal_histori.setText("Rp. " + data.getJumlahBayar());
        holder.card_histori.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            navController.navigate(R.id.action_histori_petugas_to_rincianTransaksi_siswa, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class HistoriCardViewHolder extends RecyclerView.ViewHolder {
        TextView nama_histori, kelas_histori, nominal_histori;
        CardView card_histori;

        public HistoriCardViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_histori = itemView.findViewById(R.id.histori_nama);
            kelas_histori = itemView.findViewById(R.id.kelas_histori);
            nominal_histori = itemView.findViewById(R.id.histori_nominal);
            card_histori = itemView.findViewById(R.id.card_histori);
        }
    }
}
