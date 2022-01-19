package com.lleans.spp_kelompok_2.domain.model.petugas;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailsItemPetugas implements Serializable {

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("level")
	private String level;

	@SerializedName("nama_petugas")
	private String namaPetugas;

	@SerializedName("id_petugas")
	private int idPetugas;

	@SerializedName("username")
	private String username;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setLevel(String level){
		this.level = level;
	}

	public String getLevel(){
		return level;
	}

	public void setNamaPetugas(String namaPetugas){
		this.namaPetugas = namaPetugas;
	}

	public String getNamaPetugas(){
		return namaPetugas;
	}

	public void setIdPetugas(int idPetugas){
		this.idPetugas = idPetugas;
	}

	public int getIdPetugas(){
		return idPetugas;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}