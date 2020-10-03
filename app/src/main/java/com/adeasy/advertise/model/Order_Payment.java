package com.adeasy.advertise.model;

import com.adeasy.advertise.util.CommonConstants;
import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Order_Payment implements Serializable {

    private String type;
    private String status;
    private double amount;
    private String preetyCurrency;

    public Order_Payment() {
        this.status = CommonConstants.PAYMENT_NOT_PAID;
    }

    public Order_Payment(String paymentStatus, String paymentType) {
        this.status = paymentStatus;
        this.type = paymentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Exclude
    public String getPreetyCurrency() {
        return new DoubleToCurrencyFormat().setStringValue(String.valueOf(amount));
    }

    @Exclude
    public void setPreetyCurrency(String preetyCurrency) {
        this.preetyCurrency = preetyCurrency;
    }

}
