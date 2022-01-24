package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PembayaranDataList {

	@SerializedName("details")
	private List<DetailsItemPembayaran> details;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setDetails(List<DetailsItemPembayaran> details){
		this.details = details;
	}

	public List<DetailsItemPembayaran> getDetails(){
		return details;
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