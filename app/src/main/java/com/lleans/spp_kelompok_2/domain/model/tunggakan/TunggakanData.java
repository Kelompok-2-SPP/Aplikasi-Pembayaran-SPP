package com.lleans.spp_kelompok_2.domain.model.tunggakan;

import com.google.gson.annotations.SerializedName;

public class TunggakanData {

	@SerializedName("details")
	private DetailsItemTunggakan detailsItemTunggakan;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setDetails(DetailsItemTunggakan detailsItemTunggakan){
		this.detailsItemTunggakan = detailsItemTunggakan;
	}

	public DetailsItemTunggakan getDetails(){
		return detailsItemTunggakan;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}