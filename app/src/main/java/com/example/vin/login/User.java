package com.example.vin.login;

public class User {
    private int userIndex;
    private String phone;
    private String name;

    public int getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(int userIndex) {
        this.userIndex = userIndex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
