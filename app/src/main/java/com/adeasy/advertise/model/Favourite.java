package com.adeasy.advertise.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Favourite {
    private String favouriteID;
    private String advertisementID;
    private String userID;

    //misselaneous
    private String description;
    private Date placedDate;
    private List<Date> contactedLog;
    private boolean contacted;

    public Favourite() {

    }

    //init
    public Favourite(String favID, String advertisementID, String userId) {
        this.favouriteID = favID;
        this.advertisementID = advertisementID;
        this.userID = userId;
        this.placedDate = new Date();
        this.contactedLog = new ArrayList<>();
        this.contacted = false;
    }

    public String getFavouriteID() {
        return favouriteID;
    }

    public void setFavouriteID(String favouriteID) {
        this.favouriteID = favouriteID;
    }

    public String getAdvertisementID() {
        return advertisementID;
    }

    public void setAdvertisementID(String advertisementID) {
        this.advertisementID = advertisementID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Date placedDate) {
        this.placedDate = placedDate;
    }

    public List<Date> getContactedLog() {
        return contactedLog;
    }

    public void setContactedLog(List<Date> contactedLog) {
        this.contactedLog = contactedLog;
    }

    public boolean isContacted() {
        return contacted;
    }

    public void setContacted(boolean contacted) {
        this.contacted = contacted;
    }

}
