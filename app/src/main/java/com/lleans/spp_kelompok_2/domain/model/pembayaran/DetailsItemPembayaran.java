package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailsItemPembayaran implements Serializable {

	@SerializedName("siswa")
	private SiswaPembayaran siswaPembayaran;

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
	private PetugasPembayaran petugasPembayaran;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("spp")
	private SppPembayaran sppPembayaran;

	@SerializedName("tahun_spp")
	private int tahunSpp;

	@SerializedName("id_spp")
	private int idSpp;

	@SerializedName("jumlah_bayar")
	private int jumlahBayar;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setSiswa(SiswaPembayaran siswaPembayaran){
		this.siswaPembayaran = siswaPembayaran;
	}

	public SiswaPembayaran getSiswa(){
		return siswaPembayaran;
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

	public void setPetugas(PetugasPembayaran petugasPembayaran){
		this.petugasPembayaran = petugasPembayaran;
	}

	public PetugasPembayaran getPetugas(){
		return petugasPembayaran;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSpp(SppPembayaran sppPembayaran){
		this.sppPembayaran = sppPembayaran;
	}

	public SppPembayaran getSpp(){
		return sppPembayaran;
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

	public void setJumlahBayar(int jumlahBayar){
		this.jumlahBayar = jumlahBayar;
	}

	public int getJumlahBayar(){
		return jumlahBayar;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}