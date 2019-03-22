package com.ay_sooapp.Model;

public class AlertData {
    private String url;
    private String website;
    private String currentPrice;
    private String lastCheckedIn;

    public AlertData(String url, String website, String currentPrice, String lastCheckedIn) {
        this.url = url;
        this.website = website;
        this.currentPrice = currentPrice;
        this.lastCheckedIn = lastCheckedIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getLastCheckedIn() {
        return lastCheckedIn;
    }

    public void setLastCheckedIn(String lastCheckedIn) {
        this.lastCheckedIn = lastCheckedIn;
    }
}
