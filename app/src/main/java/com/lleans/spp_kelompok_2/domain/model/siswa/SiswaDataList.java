package com.lleans.spp_kelompok_2.domain.model.siswa;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SiswaDataList {

	@SerializedName("details")
	private List<DetailsItemSiswa> details;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setDetails(List<DetailsItemSiswa> details){
		this.details = details;
	}

	public List<DetailsItemSiswa> getDetails(){
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