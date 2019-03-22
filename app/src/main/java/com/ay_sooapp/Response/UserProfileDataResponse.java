package com.ay_sooapp.Response;

import com.ay_sooapp.Model.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileDataResponse {
    @SerializedName("profile")
    @Expose
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
