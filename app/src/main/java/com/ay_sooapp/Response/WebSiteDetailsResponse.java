package com.ay_sooapp.Response;

import com.ay_sooapp.Model.WebSiteDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WebSiteDetailsResponse {
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("data")
    @Expose
    private List<WebSiteDetails> data = null;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<WebSiteDetails> getData() {
        return data;
    }

    public void setData(List<WebSiteDetails> data) {
        this.data = data;
    }

}
