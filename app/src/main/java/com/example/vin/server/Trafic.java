package com.example.vin.server;

public class Trafic {

    private String  typeName;
    private double perSec;

    public static double getTrafic(int type){
        if(type == 1){
            return 4.;
        } else if (type == 2) {
            return 3.;
        }
        return 4.;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setPriceOf1(double priceOf1) {
        this.perSec = priceOf1;
    }

    public String getTypeName() {
        return typeName;
    }

    public double getPriceOf1() {
        return perSec;
    }
}
