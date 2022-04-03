package com.lleans.spp_kelompok_2.ui.main.petugas.histori;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranSharedModel;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class HistoriCardAdapter extends RecyclerView.Adapter<HistoriCardAdapter.HistoriCardViewHolder> implements Filterable {

    private final NavController controller;
    private Context context;

    private final List<PembayaranData> listData, listAll;
    private int orange, green;
    private String date;
    private final Histori histori;
    private final Calendar c1, c2;

    public HistoriCardAdapter(List<PembayaranData> list, NavController navController, Histori histori) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = navController;
        this.histori = histori;
        this.c1 = Calendar.getInstance();
        this.c1.add(Calendar.DAY_OF_YEAR, -1);
        this.c2 = Calendar.getInstance();
    }

    @NonNull
    @Override
    public HistoriCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_aktivitas, parent, false);

        orange = view.getResources().getColor(R.color.orange);
        green = view.getResources().getColor(R.color.green);
        context = view.getContext();
        return new HistoriCardViewHolder(view);
    }

    private void setSection(PembayaranData data, HistoriCardViewHolder holder) {
        String parsed = Utils.parseLongtoStringDate(Utils.parseServerStringtoLongDate(data.getUpdatedAt(), "yyyy-MM-dd"), "dd MMMM yyyy");
        c2.setTimeInMillis(Utils.parseServerStringtoLongDate(data.getUpdatedAt(), "yyyy-MM-dd"));
        if (!parsed.equals(date)) {
            this.date = parsed;
            if (parsed.equals(Utils.getCurrentDateAndTime("dd MMMM yyyy"))) {
                holder.sectionText.setText("Hari ini");
            } else if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                holder.sectionText.setText("Kemarin");
            } else {
                holder.sectionText.setText(parsed);
            }
            holder.section.setVisibility(View.VISIBLE);
        }
    }

    private void setHolder(PembayaranData data, HistoriCardViewHolder holder) {
        holder.name.setText(data.getSiswa().getNama());
        holder.kelas.setText(data.getSiswa().getKelas().getNamaKelas());
        if (data.getSpp() != null && Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar())) {
            holder.status.setText("Belum Lunas");
            holder.status.setTextColor(orange);
            holder.nominalkurang.setText(Utils.kurangBayar(data.getSpp().getNominal(), data.getJumlahBayar()));
        } else {
            holder.status.setText("Lunas");
            holder.status.setTextColor(green);
            holder.nominalkurang.setText(Utils.formatRupiah(data.getJumlahBayar()));
        }
        holder.cardView.setOnClickListener(v -> {
            PembayaranSharedModel shared = new ViewModelProvider((LauncherFragment) context).get(PembayaranSharedModel.class);
            shared.updateData(data);
            controller.navigate(R.id.action_histori_petugas_to_rincianTransaksi_siswa);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoriCardViewHolder holder, int position) {
        PembayaranData data = listData.get(position);

        setSection(data, holder);
        setHolder(data, holder);
        UtilsUI.simpleAnimation(holder.itemView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull HistoriCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.celarAnnimation();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String year = Utils.parseLongtoStringDate(Long.valueOf(constraint.toString()), "yyyy");
                String month = Utils.parseLongtoStringDate(Long.valueOf(constraint.toString()), "MM");
                List<PembayaranData> filteredlist = new ArrayList<>();

                for (PembayaranData data : listAll) {
                    long date = Utils.parseServerStringtoLongDate(data.getUpdatedAt(), "yyyy-MM");
                    if (Utils.parseLongtoStringDate(date, "yyyy").equals(year) && Utils.parseLongtoStringDate(date, "MM").equals(month)) {
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
                listData.addAll((Collection<? extends PembayaranData>) results.values);
                histori.notFoundHandling(((Collection<?>) results.values).size() == 0);
                notifyDataSetChanged();
            }
        };
    }

    public static class HistoriCardViewHolder extends RecyclerView.ViewHolder {
        TextView name, kelas, status, nominalkurang, sectionText;
        CardView cardView;
        RelativeLayout section;

        public HistoriCardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            kelas = itemView.findViewById(R.id.kelas);
            status = itemView.findViewById(R.id.status);
            nominalkurang = itemView.findViewById(R.id.nominal);

            cardView = itemView.findViewById(R.id.card);
            section = itemView.findViewById(R.id.section);
            sectionText = itemView.findViewById(R.id.sectionText);
        }

        public void celarAnnimation() {
            itemView.clearAnimation();
        }
    }

}
