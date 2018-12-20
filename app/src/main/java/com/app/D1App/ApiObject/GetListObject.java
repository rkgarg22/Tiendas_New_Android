package com.app.D1App.ApiObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kartikeya on 03/11/2018.
 */

public class GetListObject {
    @SerializedName("title")
    private String title;
    @SerializedName("address")
    private String address;
    @SerializedName("city")
    private String city;
    @SerializedName("distance")
    private Double distance;
    @SerializedName("isNew")
    private String isNew;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}