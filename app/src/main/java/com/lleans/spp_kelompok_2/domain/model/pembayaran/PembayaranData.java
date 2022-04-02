package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.lleans.spp_kelompok_2.domain.model.petugas.PetugasData;
import com.lleans.spp_kelompok_2.domain.model.siswa.SiswaData;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;

public class PembayaranData {

	@SerializedName("siswa")
	private SiswaData siswa;

	@SerializedName("bulan_spp")
	private int bulanSpp;

	@SerializedName("id_pembayaran")
	private int idPembayaran;

	@SerializedName("nisn")
	private String nisn;

	@SerializedName("id_petugas")
	private int idPetugas;

	@SerializedName("tgl_bayar")
	private String tglBayar;
	
	@SerializedName("petugas")
	private PetugasData petugas;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("spp")
	private SppData spp;

	@SerializedName("tahun_spp")
	private int tahunSpp;

	@SerializedName("id_spp")
	private int idSpp;

	@SerializedName("jumlah_bayar")
	private Long jumlahBayar;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setSiswa(SiswaData detailsItemSiswa){
		this.siswa = detailsItemSiswa;
	}

	public SiswaData getSiswa(){
		return siswa;
	}

	public void setBulanSpp(int bulanSpp){
		this.bulanSpp = bulanSpp;
	}

	public int getBulanSpp(){
		return bulanSpp;
	}

	public void setIdPembayaran(int idPembayaran){
		this.idPembayaran = idPembayaran;
	}

	public int getIdPembayaran(){
		return idPembayaran;
	}

	public void setNisn(String nisn){
		this.nisn = nisn;
	}

	public String getNisn(){
		return nisn;
	}

	public void setIdPetugas(int idPetugas){
		this.idPetugas = idPetugas;
	}

	public int getIdPetugas(){
		return idPetugas;
	}

	public void setTglBayar(String tglBayar){
		this.tglBayar = tglBayar;
	}

	public String getTglBayar(){
		return tglBayar;
	}

	public void setPetugas(PetugasData petugasPembayaran){
		this.petugas = petugasPembayaran;
	}

	public PetugasData getPetugas(){
		return petugas;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSpp(SppData sppPembayaran){
		this.spp = sppPembayaran;
	}

	public SppData getSpp(){
		return spp;
	}

	public void setTahunSpp(int tahunSpp){
		this.tahunSpp = tahunSpp;
	}

	public int getTahunSpp(){
		return tahunSpp;
	}

	public void setIdSpp(int idSpp){
		this.idSpp = idSpp;
	}

	public int getIdSpp(){
		return idSpp;
	}

	public void setJumlahBayar(Long jumlahBayar){
		this.jumlahBayar = jumlahBayar;
	}

	public Long getJumlahBayar(){
		return jumlahBayar;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}