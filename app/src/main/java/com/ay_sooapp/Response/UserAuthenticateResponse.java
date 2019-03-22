package com.ay_sooapp.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuthenticateResponse {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("data")
    @Expose
    private Data data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
