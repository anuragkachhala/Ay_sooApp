package com.ay_sooapp.Response;

import com.ay_sooapp.Model.AlertDetailsData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlertDataResponse {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("data")
    @Expose
    private List<AlertDetailsData> data = null;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<AlertDetailsData> getData() {
        return data;
    }

    public void setData(List<AlertDetailsData> data) {
        this.data = data;
    }
}
