package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutUser {
    @SerializedName("Working_by")
    @Expose
    private String Working_by;
    @SerializedName("LATITUDE")
    @Expose
    private Double LATITUDE;
    @SerializedName("LONGITUDE")
    @Expose
    private Double LONGITUDE;
    @SerializedName("flag")
    @Expose
    private int flag;

    public LogoutUser(String working_by, Double lat, Double lon, int flag) {
        this.Working_by= working_by;
        this.LATITUDE= lat;
        this.LONGITUDE= lon;
        this.flag= flag;
    }

    public String getWorking_by() {
        return Working_by;
    }

    public void setWorking_by(String working_by) {
        Working_by = working_by;
    }

    public Double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(Double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public Double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(Double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
