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
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;

import java.util.List;

public class AktivitasCardAdapter extends RecyclerView.Adapter<AktivitasCardAdapter.AktivitasCardViewHolder> {

    private final List<DetailsItemPembayaran> listdata;
    private final NavController navController;

    private boolean fromHomepage, fromRincian;
    private int orange;

    private int count;

    public AktivitasCardAdapter(List<DetailsItemPembayaran> list, NavController navController, boolean fromHomepage, boolean fromRincian) {
        this.listdata = list;
        this.navController = navController;
        this.fromHomepage = fromHomepage;
        this.fromRincian = fromRincian;
    }

    @NonNull
    @Override
    public AktivitasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_aktivitas, parent, false);
        orange = view.getResources().getColor(R.color.orange);
        return new AktivitasCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AktivitasCardViewHolder holder, int position) {
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
            if (fromHomepage) {
                navController.navigate(R.id.action_homepage_petugas_to_rincianTransaksi_siswa, bundle);
            } else if(fromRincian){
                navController.navigate(R.id.action_detailPetugas_petuga_to_rincianTransaksi_siswa, bundle);
            }
            else {
                navController.navigate(R.id.action_aktivitas_petugas_to_rincianTransaksi_siswa, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return count != 0 ? count:listdata.size();
    }

    public int setItemCount(int count) {
        return this.count = count;
    }

    public static class AktivitasCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, kelas, status, nominalkurang;
        CardView cardView;

        public AktivitasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            kelas = itemView.findViewById(R.id.kelas);
            status = itemView.findViewById(R.id.status);
            nominalkurang = itemView.findViewById(R.id.nominal);

            cardView = itemView.findViewById(R.id.card);
        }
    }
}
