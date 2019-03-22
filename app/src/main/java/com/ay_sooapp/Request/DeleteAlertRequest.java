package com.ay_sooapp.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteAlertRequest {

    @SerializedName("alert_id")
    @Expose
    private Integer alertId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("articalNumber")
    @Expose
    private String articalNumber;
    @SerializedName("webSite")
    @Expose
    private String webSite;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticalNumber() {
        return articalNumber;
    }

    public void setArticalNumber(String articalNumber) {
        this.articalNumber = articalNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
