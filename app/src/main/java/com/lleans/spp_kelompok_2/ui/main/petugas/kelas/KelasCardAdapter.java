package com.lleans.spp_kelompok_2.ui.main.petugas.kelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasData;
import com.lleans.spp_kelompok_2.domain.model.kelas.KelasSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KelasCardAdapter extends RecyclerView.Adapter<KelasCardAdapter.KelasCardViewHolder> implements Filterable {

    private final NavController controller;
    private Context context;

    private final Kelas kelas;
    private final List<KelasData> listData, listAll;
    private Long angkatan;

    public KelasCardAdapter(List<KelasData> list, NavController controller, Kelas kelas) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = controller;
        this.kelas = kelas;
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
        KelasData data = listData.get(position);

        if (this.angkatan != data.getAngkatan()) {
            this.angkatan = data.getAngkatan();
            holder.sectionText.setText("Angkatan " + data.getAngkatan());
            holder.section.setVisibility(View.VISIBLE);
        }
        holder.nama_kelas.setText(data.getNamaKelas());
        holder.angkatan.setText("Angkatan " + data.getAngkatan());
        UtilsUI.nicknameBuilder(context.getApplicationContext(), data.getNamaKelas(), holder.nick, holder.nickFrame);
        holder.cardView.setOnClickListener(v -> {
            KelasSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(KelasSharedModel.class);
            sharedModel.updateData(data);
            controller.navigate(R.id.action_kelas_petugas_to_siswa_petugas);
        });
        UtilsUI.simpleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull KelasCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.clearAnimation();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<KelasData> filteredlist = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredlist.addAll(listAll);
                } else {
                    for (KelasData data : listAll) {
                        if (data.getNamaKelas().toLowerCase().contains(constraint.toString().toLowerCase()) || String.valueOf(data.getAngkatan()).contains(constraint.toString().toLowerCase()))
                            filteredlist.add(data);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listData.clear();
                listData.addAll((Collection<? extends KelasData>) results.values);
                kelas.notFoundHandling(((Collection<?>) results.values).size() == 0);
                notifyDataSetChanged();
            }
        };
    }

    public static class KelasCardViewHolder extends RecyclerView.ViewHolder {
        TextView nama_kelas, angkatan, nick, sectionText;
        CardView cardView;
        FrameLayout nickFrame;
        RelativeLayout section;

        public KelasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_kelas = itemView.findViewById(R.id.title);
            angkatan = itemView.findViewById(R.id.secondaryText);
            nick = itemView.findViewById(R.id.nick);

            cardView = itemView.findViewById(R.id.card);
            nickFrame = itemView.findViewById(R.id.nickFrame);

            section = itemView.findViewById(R.id.section);
            sectionText = itemView.findViewById(R.id.sectionText);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
