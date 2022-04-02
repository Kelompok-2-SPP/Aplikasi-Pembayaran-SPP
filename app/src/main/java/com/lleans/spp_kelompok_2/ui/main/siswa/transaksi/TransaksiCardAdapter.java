package com.lleans.spp_kelompok_2.ui.main.siswa.transaksi;

import android.content.Context;
import android.util.Log;
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
import java.util.Collection;
import java.util.List;

public class TransaksiCardAdapter extends RecyclerView.Adapter<TransaksiCardAdapter.TransaksiCardViewHolder> implements Filterable {

    private final NavController controller;
    private Context context;

    private final List<PembayaranData> listData, listAll;
    private int color, count, tahun;
    private final boolean fromHomepage;

    public TransaksiCardAdapter(List<PembayaranData> list, NavController controller, boolean fromHomepage) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = controller;
        this.fromHomepage = fromHomepage;
    }

    @NonNull
    @Override
    public TransaksiCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_transaksi, parent, false);

        color = view.getResources().getColor(R.color.orange);
        context = view.getContext();
        return new TransaksiCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransaksiCardViewHolder holder, int position) {

        PembayaranData data = listData.get(position);
        if (this.tahun != data.getTahunSpp() && !fromHomepage) {
            this.tahun = data.getTahunSpp();
            holder.sectionText.setText("Tahun " + data.getTahunSpp());
            holder.section.setVisibility(View.VISIBLE);
        }
        holder.title.setText(Utils.parseLongtoStringDate(Utils.parseServerStringtoLongDate(data.getTahunSpp() + "-" + data.getBulanSpp(), "yyyy-MM"), "yyyy • MMMM"));
        if (data.getSpp() != null && Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar())) {
            holder.nominal.setText(Utils.kurangBayar(data.getSpp().getNominal(), data.getJumlahBayar()));
            holder.status.setText("Belum Lunas");
            holder.status.setTextColor(color);
        } else {
            holder.nominal.setText(Utils.formatRupiah(data.getJumlahBayar()));
            holder.status.setText("Lunas");
        }
        holder.cardView.setOnClickListener(v -> {
            PembayaranSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(PembayaranSharedModel.class);
            sharedModel.updateData(data);
            if (fromHomepage) {
                controller.navigate(R.id.action_homepage_siswa_to_rincianTransaksi_siswa2);
            } else {
                controller.navigate(R.id.action_transaksi_siswa_to_rincianTransaksi_siswa);
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
    public void onViewDetachedFromWindow(@NonNull TransaksiCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.clearAnimation();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                int year = Integer.parseInt(constraint.toString());
                List<PembayaranData> filteredlist = new ArrayList<>();

                for (PembayaranData data : listAll) {
                    if (data.getTahunSpp() == year) {
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
                notifyDataSetChanged();
            }
        };
    }

    public static class TransaksiCardViewHolder extends RecyclerView.ViewHolder {
        TextView title, status, nominal, sectionText;
        CardView cardView;
        RelativeLayout section;

        public TransaksiCardViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            status = itemView.findViewById(R.id.statusTransaksi);
            nominal = itemView.findViewById(R.id.totalTransaksi);

            cardView = itemView.findViewById(R.id.card);
            section = itemView.findViewById(R.id.section);
            sectionText = itemView.findViewById(R.id.sectionText);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }

}
