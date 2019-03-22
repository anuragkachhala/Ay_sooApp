package com.ay_sooapp.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("data")
    @Expose
    private UserProfileDataResponse data;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public UserProfileDataResponse getData() {
        return data;
    }

    public void setData(UserProfileDataResponse data) {
        this.data = data;
    }
}
