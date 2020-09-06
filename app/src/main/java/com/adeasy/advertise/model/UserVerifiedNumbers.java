package com.adeasy.advertise.model;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas94@gmail.com
 **/
public class UserVerifiedNumbers extends User {

    private List<Integer> verifiedNumbers;

    public UserVerifiedNumbers() {
    }

    public UserVerifiedNumbers(FirebaseUser user, Integer number) {
        super(user);
        this.verifiedNumbers = new ArrayList<>();
        this.verifiedNumbers.add(number);
    }

    public List<Integer> getVerifiedNumbers() {
        return verifiedNumbers;
    }

    public void setVerifiedNumbers(List<Integer> verifiedNumbers) {
        this.verifiedNumbers = verifiedNumbers;
    }

}
