package com.lleans.spp_kelompok_2.domain.model.siswa;

import com.google.gson.annotations.SerializedName;

public class DetailsItemSiswa {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("nama")
	private String nama;

	@SerializedName("nisn")
	private String nisn;

	@SerializedName("nis")
	private String nis;

	@SerializedName("id_kelas")
	private int idKelas;

	@SerializedName("no_telp")
	private String noTelp;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setNisn(String nisn){
		this.nisn = nisn;
	}

	public String getNisn(){
		return nisn;
	}

	public void setNis(String nis){
		this.nis = nis;
	}

	public String getNis(){
		return nis;
	}

	public void setIdKelas(int idKelas){
		this.idKelas = idKelas;
	}

	public int getIdKelas(){
		return idKelas;
	}

	public void setNoTelp(String noTelp){
		this.noTelp = noTelp;
	}

	public String getNoTelp(){
		return noTelp;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}