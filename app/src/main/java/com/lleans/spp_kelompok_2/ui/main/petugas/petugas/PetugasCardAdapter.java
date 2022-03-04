package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import com.lleans.spp_kelompok_2.domain.model.petugas.DetailsItemPetugas;

import java.util.List;

public class PetugasCardAdapter extends RecyclerView.Adapter<PetugasCardAdapter.PetugasCardViewHolder> {

    private final List<DetailsItemPetugas> listdata;
    private final NavController navController;

    public PetugasCardAdapter(List<DetailsItemPetugas> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }


    @NonNull
    @Override
    public PetugasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_main, parent, false);
        return new PetugasCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PetugasCardViewHolder holder, int position) {
        DetailsItemPetugas data = listdata.get(position);
        holder.name.setText(data.getNamaPetugas());
        holder.uname.setText(data.getUsername());
        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("petugas", data);
            navController.navigate(R.id.action_petugas_petugas_to_tambahPetugas, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class PetugasCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, uname;
        CardView cardView;

        public PetugasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            uname = itemView.findViewById(R.id.secondaryText);

            cardView = itemView.findViewById(R.id.card);
        }
    }
}
