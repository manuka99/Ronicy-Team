package com.adeasy.advertise.model;

import com.adeasy.advertise.util.CommonConstants;

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

    public Order_Payment() {
        this.status = CommonConstants.PAYMENT_NOT_PAID;
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

}
