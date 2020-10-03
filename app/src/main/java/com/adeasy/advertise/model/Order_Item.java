package com.adeasy.advertise.model;

import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Order_Item implements Serializable {

    private String id;
    private String itemName;
    private String imageUrl;
    private String categoryName;
    private double price;
    private String preetyCurrency;

    public Order_Item() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getPreetyCurrency() {
        return new DoubleToCurrencyFormat().setStringValue(String.valueOf(price));
    }

    @Exclude
    public void setPreetyCurrency(String preetyCurrency) {
        this.preetyCurrency = preetyCurrency;
    }

}
