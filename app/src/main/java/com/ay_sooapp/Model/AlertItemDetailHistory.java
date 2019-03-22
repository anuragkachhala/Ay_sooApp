package com.ay_sooapp.Model;

public class AlertItemDetailHistory {
    private String price ;
    private String website;
    private String checkedOn;

    public AlertItemDetailHistory(String price, String website, String checkedOn) {
        this.price = price;
        this.website = website;
        this.checkedOn = checkedOn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCheckedOn() {
        return checkedOn;
    }

    public void setCheckedOn(String checkedOn) {
        this.checkedOn = checkedOn;
    }
}
