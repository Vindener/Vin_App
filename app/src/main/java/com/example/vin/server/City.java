package com.example.vin.server;

public class City {
    private int cityIndex;
    private String cityName;
    private double[] coordinates;

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public double[] getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
    public int getCityIndex() {
        return cityIndex;
    }
    public void setCityIndex(int cityIndex) {
        this.cityIndex = cityIndex;
    }
}
