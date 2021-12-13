package com.lleans.spp_kelompok_2.model.auth;

import com.google.gson.annotations.SerializedName;

public class DetailsItemAuth {

	@SerializedName("logged")
	private boolean logged;

	@SerializedName("token")
	private String token;

	public void setLogged(boolean logged){
		this.logged = logged;
	}

	public boolean isLogged(){
		return logged;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}