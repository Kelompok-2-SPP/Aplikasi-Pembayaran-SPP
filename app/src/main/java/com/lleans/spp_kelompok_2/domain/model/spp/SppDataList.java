package com.lleans.spp_kelompok_2.domain.model.spp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SppDataList {

	@SerializedName("details")
	private List<DetailsItemSpp> details;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setDetails(List<DetailsItemSpp> details){
		this.details = details;
	}

	public List<DetailsItemSpp> getDetails(){
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