package com.ay_sooapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertDetailsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("AddedByUserID")
    @Expose
    private String addedByUserID;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("WebsiteID")
    @Expose
    private Integer websiteID;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("lastCheckedDate")
    @Expose
    private String lastCheckedDate;
    @SerializedName("updatedOn")
    @Expose
    private String updatedOn;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("CreatedByIP")
    @Expose
    private String createdByIP;
    @SerializedName("LastUpdatedByIP")
    @Expose
    private String lastUpdatedByIP;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddedByUserID() {
        return addedByUserID;
    }

    public void setAddedByUserID(String addedByUserID) {
        this.addedByUserID = addedByUserID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWebsiteID() {
        return websiteID;
    }

    public void setWebsiteID(Integer websiteID) {
        this.websiteID = websiteID;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastCheckedDate() {
        return lastCheckedDate;
    }

    public void setLastCheckedDate(String lastCheckedDate) {
        this.lastCheckedDate = lastCheckedDate;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreatedByIP() {
        return createdByIP;
    }

    public void setCreatedByIP(String createdByIP) {
        this.createdByIP = createdByIP;
    }

    public String getLastUpdatedByIP() {
        return lastUpdatedByIP;
    }

    public void setLastUpdatedByIP(String lastUpdatedByIP) {
        this.lastUpdatedByIP = lastUpdatedByIP;
    }


}

