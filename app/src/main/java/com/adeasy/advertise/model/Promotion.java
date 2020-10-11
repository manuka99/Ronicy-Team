package com.adeasy.advertise.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Promotion {

    private String promoID;
    private String advertisementID;
    private Date placeDate;
    private boolean approved;
    private boolean activated;
    private boolean reviewed;
    private Map<String, Integer> promos;

    public static final int DAILY_BUMP_AD = 1;
    public static final int TOP_AD = 2;
    public static final int URGENT_AD = 3;
    public static final int SPOTLIGHT_AD = 4;

    public Promotion() {

    }

    public Promotion(String promoID, String advertisementID, Map<String, Integer> promos) {
        this.promoID = promoID;
        this.approved = false;
        this.reviewed = false;
        this.activated = false;
        this.advertisementID = advertisementID;
        this.promos = promos;
        this.placeDate = new Date();
    }

    public String getPromoID() {
        return promoID;
    }

    public void setPromoID(String promoID) {
        this.promoID = promoID;
    }

    public Date getPlaceDate() {
        return placeDate;
    }

    public void setPlaceDate(Date placeDate) {
        this.placeDate = placeDate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getAdvertisementID() {
        return advertisementID;
    }

    public void setAdvertisementID(String advertisementID) {
        this.advertisementID = advertisementID;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public Map<String, Integer> getPromos() {
        return promos;
    }

    public void setPromos(Map<String, Integer> promos) {
        this.promos = promos;
    }

}
