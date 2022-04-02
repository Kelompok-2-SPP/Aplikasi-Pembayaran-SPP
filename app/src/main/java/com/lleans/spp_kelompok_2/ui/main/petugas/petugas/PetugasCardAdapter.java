package com.lleans.spp_kelompok_2.ui.main.petugas.petugas;

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
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PetugasCardAdapter extends RecyclerView.Adapter<PetugasCardAdapter.PetugasCardViewHolder> implements Filterable {

    private final NavController controller;
    private Context context;

    private final List<PetugasData> listData, listAll;
    private final Petugas petugas;

    public PetugasCardAdapter(List<PetugasData> list, NavController controller, Petugas petugas) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = controller;
        this.petugas = petugas;
    }

    @NonNull
    @Override
    public PetugasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_main, parent, false);

        context = view.getContext();
        return new PetugasCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PetugasCardViewHolder holder, int position) {
        PetugasData data = listData.get(position);

        holder.name.setText(data.getNamaPetugas());
        holder.uname.setText(data.getUsername());
        UtilsUI.nicknameBuilder(context.getApplicationContext(), data.getNamaPetugas(), holder.nick, holder.nickFrame);
        holder.cardView.setOnClickListener(v -> {
            PetugasSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(PetugasSharedModel.class);
            sharedModel.updateData(data);
            controller.navigate(R.id.action_petugas_petugas_to_detailPetugas);
        });
        UtilsUI.simpleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PetugasCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.clearAnimation();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<PetugasData> filteredlist = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredlist.addAll(listAll);
                } else {
                    for (PetugasData data : listAll) {
                        if (data.getNamaPetugas().toLowerCase().contains(constraint.toString().toLowerCase()) || data.getUsername().toLowerCase().contains(constraint.toString().toLowerCase()))
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
                listData.addAll((Collection<? extends PetugasData>) results.values);
                petugas.notFoundHandling(((Collection<?>) results.values).size() == 0);
                notifyDataSetChanged();
            }
        };
    }

    public static class PetugasCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, uname, nick;
        CardView cardView;
        FrameLayout nickFrame;

        public PetugasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            uname = itemView.findViewById(R.id.secondaryText);
            nick = itemView.findViewById(R.id.nick);

            cardView = itemView.findViewById(R.id.card);
            nickFrame = itemView.findViewById(R.id.nickFrame);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }
}
