package com.ay_sooapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserProfileDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("EmailConfirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("SecurityStamp")
    @Expose
    private Object securityStamp;
    @SerializedName("PhoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("PhoneNumberConfirmed")
    @Expose
    private Boolean phoneNumberConfirmed;
    @SerializedName("LockoutEndDateUtc")
    @Expose
    private Object lockoutEndDateUtc;
    @SerializedName("LockoutEnabled")
    @Expose
    private Boolean lockoutEnabled;
    @SerializedName("TwoFactorEnabled")
    @Expose
    private Boolean twoFactorEnabled;
    @SerializedName("AccessFailedCount")
    @Expose
    private Integer accessFailedCount;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("activation")
    @Expose
    private String activation;
    @SerializedName("activationExp")
    @Expose
    private String activationExp;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("platform")
    @Expose


    private String platform;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getSecurityStamp() {
        return securityStamp;
    }

    public void setSecurityStamp(Object securityStamp) {
        this.securityStamp = securityStamp;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getPhoneNumberConfirmed() {
        return phoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(Boolean phoneNumberConfirmed) {
        this.phoneNumberConfirmed = phoneNumberConfirmed;
    }

    public Object getLockoutEndDateUtc() {
        return lockoutEndDateUtc;
    }

    public void setLockoutEndDateUtc(Object lockoutEndDateUtc) {
        this.lockoutEndDateUtc = lockoutEndDateUtc;
    }

    public Boolean getLockoutEnabled() {
        return lockoutEnabled;
    }

    public void setLockoutEnabled(Boolean lockoutEnabled) {
        this.lockoutEnabled = lockoutEnabled;
    }

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public Integer getAccessFailedCount() {
        return accessFailedCount;
    }

    public void setAccessFailedCount(Integer accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getActivationExp() {
        return activationExp;
    }

    public void setActivationExp(String activationExp) {
        this.activationExp = activationExp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}
