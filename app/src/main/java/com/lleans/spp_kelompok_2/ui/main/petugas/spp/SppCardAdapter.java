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
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;

import java.util.List;

public class SppCardAdapter extends RecyclerView.Adapter<SppCardAdapter.SppCardViewHolder> {

    private final List<DetailsItemSpp> listdata;
    private final NavController navController;

    public SppCardAdapter(List<DetailsItemSpp> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }

    public String getMonth(int month) {
        String[] months = {"none", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Nopember", "Desember"};
        return months[month];
    }

    @NonNull
    @Override
    public SppCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_total_spp, parent, false);
        return new SppCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SppCardViewHolder holder, int position) {
        DetailsItemSpp data = listdata.get(position);
        holder.nominal.setText("Rp. " + data.getNominal());
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("spp", data);
            navController.navigate(R.id.action_spp_petugas_to_rincianSpp_petugas, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class SppCardViewHolder extends RecyclerView.ViewHolder {
        TextView bulan, nominal;
        CardView cardView;

        public SppCardViewHolder(@NonNull View itemView) {
            super(itemView);
            bulan = itemView.findViewById(R.id.bulan_spp);
            nominal = itemView.findViewById(R.id.nominalSpp);
            cardView = itemView.findViewById(R.id.card_spp);
        }
    }
}
