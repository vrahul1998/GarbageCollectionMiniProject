package com.example.garbagecollection.Company;

public class Employee {
    String name;
    String age;
    String image;
    String email;
    String cid;
    String company;
    String status;
    String activity;

    public Employee(){}
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Employee(String name, String age, String image, String email, String cid, String company, String status, String activity) {
        this.name = name;
        this.age = age;
        this.image = image;
        this.email = email;
        this.cid = cid;
        this.company = company;
        this.status = status;
        this.activity = activity;
    }
}
