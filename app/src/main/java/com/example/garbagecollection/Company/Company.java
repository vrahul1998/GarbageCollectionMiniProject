package com.example.garbagecollection.Company;

public class Company {
    String name;
    String desc;
    String cid;
    String phone;
    String image;

    public Company() {
    }

    public Company(String name, String desc, String cid, String phone, String image) {
        this.name= name;
        this.desc = desc;
        this.cid = cid;
        this.phone = phone;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
