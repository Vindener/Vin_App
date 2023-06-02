package com.example.vin.list;

public class Trips {
    private int typeId;
    private int transportIndex;
    private double cost;
    private String duration;
    private String  timeStart;
    private String timeEnd;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTransportIndex() {
        return transportIndex;
    }

    public void setTransportIndex(int transportIndex) {
        this.transportIndex = transportIndex;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String  getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}