package com.example.vin.maps;

import com.google.android.gms.maps.model.Marker;

public class Transport {
    private String title;
    private boolean isFree;
    private double latitude;
    private double longitude;
    private Marker marker;

    private int type;

    public Transport(String title, boolean isFree, double latitude, double longitude,int type) {
        this.title = title;
        this.isFree = isFree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getType() {
        return type;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}

