package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.DetailsItemPembayaran;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranSharedModel;
import com.lleans.spp_kelompok_2.network.ApiClient;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusSiswaCardAdapter extends RecyclerView.Adapter<StatusSiswaCardAdapter.StatusCardViewHolder> {

    private final List<DetailsItemPembayaran> listData;
    private final NavController navController;
    private InputMethodManager imm;
    private SessionManager sessionManager;
    private Context context;

    private int orange, green, neutral;

    public StatusSiswaCardAdapter(List<DetailsItemPembayaran> list, NavController navController) {
        this.listData = list;
        this.navController = navController;
    }

    private void updateStatus(int idPembayaran, long jumlahPembayaran, int position) {
        Call<PembayaranData> updateStatus;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        updateStatus = apiInterface.putPembayaran(
                "Bearer " + sessionManager.getUserDetail().get(SessionManager.TOKEN),
                idPembayaran,
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                null,
                Utils.getCurrentDateAndTime("yyyy-MM-dd"),
                null,
                null,
                null,
                jumlahPembayaran
        );
        updateStatus.enqueue(new Callback<PembayaranData>() {
            @Override
            public void onResponse(Call<PembayaranData> call, Response<PembayaranData> response) {
                if (response.body() != null && response.isSuccessful()) {
                    listData.get(position).setJumlahBayar(response.body().getDetails().getJumlahBayar());
                    notifyItemChanged(position);
                } else {
                    try {
                        PembayaranData message = new Gson().fromJson(response.errorBody().charStream(), PembayaranData.class);
                        toaster(message.getMessage());
                    } catch (Exception e) {
                        try {
                            dialog("Something went wrong !", Html.fromHtml(response.errorBody().string()));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            // On failure response
            @Override
            public void onFailure(@NonNull Call<PembayaranData> call, @NonNull Throwable t) {
                dialog("Something went wrong !", Html.fromHtml(t.getLocalizedMessage()));
            }
        });
    }

    @NonNull
    @Override
    public StatusCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_status, parent, false);
        sessionManager = new SessionManager(view.getContext());
        orange = parent.getResources().getColor(R.color.orange);
        green = parent.getResources().getColor(R.color.green);
        neutral = parent.getResources().getColor(R.color.neutral_white);
        context = parent.getContext();
        imm = (InputMethodManager) parent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return new StatusCardViewHolder(view);
    }

    private void setStatus(boolean lunas, @NonNull StatusCardViewHolder holder) {
        if (lunas) {
            holder.status.getTabAt(0).select();
            holder.belumLunas.setVisibility(View.GONE);
            holder.card.setCardBackgroundColor(green);
            holder.status.setSelectedTabIndicatorColor(green);
            holder.status.setTabTextColors(green, neutral);
            holder.status.setTabRippleColor(ColorStateList.valueOf(green));
            holder.totSpp.setTextColor(green);
        } else {
            holder.status.getTabAt(1).select();
            holder.belumLunas.setVisibility(View.VISIBLE);
            holder.card.setCardBackgroundColor(orange);
            holder.status.setSelectedTabIndicatorColor(orange);
            holder.status.setTabTextColors(orange, neutral);
            holder.status.setTabRippleColor(ColorStateList.valueOf(orange));
            holder.totSpp.setTextColor(orange);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull StatusCardViewHolder holder, int position) {
        DetailsItemPembayaran data = listData.get(position);
        holder.sudBayar.addTextChangedListener(new MoneyTextWatcher(holder.sudBayar, data.getSpp().getNominal()));

        holder.bulan.setText(Utils.getMonth(data.getBulanSpp()));
        holder.tahun.setText("Status SPP tahun " + data.getTahunSpp());
        holder.totSpp.setText(Utils.formatRupiah(data.getSpp().getNominal()));
        holder.sudBayar.setText(String.valueOf(data.getJumlahBayar()));

        setStatus(!Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar()), holder);

        holder.status.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    setStatus(false, holder);
                    holder.sudBayar.requestFocus();
                    imm.showSoftInput(holder.sudBayar, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    setStatus(true, holder);
                    imm.hideSoftInputFromWindow(holder.sudBayar.getWindowToken(), 0);
                    updateStatus(data.getIdPembayaran(), data.getSpp().getNominal(), holder.getAdapterPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        holder.sudBayar.setOnFocusChangeListener((v, hasFocus) -> {
            long unformatted = Utils.unformatRupiah(holder.sudBayar.getText().toString());
            if (!hasFocus && unformatted == data.getJumlahBayar()) {
                notifyItemChanged(holder.getAdapterPosition());
            } else if (!hasFocus && unformatted != data.getJumlahBayar()) {
                updateStatus(data.getIdPembayaran(), unformatted, holder.getAdapterPosition());
            }
        });

        holder.card.setOnClickListener(v -> {
            PembayaranSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(PembayaranSharedModel.class);
            sharedModel.updateData(data);
            navController.navigate(R.id.action_status_siswa_to_rincianTransaksi_siswa2);
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class StatusCardViewHolder extends RecyclerView.ViewHolder {
        TextView bulan, tahun, totSpp;
        EditText sudBayar;

        ConstraintLayout belumLunas;
        CardView card;
        TabLayout status;

        public StatusCardViewHolder(@NonNull View itemView) {
            super(itemView);
            bulan = itemView.findViewById(R.id.bulan);
            tahun = itemView.findViewById(R.id.tahun);
            totSpp = itemView.findViewById(R.id.totalSpp);
            sudBayar = itemView.findViewById(R.id.sudahBayar);

            belumLunas = itemView.findViewById(R.id.belumBayar);
            status = itemView.findViewById(R.id.tabStatus);
            card = itemView.findViewById(R.id.card);
        }
    }

    private void toaster(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void dialog(String title, Spanned text) {
        MaterialAlertDialogBuilder as = new MaterialAlertDialogBuilder(context);
        as.setTitle(title).setMessage(text).setPositiveButton("Ok", null).show();
    }
}
