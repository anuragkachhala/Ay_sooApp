package com.ay_sooapp.Response;

import com.ay_sooapp.Model.UserProfileDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginProfileResponse {

    @SerializedName("userData")
    @Expose
    private UserProfileDetails userData;

    public UserProfileDetails getUserData() {
        return userData;
    }

    public void setUserData(UserProfileDetails userData) {
        this.userData = userData;
    }
}