package com.adeasy.advertise.model;

import android.net.Uri;

import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.adeasy.advertise.util.UniqueIdBasedOnName;
import com.google.firebase.firestore.Exclude;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Advertisement implements Serializable {

    private String id;
    private String title;
    private String condition;
    private String description;
    private double price;
    private Date placedDate;
    private String categoryID;
    private String userID;
    private List<String> imageUrls;
    private List<Integer> numbers;
    private String location;
    private String unapprovedReason;

    private boolean reviewed;
    private boolean availability;
    private boolean approved;
    private boolean buynow;
    private Map<String, Date> promotions;

    private String preetyTime;
    private String preetyCurrency;

    public Advertisement() {
        this.placedDate = new Date();
        this.availability = true;
        this.approved = false;
        this.buynow = false;
        this.reviewed = false;
        this.promotions = new HashMap<>();
        this.imageUrls = new ArrayList<>();
        this.numbers = new ArrayList<>();
    }

    public Advertisement(Date date, Boolean isApproved, Boolean isAvailable, Boolean isBuyNow) {
        this.placedDate = date;
        this.availability = isAvailable;
        this.approved = isApproved;
        this.buynow = isBuyNow;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public String getUnapprovedReason() {
        return unapprovedReason;
    }

    public void setUnapprovedReason(String unapprovedReason) {
        this.unapprovedReason = unapprovedReason;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    @Exclude
    public String getPreetyCurrency() {
        return new DoubleToCurrencyFormat().setStringValue(String.valueOf(price));
    }

    @Exclude
    public void setPreetyCurrency(String preetyCurrency) {
        this.preetyCurrency = preetyCurrency;
    }

    public Map<String, Date> getPromotions() {
        return promotions;
    }

    public void setPromotions(Map<String, Date> promotions) {
        this.promotions = promotions;
    }

}