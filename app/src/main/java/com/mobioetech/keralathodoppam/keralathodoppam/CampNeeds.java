package com.mobioetech.keralathodoppam.keralathodoppam;

public class CampNeeds {
    private String campname;
    private double latitudeofpersonregistered;
    private double longitudeofpersonregister;
    private String locality;
    private String district;
    private String needs;

    public String getNeeds_url() {
        return needs_url;
    }

    public void setNeeds_url(String needs_url) {
        this.needs_url = needs_url;
    }

    private String needs_url;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    private String phonenumber;

    public CampNeeds() {
    }

    public String getCampname() {
        return campname;
    }

    public void setCampname(String campname) {
        this.campname = campname;
    }

    public double getLatitudeofpersonregistered() {
        return latitudeofpersonregistered;
    }

    public void setLatitudeofpersonregistered(double latitudeofpersonregistered) {
        this.latitudeofpersonregistered = latitudeofpersonregistered;
    }

    public double getLongitudeofpersonregister() {
        return longitudeofpersonregister;
    }

    public void setLongitudeofpersonregister(double longitudeofpersonregister) {
        this.longitudeofpersonregister = longitudeofpersonregister;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }
}
