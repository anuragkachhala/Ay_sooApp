package com.ay_sooapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("user")
    @Expose
    private UserProfileDetails userProfileDetails;

    public UserProfileDetails getUserProfileDetails() {
        return userProfileDetails;
    }

    public void setUserProfileDetails(UserProfileDetails user) {
        this.userProfileDetails = userProfileDetails;
    }

}
