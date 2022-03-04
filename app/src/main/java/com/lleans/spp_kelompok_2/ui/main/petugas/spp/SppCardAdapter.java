package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

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
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;

import java.util.List;

public class SppCardAdapter extends RecyclerView.Adapter<SppCardAdapter.SppCardViewHolder> {

    private final List<DetailsItemSpp> listdata;
    private final NavController navController;

    private boolean fromHomepage;
    private int count;

    public SppCardAdapter(List<DetailsItemSpp> list, NavController navController, boolean fromHomepage) {
        this.listdata = list;
        this.navController = navController;
        this.fromHomepage = fromHomepage;
    }

    @NonNull
    @Override
    public SppCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_spp, parent, false);
        return new SppCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SppCardViewHolder holder, int position) {
        DetailsItemSpp data = listdata.get(position);
        holder.judul.setText("Total SPP\nAngkatan " + data.getAngkatan());
        holder.tahun.setText("Tahun " + data.getTahun());

        holder.nominal.setText(Utils.formatRupiah(data.getNominal()));
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("spp", data);
            if (fromHomepage) {
                navController.navigate(R.id.action_homepage_petugas_to_rincianSpp_petugas, bundle);
            } else {
                navController.navigate(R.id.action_spp_petugas_to_rincianSpp_petugas, bundle);
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

    public static class SppCardViewHolder extends RecyclerView.ViewHolder {
        TextView judul, tahun, nominal;
        CardView cardView;

        public SppCardViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.titleSpp);
            tahun = itemView.findViewById(R.id.tahunSpp);
            nominal = itemView.findViewById(R.id.jumlahSpp);

            cardView = itemView.findViewById(R.id.card_spp);
        }
    }
}
