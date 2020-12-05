package com.rejointech.keybook.Data;

public class userCredentials {
    private String email, phone, name, uid;

    public userCredentials() {
    }

    public userCredentials(String email, String phone, String name, String uid) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
