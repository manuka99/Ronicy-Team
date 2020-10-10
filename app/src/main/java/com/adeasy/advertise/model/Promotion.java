package com.adeasy.advertise.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Promotion {

    private String promoID;
    private String advertisementID;
    private int promotionType;
    private Date placeDate;
    private Date expireDate;
    private int applyDays;
    private boolean approved;

    public static final int DAILY_BUMP_AD = 1;
    public static final int TOP_AD = 2;
    public static final int URGENT_AD = 3;
    public static final int SPOTLIGHT_AD = 4;

    public Promotion() {

    }

    public Promotion(String promoID, String advertisementID, int promotionType, int applyDays) {
        this.promoID = promoID;
        this.promotionType = promotionType;
        this.approved = false;
        this.applyDays = applyDays;
        this.advertisementID = advertisementID;

        Date pDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(pDate);
        c.add(Calendar.DATE, applyDays);

        this.placeDate = pDate;
        this.expireDate = c.getTime();
    }

    public String getPromoID() {
        return promoID;
    }

    public void setPromoID(String promoID) {
        this.promoID = promoID;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public Date getPlaceDate() {
        return placeDate;
    }

    public void setPlaceDate(Date placeDate) {
        this.placeDate = placeDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public int getApplyDays() {
        return applyDays;
    }

    public void setApplyDays(int applyDays) {
        this.applyDays = applyDays;
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

}
