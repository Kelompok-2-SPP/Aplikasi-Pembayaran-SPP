package com.lleans.spp_kelompok_2.domain.model.tunggakan;

import com.google.gson.annotations.SerializedName;
import com.lleans.spp_kelompok_2.domain.model.spp.SppData;

public class PembayaranTunggakan {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("spp")
	private SppData spp;

	@SerializedName("bulan_spp")
	private int bulanSpp;

	@SerializedName("id_pembayaran")
	private int idPembayaran;

	@SerializedName("tahun_spp")
	private int tahunSpp;

	@SerializedName("jumlah_bayar")
	private Long jumlahBayar;

	@SerializedName("tgl_bayar")
	private String tglBayar;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSpp(SppData sppTunggakan){
		this.spp = sppTunggakan;
	}

	public SppData getSpp(){
		return spp;
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

	public void setTahunSpp(int tahunSpp){
		this.tahunSpp = tahunSpp;
	}

	public int getTahunSpp(){
		return tahunSpp;
	}

	public void setJumlahBayar(Long jumlahBayar){
		this.jumlahBayar = jumlahBayar;
	}

	public Long getJumlahBayar(){
		return jumlahBayar;
	}

	public void setTglBayar(String tglBayar){
		this.tglBayar = tglBayar;
	}

	public String getTglBayar(){
		return tglBayar;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}