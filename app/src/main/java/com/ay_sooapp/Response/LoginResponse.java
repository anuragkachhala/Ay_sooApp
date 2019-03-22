package com.ay_sooapp.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("data")
    @Expose
    private LoginDataResponse data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public LoginDataResponse getData() {
        return data;
    }

    public void setData(LoginDataResponse data) {
        this.data = data;
    }

}