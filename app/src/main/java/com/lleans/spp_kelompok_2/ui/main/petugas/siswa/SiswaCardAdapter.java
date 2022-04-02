package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SiswaCardAdapter extends RecyclerView.Adapter<SiswaCardAdapter.SiswaCardViewHolder> implements Filterable {

    private final NavController controller;
    private Context context;

    private final List<SiswaData> listData, listAll;

    public SiswaCardAdapter(List<SiswaData> list, NavController controller) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = controller;
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
        SiswaData data = listData.get(position);

        holder.name.setText(data.getNama());
        holder.nisn.setText(data.getNisn());
        UtilsUI.nicknameBuilder(context.getApplicationContext(), data.getNama(), holder.nick, holder.nickFrame);
        holder.cardView.setOnClickListener(v -> {
            SiswaSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(SiswaSharedModel.class);
            sharedModel.updateData(data);
            controller.navigate(R.id.action_siswa_petugas_to_detail_siswa);
        });
        UtilsUI.simpleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SiswaCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.clearAnimation();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<SiswaData> filteredlist = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredlist.addAll(listAll);
                } else {
                    for (SiswaData data : listAll) {
                        if (data.getNama().toLowerCase().contains(constraint.toString().toLowerCase()) || data.getNisn().contains(constraint.toString()))
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
                listData.addAll((Collection<? extends SiswaData>) results.values);
                notifyDataSetChanged();
            }
        };
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

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
