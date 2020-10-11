package com.adeasy.advertise.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ApprovedPromotions {

    public static final String promotionID_name = "promotionID";
    public static final String advertismentID_name = "advertismentID";
    public static final String urgentPromoExpireTime_name = "urgentPromoExpireTime";
    public static final String dailyPromoPromoExpireTime_name = "dailyPromoPromoExpireTime";
    public static final String spotLightPromoExpireTime_name = "spotLightPromoExpireTime";
    public static final String topAdPromoExpireTime_name = "topAdPromoExpireTime";
    public static final String lastUpdated_name = "lastUpdated";
    public static final String stopPromotions_name = "stopPromotions";

    private String advertismentID;
    private Date urgentPromoExpireTime;
    private Date dailyPromoPromoExpireTime;
    private Date spotLightPromoExpireTime;
    private Date topAdPromoExpireTime;
    private Boolean stopPromotions;
    List<String> notes;
    List<String> promoIDs;

    public ApprovedPromotions() {

    }

    public ApprovedPromotions(String advertismentID, Boolean stopPromotions) {
        super();
        this.advertismentID = advertismentID;
        this.stopPromotions = stopPromotions;
        this.notes = new ArrayList();
        this.promoIDs = new ArrayList();
    }

    public String getAdvertismentID() {
        return advertismentID;
    }

    public void setAdvertismentID(String advertismentID) {
        this.advertismentID = advertismentID;
    }

    public Date getUrgentPromoExpireTime() {
        return urgentPromoExpireTime;
    }

    public void setUrgentPromoExpireTime(Date urgentPromoExpireTime) {
        this.urgentPromoExpireTime = urgentPromoExpireTime;
    }

    public Date getDailyPromoPromoExpireTime() {
        return dailyPromoPromoExpireTime;
    }

    public void setDailyPromoPromoExpireTime(Date dailyPromoPromoExpireTime) {
        this.dailyPromoPromoExpireTime = dailyPromoPromoExpireTime;
    }

    public Date getSpotLightPromoExpireTime() {
        return spotLightPromoExpireTime;
    }

    public void setSpotLightPromoExpireTime(Date spotLightPromoExpireTime) {
        this.spotLightPromoExpireTime = spotLightPromoExpireTime;
    }

    public Date getTopAdPromoExpireTime() {
        return topAdPromoExpireTime;
    }

    public void setTopAdPromoExpireTime(Date topAdPromoExpireTime) {
        this.topAdPromoExpireTime = topAdPromoExpireTime;
    }

    public Boolean getStopPromotions() {
        return stopPromotions;
    }

    public void setStopPromotions(Boolean stopPromotions) {
        this.stopPromotions = stopPromotions;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public List<String> getPromoIDs() {
        return promoIDs;
    }

    public void setPromoIDs(List<String> promoIDs) {
        this.promoIDs = promoIDs;
    }

}
