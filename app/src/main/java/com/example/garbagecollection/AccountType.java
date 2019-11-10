package com.example.garbagecollection;

public class AccountType {
    String email;
    String type;
    String uid;
    public AccountType() {
    }

    public String getEmail() {
        return email;
    }
    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid=uid;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AccountType(String email,String uid, String type) {
        this.email = email;
        this.uid=uid;
        this.type = type;
    }
}
