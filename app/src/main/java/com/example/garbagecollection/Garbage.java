package com.example.garbagecollection;

public class Garbage {
  String state;
  String gid;
  double latitude;
  double longitude;
  String imgName;
  String wasteType;
  String email;
  String acceptedEmail;
  String status;
  String desc;
  String title;

   public Garbage(){}

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcceptedEmail() {
        return acceptedEmail;
    }

    public void setAcceptedEmail(String acceptedEmail) {
        this.acceptedEmail = acceptedEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Garbage(String state, String gid, double latitude, double longitude, String imgName, String wasteType, String email, String acceptedEmail, String status, String desc, String title) {
        this.state = state;
        this.gid = gid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgName = imgName;
        this.wasteType = wasteType;
        this.email = email;
        this.acceptedEmail = acceptedEmail;
        this.status = status;
        this.desc = desc;
        this.title = title;
    }
}
