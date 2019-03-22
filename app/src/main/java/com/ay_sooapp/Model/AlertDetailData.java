package com.ay_sooapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AlertDetailData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("WebsiteID")
    @Expose
    private Integer websiteID;
    @SerializedName("alertId")
    @Expose
    private Integer alertId;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWebsiteID() {
        return websiteID;
    }

    public void setWebsiteID(Integer websiteID) {
        this.websiteID = websiteID;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}

/*
public class AlertDetailData {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("alertId")
    @Expose
    private Integer alertId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("updatedOn")
    @Expose
    private String updatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
*/
