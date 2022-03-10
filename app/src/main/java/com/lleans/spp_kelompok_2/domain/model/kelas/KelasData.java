package com.lleans.spp_kelompok_2.domain.model.kelas;

import com.google.gson.annotations.SerializedName;

public class KelasData {

    @SerializedName("details")
    private DetailsItemKelas details;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public void setDetails(DetailsItemKelas details) {
        this.details = details;
    }

    public DetailsItemKelas getDetails() {
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