package com.lleans.spp_kelompok_2.domain.model.pembayaran;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PembayaranData {

    @SerializedName("details")
    private DetailsItemPembayaran details;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public void setDetails(DetailsItemPembayaran details) {
        this.details = details;
    }

    public DetailsItemPembayaran getDetails() {
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