package com.example.garbagecollection;

public class CitizenUser {
String name;
String age;
String imgsrc;
String phoneNo;
String dob;
public CitizenUser(){}

    public CitizenUser(String name, String age, String imgsrc, String phoneNo, String dob) {
        this.name = name;
        this.age = age;
        this.imgsrc = imgsrc;
        this.phoneNo = phoneNo;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getdob() {
        return dob;
    }

    public void setdob(String dob) {
        this.dob = dob;
    }
}
