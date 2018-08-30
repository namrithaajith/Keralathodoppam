package com.mobioetech.keralathodoppam.keralathodoppam;

public class ServiceRequest {
    private String name;
    private double currentlatitude;
    private double currentlongitude;
    private String phonenumber;
    private String servicetype;
    private double pinnedlatitude;
    private double pinnedlongitude;

    public String getPinnedaddress() {
        return pinnedaddress;
    }

    public void setPinnedaddress(String pinnedaddress) {
        this.pinnedaddress = pinnedaddress;
    }

    private String pinnedaddress;

    public String getCurrentaddress() {
        return currentaddress;
    }

    public void setCurrentaddress(String currentaddress) {
        this.currentaddress = currentaddress;
    }

    private String currentaddress;
    private String timestamp;

    public ServiceRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentlatitude() {
        return currentlatitude;
    }

    public void setCurrentlatitude(double currentlatitude) {
        this.currentlatitude = currentlatitude;
    }

    public double getCurrentlongitude() {
        return currentlongitude;
    }

    public void setCurrentlongitude(double currentlongitude) {
        this.currentlongitude = currentlongitude;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public double getPinnedlatitude() {
        return pinnedlatitude;
    }

    public void setPinnedlatitude(double pinnedlatitude) {
        this.pinnedlatitude = pinnedlatitude;
    }

    public double getPinnedlongitude() {
        return pinnedlongitude;
    }

    public void setPinnedlongitude(double pinnedlongitude) {
        this.pinnedlongitude = pinnedlongitude;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
