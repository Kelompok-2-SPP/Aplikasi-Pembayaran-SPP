package com.lleans.spp_kelompok_2.domain.model.spp;

import com.google.gson.annotations.SerializedName;

public class SppData {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("tahun")
    private Long tahun;

    @SerializedName("nominal")
    private Long nominal;

    @SerializedName("id_spp")
    private int idSpp;

    @SerializedName("angkatan")
    private Long angkatan;

    @SerializedName("updatedAt")
    private String updatedAt;

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setTahun(Long tahun){
        this.tahun = tahun;
    }

    public Long getTahun(){
        return tahun;
    }

    public void setNominal(Long nominal){
        this.nominal = nominal;
    }

    public Long getNominal(){
        return nominal;
    }

    public void setIdSpp(int idSpp){
        this.idSpp = idSpp;
    }

    public int getIdSpp(){
        return idSpp;
    }

    public void setAngkatan(Long angkatan){
        this.angkatan = angkatan;
    }

    public Long getAngkatan(){
        return angkatan;
    }

    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt(){
        return updatedAt;
    }
}