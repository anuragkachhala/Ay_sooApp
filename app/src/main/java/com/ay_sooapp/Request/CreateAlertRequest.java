package com.ay_sooapp.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateAlertRequest {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("articalNumber")
    @Expose
    private String articalNumber;
    @SerializedName("webSite")
    @Expose
    private String webSite;


    @SerializedName("webSiteID")
    @Expose
    private long webSiteID;

    public long getWebSiteID() {
        return webSiteID;
    }

    public void setWebSiteID(long webSiteID) {
        this.webSiteID = webSiteID;
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

    /*public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }*/

}

