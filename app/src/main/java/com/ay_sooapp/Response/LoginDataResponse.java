package com.ay_sooapp.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDataResponse {

    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("profile")
    @Expose
    private LoginProfileResponse profile;

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

    public LoginProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(LoginProfileResponse profile) {
        this.profile = profile;
    }

}
