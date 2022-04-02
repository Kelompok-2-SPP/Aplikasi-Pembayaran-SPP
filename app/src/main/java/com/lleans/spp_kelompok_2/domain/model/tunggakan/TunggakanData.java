package com.lleans.spp_kelompok_2.domain.model.tunggakan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TunggakanData {

    @SerializedName("tunggakan")
    private List<PembayaranTunggakan> tunggakan;

    @SerializedName("nisn")
    private String nisn;

    @SerializedName("jumlah_tunggakan")
    private int jumlahTunggakan;

    @SerializedName("total_tunggakan")
    private Long totalTunggakan;

    public void setTunggakan(List<PembayaranTunggakan> tunggakan){
        this.tunggakan = tunggakan;
    }

    public List<PembayaranTunggakan> getTunggakan(){
        return tunggakan;
    }

    public void setNisn(String nisn){
        this.nisn = nisn;
    }

    public String getNisn(){
        return nisn;
    }

    public void setJumlahTunggakan(int jumlahTunggakan){
        this.jumlahTunggakan = jumlahTunggakan;
    }

    public int getJumlahTunggakan(){
        return jumlahTunggakan;
    }

    public void setTotalTunggakan(Long totalTunggakan){
        this.totalTunggakan = totalTunggakan;
    }

    public Long getTotalTunggakan(){
        return totalTunggakan;
    }
}