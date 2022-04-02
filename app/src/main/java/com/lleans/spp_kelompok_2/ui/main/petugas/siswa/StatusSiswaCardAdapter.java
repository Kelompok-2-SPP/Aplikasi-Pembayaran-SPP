package com.lleans.spp_kelompok_2.ui.main.petugas.siswa;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lleans.spp_kelompok_2.R;
import com.lleans.spp_kelompok_2.domain.Utils;
import com.lleans.spp_kelompok_2.domain.model.BaseResponse;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranData;
import com.lleans.spp_kelompok_2.domain.model.pembayaran.PembayaranSharedModel;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.network.ApiInterface;
import com.lleans.spp_kelompok_2.ui.launcher.LauncherFragment;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;
import com.lleans.spp_kelompok_2.ui.utils.MoneyTextWatcher;
import com.lleans.spp_kelompok_2.ui.utils.UtilsUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusSiswaCardAdapter extends RecyclerView.Adapter<StatusSiswaCardAdapter.StatusCardViewHolder> implements Filterable {

    private final NavController controller;
    private final ApiInterface apiInterface;
    private InputMethodManager inputMethodManager;
    private SessionManager sessionManager;
    private Context context;

    private final List<PembayaranData> listData, listAll;
    private int orange, green, neutral, tahun;
    private final StatusSiswa statusSiswa;

    public StatusSiswaCardAdapter(List<PembayaranData> list, NavController controller, ApiInterface apiInterface, StatusSiswa statusSiswa) {
        this.listData = list;
        this.listAll = new ArrayList<>(list);
        this.controller = controller;
        this.apiInterface = apiInterface;
        this.statusSiswa = statusSiswa;
    }

    private void localUpdate(int postion, long jumlahBayar) {
        PembayaranData obj = listData.get(postion);
        PetugasData pet = new PetugasData();

        pet.setIdPetugas(Integer.parseInt(sessionManager.getUserDetail().get(SessionManager.ID)));
        pet.setNamaPetugas(sessionManager.getUserDetail().get(SessionManager.USERNAME));
        obj.setIdPetugas(Integer.parseInt(sessionManager.getUserDetail().get(SessionManager.ID)));
        obj.setPetugas(pet);
        obj.setTglBayar(Utils.getCurrentDateAndTime("yyyy-MM-dd"));
        obj.setJumlahBayar(jumlahBayar);
        listData.set(postion, obj);
    }

    private void updateStatus(int idPembayaran, long jumlahPembayaran, int position) {
        localUpdate(position, jumlahPembayaran);
        Call<BaseResponse<PembayaranData>> updateStatus;

        updateStatus = apiInterface.putPembayaran(
                idPembayaran,
                Integer.valueOf(sessionManager.getUserDetail().get(SessionManager.ID)),
                null,
                Utils.getCurrentDateAndTime("yyyy-MM-dd"),
                null,
                null,
                null,
                jumlahPembayaran
        );
        updateStatus.enqueue(new Callback<BaseResponse<PembayaranData>>() {
            @Override
            public void onResponse(Call<BaseResponse<PembayaranData>> call, Response<BaseResponse<PembayaranData>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    listData.set(position, response.body().getDetails());
                } else {
                    try {
                        BaseResponse message = new Gson().fromJson(response.errorBody().charStream(), BaseResponse.class);
                        UtilsUI.toaster(context, message.getMessage());
                    } catch (Exception e) {
                        try {
                            UtilsUI.dialog(context, "Something went wrong!", response.errorBody().string(), false).show();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<PembayaranData>> call, @NonNull Throwable t) {
                UtilsUI.dialog(context, "Something went wrong!", t.getLocalizedMessage(), false).show();
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
        inputMethodManager = (InputMethodManager) parent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        PembayaranData data = listData.get(position);

        if (this.tahun != data.getTahunSpp()) {
            this.tahun = data.getTahunSpp();
            holder.sectionText.setText("Tahun " + data.getTahunSpp());
            holder.section.setVisibility(View.VISIBLE);
        }
        if (data.getSpp() != null) {
            holder.sudBayar.addTextChangedListener(new MoneyTextWatcher(holder.sudBayar, data.getSpp().getNominal()));
            setStatus(!Utils.statusPembayaran(data.getSpp().getNominal(), data.getJumlahBayar()), holder);
            holder.totSpp.setText(Utils.formatRupiah(data.getSpp().getNominal()));
        } else {
            setStatus(true, holder);
            holder.sudBayar.addTextChangedListener(new MoneyTextWatcher(holder.sudBayar, data.getJumlahBayar()));
        }
        holder.sudBayar.setText(String.valueOf(data.getJumlahBayar()));
        holder.bulan.setText(Utils.parseLongtoStringDate(Utils.parseServerStringtoLongDate(String.valueOf(data.getBulanSpp()), "MM"), "MMMM"));
        holder.tahun.setText("Status SPP tahun " + data.getTahunSpp());
        holder.status.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    setStatus(false, holder);
                    holder.sudBayar.requestFocus();
                    inputMethodManager.showSoftInput(holder.sudBayar, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    setStatus(true, holder);
                    inputMethodManager.hideSoftInputFromWindow(holder.sudBayar.getWindowToken(), 0);
                    if (data.getSpp() != null) {
                        updateStatus(data.getIdPembayaran(), data.getSpp().getNominal(), holder.getAdapterPosition());
                    } else {
                        updateStatus(data.getIdPembayaran(), data.getJumlahBayar(), holder.getAdapterPosition());
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        holder.sudBayar.setOnEditorActionListener((v, actionId, event) -> {
            v.clearFocus();
            long unformatted = Utils.unformatRupiah(holder.sudBayar.getText().toString());
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                if (unformatted == data.getJumlahBayar()) {
                    notifyItemChanged(holder.getAdapterPosition());
                } else if (unformatted != data.getJumlahBayar()) {
                    updateStatus(data.getIdPembayaran(), unformatted, holder.getAdapterPosition());
                }
            }
            return false;
        });
        holder.card.setOnClickListener(v -> {
            PembayaranSharedModel sharedModel = new ViewModelProvider((LauncherFragment) context).get(PembayaranSharedModel.class);
            sharedModel.updateData(data);
            controller.navigate(R.id.action_status_siswa_to_rincianTransaksi_siswa2);
        });
        UtilsUI.simpleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull StatusCardViewHolder holder) {
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
                statusSiswa.notFoundHandling(((Collection<?>) results.values).size() == 0);
                tahun = 0;
                notifyDataSetChanged();
            }
        };
    }

    public static class StatusCardViewHolder extends RecyclerView.ViewHolder {
        TextView bulan, tahun, totSpp, sectionText;
        EditText sudBayar;

        ConstraintLayout belumLunas;
        CardView card;
        RelativeLayout section;
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

            section = itemView.findViewById(R.id.section);
            sectionText = itemView.findViewById(R.id.sectionText);
        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }
    }

}
