package com.example.vin.maps;

import com.google.android.gms.maps.model.Marker;

public class Transport {
    private String title;
    private boolean isFree;
    private double latitude;
    private double longitude;
    private Marker marker;
    private int type;
    private int persec;

    private int battery;

    public Transport(String title, boolean isFree, double latitude, double longitude,int type) {
        this.title = title;
        this.isFree = isFree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;

        if (type == 1) {
            persec = 3;
        } else if (type == 2) {
            persec = 4;
        } else {
            persec = 2;
        }
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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        if (type == 1) {
            persec = 3;
        } else if (type == 2) {
            persec = 4;
        } else {
            persec = 0;
        }
    }

    public int getPersec(int type){
        if (type == 1){
            return 3;
        }
        else if(type == 2){
            return 4;
        }
        return persec;
    }
}

