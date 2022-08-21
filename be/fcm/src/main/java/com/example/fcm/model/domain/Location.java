package com.example.fcm.model.domain;

public class Location {
    private double latitude;
    private double longitude;

    public Location() {
    }
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public double getLat() {
        return latitude;
    }
    public void setLat(double latitude) {
        this.latitude = latitude;
    }
    public double getLng() {
        return longitude;
    }
    public void setLng(double longitude) {
        this.longitude = longitude;
    }
}
