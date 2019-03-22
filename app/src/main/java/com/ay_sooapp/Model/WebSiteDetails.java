package com.ay_sooapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebSiteDetails {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("URL")
    @Expose
    private String uRL;
    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("SearchURL")
    @Expose
    private String searchURL;


    public WebSiteDetails(long id, String uRL, String name, String searchURL) {
        this.id = id;
        this.uRL = uRL;
        this.name = name;
        this.searchURL = searchURL;
    }

    public WebSiteDetails() {

    }


    public String getSearchURL() {
        return searchURL;
    }

    public void setSearchURL(String searchURL) {
        this.searchURL = searchURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



