package com.lleans.spp_kelompok_2.model.auth;

import com.google.gson.annotations.SerializedName;

public class AuthData {

	@SerializedName("details")
	private DetailsItemAuth detailsItemAuth;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setDetails(DetailsItemAuth detailsItemAuth){
		this.detailsItemAuth = detailsItemAuth;
	}

	public DetailsItemAuth getDetails(){
		return detailsItemAuth;
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