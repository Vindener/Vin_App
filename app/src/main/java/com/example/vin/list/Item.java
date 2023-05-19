package com.example.vin.list;

import com.example.vin.R;

public class Item {
    private int image;
    private String number;
    private String name;
    private String date;
    private String time;
    private String dirucation;

    public Item(int type, String number, String name, String date, String time, String dirucation) {
        if(type==1){
            this.image = R.drawable.ic_menu_gallery;
        }
        else{
            this.image = R.drawable.ic_menu_camera;
        }
        this.number = number;
        this.name = name;
        this.date = date;
        this.time = time;
        this.dirucation = dirucation;
    }

    public int getImage() {
        return image;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    public String getdirucation() {
        return dirucation;
    }
}
