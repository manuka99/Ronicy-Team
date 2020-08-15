package com.adeasy.advertise.model;

import com.google.firebase.firestore.Exclude;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Advertisement {

    private String id;
    private String title;
    private String condition;
    private String description;
    private String imageUrl;
    private double price;
    private Date placedDate;
    private boolean availability;
    private boolean approved;
    private boolean buynow;
    private String categoryID;
    private String userID;
    private String preetyTime;

    public Advertisement() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Date getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Date placedDate) {
        this.placedDate = placedDate;
    }

    public boolean isBuynow() {
        return buynow;
    }

    public void setBuynow(boolean buynow) {
        this.buynow = buynow;
    }

    @Exclude
    public String getPreetyTime() {
        String pTime = "";
        PrettyTime p = new PrettyTime();
        pTime = p.format(this.placedDate);
        return pTime;
    }

    @Exclude
    public void setPreetyTime(String preetyTime) {
        this.preetyTime = preetyTime;
    }
}