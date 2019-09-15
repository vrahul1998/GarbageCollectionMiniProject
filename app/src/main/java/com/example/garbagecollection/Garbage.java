package com.example.garbagecollection;

public class Garbage {
    String title,location,status;
    int img;
    public Garbage(){

    }

    public Garbage(String title, String location, String status, int img) {
        this.title = title;
        this.location = location;
        this.status = status;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String ststus) {
        this.status = ststus;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
