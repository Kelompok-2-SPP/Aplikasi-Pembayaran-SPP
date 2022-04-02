package com.lleans.spp_kelompok_2.domain.model;

import com.google.gson.annotations.SerializedName;

public class AuthData {

	@SerializedName("logged")
	private Boolean logged;

	@SerializedName("token")
	private String token;

	public void setLogged(Boolean logged){ this.logged = logged; }

	public boolean isLogged(){ return logged; }

	public void setToken(String token){ this.token = token; }

	public String getToken(){ return token; }
}