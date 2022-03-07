package com.lleans.spp_kelompok_2.domain.model.tunggakan;

import com.google.gson.annotations.SerializedName;
import com.lleans.spp_kelompok_2.domain.model.spp.DetailsItemSpp;

public class PembayaranTunggakan {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("spp")
	private DetailsItemSpp spp;

	@SerializedName("bulan_spp")
	private int bulanSpp;

	@SerializedName("id_pembayaran")
	private int idPembayaran;

	@SerializedName("tahun_spp")
	private int tahunSpp;

	@SerializedName("jumlah_bayar")
	private Object jumlahBayar;

	@SerializedName("tgl_bayar")
	private Object tglBayar;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSpp(DetailsItemSpp sppTunggakan){
		this.spp = sppTunggakan;
	}

	public DetailsItemSpp getSpp(){
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

	public void setJumlahBayar(Object jumlahBayar){
		this.jumlahBayar = jumlahBayar;
	}

	public Object getJumlahBayar(){
		return jumlahBayar;
	}

	public void setTglBayar(Object tglBayar){
		this.tglBayar = tglBayar;
	}

	public Object getTglBayar(){
		return tglBayar;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}