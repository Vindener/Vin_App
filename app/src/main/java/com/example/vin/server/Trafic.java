package com.example.vin.server;

public class Trafic {
    //отримати з сервера значення
    private double perces_1 = 4.;
    private double perces_2 = 3.;

    public static double getTrafic(int type){
        if(type == 1){
            return 4.;
        } else if (type == 2) {
            return 3.;
        }
        return 4.;
    }
}
