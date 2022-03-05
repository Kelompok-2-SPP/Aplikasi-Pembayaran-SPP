package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;

import java.util.List;

public class HistoriCardAdapter extends RecyclerView.Adapter<HistoriCardAdapter.HistoriCardViewHolder> {

    private int orange;
    private final List<DetailsItemPembayaran> listdata;
    private final NavController navController;

    public HistoriCardAdapter(List<DetailsItemPembayaran> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }

    @NonNull
    @Override
    public HistoriCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_aktivitas, parent, false);
        orange = view.getResources().getColor(R.color.orange);
        return new HistoriCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoriCardViewHolder holder, int position) {
        DetailsItemPembayaran data = listdata.get(position);
        holder.name.setText(data.getSiswa().getNama());
        if (Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar())) {
            holder.kelas.setText(data.getSiswa().getKelas().getNamaKelas());
            holder.status.setText("Belum Lunas");
            holder.status.setTextColor(orange);
            holder.nominalkurang.setText(Utils.kurangBayar(data.getSpp().getNominal(), data.getJumlahBayar()));
        }else{
            holder.kelas.setText(data.getSiswa().getKelas().getNamaKelas());
            holder.status.setText("Lunas");
            holder.nominalkurang.setText(Utils.formatRupiah(data.getJumlahBayar()));
        }
        holder.cardView.setOnClickListener(v -> {
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
        TextView name, kelas, status, nominalkurang;
        CardView cardView;

        public HistoriCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            kelas = itemView.findViewById(R.id.kelas);
            status = itemView.findViewById(R.id.status);
            nominalkurang = itemView.findViewById(R.id.nominal);

            cardView = itemView.findViewById(R.id.card);
        }
    }
}
