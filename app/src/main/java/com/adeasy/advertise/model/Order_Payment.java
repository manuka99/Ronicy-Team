package com.adeasy.advertise.model;

import java.io.Serializable;

public class Order_Payment implements Serializable {

    private String type;
    private String status;
    private double amount;

    public Order_Payment() {
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
