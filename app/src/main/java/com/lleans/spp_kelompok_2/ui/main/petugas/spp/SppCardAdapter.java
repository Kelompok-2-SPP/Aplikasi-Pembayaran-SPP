package com.lleans.spp_kelompok_2.ui.main.petugas.spp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SppCardAdapter extends RecyclerView.Adapter<SppCardAdapter.SppCardViewHolder> implements Filterable {

    private final NavController controller;
    private Context context;

    private final List<SppData> listData, listAll;
    private final boolean fromHomepage;
    private int count, orange, tahun;
    private Spp spp;

    public SppCardAdapter(List<SppData> list, NavController controller, boolean fromHomepage, @Nullable Spp spp) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = controller;
        this.fromHomepage = fromHomepage;
        this.spp = spp;
    }

    @NonNull
    @Override
    public SppCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_spp, parent, false);

        context = view.getContext();
        orange = view.getResources().getColor(R.color.orange);
        return new SppCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SppCardViewHolder holder, int position) {
        SppData data = listData.get(position);

        if (this.tahun != data.getTahun() && !fromHomepage) {
            this.tahun = data.getTahun();
            holder.sectionText.setText("Tahun " + data.getTahun());
            holder.section.setVisibility(View.VISIBLE);
        }
        holder.judul.setText("Total SPP\nAngkatan " + data.getAngkatan());
        holder.tahun.setText("Tahun " + data.getTahun());
        holder.nominal.setText(Utils.formatRupiah(data.getNominal()));
        if (data.getNominal() <= 400000) {
            holder.cardView.setCardBackgroundColor(orange);
            holder.nominal.setTextColor(orange);
        }
        holder.cardView.setOnClickListener(v -> {
            SppSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(SppSharedModel.class);
            sharedModel.updateData(data);
            if (fromHomepage) {
                controller.navigate(R.id.action_homepage_petugas_to_rincianSpp_petugas);
            } else {
                controller.navigate(R.id.action_spp_petugas_to_rincianSpp_petugas);
            }
        });
        UtilsUI.simpleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        if (count >= listData.size() || count == 0) {
            return listData.size();
        } else {
            return count;
        }
    }

    public int setItemCount(int count) {
        return this.count = count;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SppCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.clearAnimation();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                int year = Integer.parseInt(constraint.toString());
                List<SppData> filteredlist = new ArrayList<>();

                for (SppData data : listAll) {
                    if (data.getTahun() == year) {
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
                listData.addAll((Collection<? extends SppData>) results.values);
                spp.notFoundHandling(((Collection<?>) results.values).size() == 0);
                notifyDataSetChanged();
            }
        };
    }

    public static class SppCardViewHolder extends RecyclerView.ViewHolder {
        TextView judul, tahun, nominal, sectionText;
        CardView cardView;
        RelativeLayout section;

        public SppCardViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.titleSpp);
            tahun = itemView.findViewById(R.id.tahunSpp);
            nominal = itemView.findViewById(R.id.jumlahSpp);

            cardView = itemView.findViewById(R.id.card_spp);
            section = itemView.findViewById(R.id.section);
            sectionText = itemView.findViewById(R.id.sectionText);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }

}
