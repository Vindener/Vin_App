package com.example.vin.maps;

import com.google.android.gms.maps.model.Marker;

public class Transport {
    private String title;
    private boolean isFree;
    private double latitude;
    private double longitude;
    private Marker marker;
    private int type;
    private int battery;

    public Transport(){

    }

    public Transport(String title, boolean isFree, double latitude, double longitude,int type,int battery) {
        this.title = title;
        this.isFree = isFree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.battery = battery;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public boolean isFree() {
        return isFree;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
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
    }

    public void setBattery(int battery){
        this.battery = battery;
    }
    public int getBattery(){
        return battery;
    }

    public void setStan(int stan){
        if(stan == 1){
            isFree = true;
        }
        else{
            isFree= false;
        }
    }
}

