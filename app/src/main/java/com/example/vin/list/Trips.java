package com.example.vin.list;

import java.text.SimpleDateFormat;
import java.util.Date;
public class Trips {
    private int typeId;
    private int transportIndex;
    private double cost;
    private double duration;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDate = dateFormat.format(timeStart);
        this.timeStart = formattedDate;
    }

    public String  getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDate = dateFormat.format(timeEnd);
        this.timeEnd = formattedDate;
    }

    // Дополнительные поля и методы по необходимости
}