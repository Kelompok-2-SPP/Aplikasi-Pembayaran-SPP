package com.lleans.spp_kelompok_2.model.pembayaran;

import com.google.gson.annotations.SerializedName;

public class DetailsItemPembayaran {

	@SerializedName("siswa")
	private SiswaPembayaran siswaPembayaran;

	@SerializedName("bulan_dibayar")
	private int bulanDibayar;

	@SerializedName("id_pembayaran")
	private int idPembayaran;

	@SerializedName("nisn")
	private String nisn;

	@SerializedName("id_petugas")
	private int idPetugas;

	@SerializedName("tgl_dibayar")
	private String tglDibayar;

	@SerializedName("petugas")
	private PetugasPembayaran petugasPembayaran;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("spp")
	private SppPembayaran sppPembayaran;

	@SerializedName("tahun_dibayar")
	private int tahunDibayar;

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

	public void setBulanDibayar(int bulanDibayar){
		this.bulanDibayar = bulanDibayar;
	}

	public int getBulanDibayar(){
		return bulanDibayar;
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

	public void setTglDibayar(String tglDibayar){
		this.tglDibayar = tglDibayar;
	}

	public String getTglDibayar(){
		return tglDibayar;
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

	public void setTahunDibayar(int tahunDibayar){
		this.tahunDibayar = tahunDibayar;
	}

	public int getTahunDibayar(){
		return tahunDibayar;
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