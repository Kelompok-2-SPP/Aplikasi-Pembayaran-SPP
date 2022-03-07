package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

import android.content.Context;
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
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;

import java.util.List;

public class KelasCardAdapter extends RecyclerView.Adapter<KelasCardAdapter.KelasCardViewHolder> {

    private final List<DetailsItemKelas> listdata;
    private final NavController navController;

    private Context context;

    public KelasCardAdapter(List<DetailsItemKelas> list, NavController navController) {
        this.listdata = list;
        this.navController = navController;
    }


    @NonNull
    @Override
    public KelasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_main, parent, false);
        context = view.getContext();
        return new KelasCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KelasCardViewHolder holder, int position) {
        DetailsItemKelas data = listdata.get(position);
        holder.nama_kelas.setText(data.getNamaKelas());
        holder.jumlah_kelas.setText("Angkatan " + data.getAngkatan());
        Utils.nicknameBuilder(context, data.getNamaKelas(), holder.nick, holder.nickFrame);
        holder.cardView.setOnClickListener(v -> {
            KelasSharedModel sharedModel = new  ViewModelProvider((LauncherFragment) context).get(KelasSharedModel.class);
            sharedModel.updateData(data);
            navController.navigate(R.id.action_kelas_petugas_to_siswa_petugas);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class KelasCardViewHolder extends RecyclerView.ViewHolder {
        TextView nama_kelas, jumlah_kelas, nick;
        CardView cardView;
        FrameLayout nickFrame;

        public KelasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_kelas = itemView.findViewById(R.id.title);
            jumlah_kelas = itemView.findViewById(R.id.secondaryText);
            nick = itemView.findViewById(R.id.nick);

            cardView = itemView.findViewById(R.id.card);
            nickFrame = itemView.findViewById(R.id.nickFrame);
        }
    }
}
