package com.ay_sooapp.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("price")
    @Expose
    private  String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
