package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.kelas.DetailsItemKelas;
import com.lleans.spp_kelompok_2.domain.model.siswa.DetailsItemSiswa;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;

import java.util.List;

public class SiswaCardAdapter extends RecyclerView.Adapter<SiswaCardAdapter.SiswaCardViewHolder> {

    private final List<DetailsItemSiswa> listdata;
    private final DetailsItemKelas kelas;
    private final NavController navController;

    private Context context;

    public SiswaCardAdapter(List<DetailsItemSiswa> list, NavController navController, DetailsItemKelas kelas) {
        this.listdata = list;
        this.navController = navController;
        this.kelas = kelas;
    }

    @NonNull
    @Override
    public SiswaCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_main, parent, false);
        context = parent.getContext();
        return new SiswaCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SiswaCardViewHolder holder, int position) {
        DetailsItemSiswa data = listdata.get(position);
        holder.name.setText(data.getNama());
        holder.nisn.setText(data.getNisn());
        Utils.nicknameBuilder(context, data.getNama(), holder.nick, holder.nickFrame);
        holder.cardView.setOnClickListener(v -> {
            SiswaSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(SiswaSharedModel.class);
            sharedModel.updateData(data);
            navController.navigate(R.id.action_siswa_petugas_to_detail_siswa);
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class SiswaCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, nisn, nick;
        CardView cardView;
        FrameLayout nickFrame;

        public SiswaCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            nisn = itemView.findViewById(R.id.secondaryText);
            nick = itemView.findViewById(R.id.nick);

            cardView = itemView.findViewById(R.id.card);
            nickFrame = itemView.findViewById(R.id.nickFrame);
        }
    }
}
