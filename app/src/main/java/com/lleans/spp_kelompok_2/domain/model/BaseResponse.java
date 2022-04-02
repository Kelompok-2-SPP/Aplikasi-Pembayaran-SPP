package com.lleans.spp_kelompok_2.domain.model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<interfaceClass> {

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("details")
    private interfaceClass details;

    public void setStatus(Integer status) { this.status = status; }

    public Integer getStatus() { return status; }

    public void setMessage(String message) { this.message = message; }

    public String getMessage() { return message;}

    public void setDetails(interfaceClass details) { this.details = details; }

    public interfaceClass getDetails() { return details; }
}
