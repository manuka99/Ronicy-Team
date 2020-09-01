package com.adeasy.advertise.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String uid;
    private String name;
    private List<Integer> verifiedNumbers;
    private String address;
    private String email;

    public User(){
        this.verifiedNumbers = new ArrayList<>();
    }

    public User(FirebaseUser user, Integer number){
        this.uid = user.getUid();
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.verifiedNumbers = new ArrayList<>();
        this.verifiedNumbers.add(number);
    }

    public User(FirebaseUser user, Integer number, String address){
        this.uid = user.getUid();
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.address = address;
        this.verifiedNumbers = new ArrayList<>();
        this.verifiedNumbers.add(number);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Integer> getVerifiedNumbers() {
        return verifiedNumbers;
    }

    public void setVerifiedNumbers(List<Integer> verifiedNumbers) {
        this.verifiedNumbers = verifiedNumbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
