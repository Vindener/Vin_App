package com.example.vin.trip;

import java.util.Date;
public class TripData {
    private int userId;
    private int transportId;
    private int placeId;
    private String timeStart;
    private String timeEnd;
    private double duration;
    private double cost;
    private String foto;

    public TripData(int userId,int transportId, int placeId, String timeStart, String timeEnd, double duration, double cost, String foto) {
        this.userId = userId;
        this.transportId = transportId;
        this.placeId = placeId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.duration = duration;
        this.cost = cost;
        this.foto = foto;
    }

    public int getUserId() {
        return userId;
    }
    public int getTransportId() {
        return transportId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public double getDuration() {
        return duration;
    }

    public double getCost() {
        return cost;
    }

    public String getFoto() {
        return foto;
    }
}
