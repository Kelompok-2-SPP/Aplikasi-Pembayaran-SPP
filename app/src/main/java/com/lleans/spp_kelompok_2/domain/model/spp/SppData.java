package com.lleans.spp_kelompok_2.domain.model.spp;

import com.google.gson.annotations.SerializedName;

public class SppData {

    @SerializedName("details")
    private DetailsItemSpp details;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public void setDetails(DetailsItemSpp details) {
        this.details = details;
    }

    public DetailsItemSpp getDetails() {
        return details;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}