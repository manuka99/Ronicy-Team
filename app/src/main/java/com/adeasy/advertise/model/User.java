package com.adeasy.advertise.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String uid;
    private List<Integer> verifiedNumbers;
    private String address;

    public User(){
        this.verifiedNumbers = new ArrayList<>();
    }

    public User(String uid, Integer number){
        this.uid = uid;
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

}
