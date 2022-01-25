package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

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
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;

import java.util.List;

public class TransaksiCardAdapter extends RecyclerView.Adapter<TransaksiCardAdapter.TransaksiCardViewHolder> {

    private final List<DetailsItemPembayaran> listdata;
    private final NavController navController;

    public TransaksiCardAdapter(List<DetailsItemPembayaran> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }

    @NonNull
    @Override
    public TransaksiCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_aktivitas_siswa, parent, false);
        return new TransaksiCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransaksiCardViewHolder holder, int position) {
        DetailsItemPembayaran data = listdata.get(position);
        holder.title.setText(data.getTahunDibayar() + " • " + Utils.getMonth(data.getBulanDibayar()));
        holder.nominal.setText("Rp" + data.getJumlahBayar());
        if (Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar())) {
            holder.status.setText("Belum Lunas");
            holder.status.setTextColor(16750848);
        }
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            navController.navigate(R.id.action_transaksi_siswa_to_rincianTransaksi_siswa, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class TransaksiCardViewHolder extends RecyclerView.ViewHolder {
        TextView title, status, nominal;
        CardView cardView;

        public TransaksiCardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_pembayaran);
            status = itemView.findViewById(R.id.status_pembayaran);
            nominal = itemView.findViewById(R.id.totalPembayaran);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
