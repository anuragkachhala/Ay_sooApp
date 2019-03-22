package com.ay_sooapp.Response;

import com.ay_sooapp.Model.AlertDetailData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlertDetailResponse {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("data")
    @Expose
    private List<AlertDetailData> data = null;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<AlertDetailData> getData() {
        return data;
    }

    public void setData(List<AlertDetailData> data) {
        this.data = data;
    }

}
