package com.lleans.spp_kelompok_2.domain.model.spp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailsItemSpp implements Serializable {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("tahun")
	private int tahun;

	@SerializedName("nominal")
	private int nominal;

	@SerializedName("id_spp")
	private int idSpp;

	@SerializedName("angkatan")
	private int angkatan;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTahun(int tahun){
		this.tahun = tahun;
	}

	public int getTahun(){
		return tahun;
	}

	public void setNominal(int nominal){
		this.nominal = nominal;
	}

	public int getNominal(){
		return nominal;
	}

	public void setIdSpp(int idSpp){
		this.idSpp = idSpp;
	}

	public int getIdSpp(){
		return idSpp;
	}

	public void setAngkatan(int angkatan){
		this.angkatan = angkatan;
	}

	public int getAngkatan(){
		return angkatan;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}