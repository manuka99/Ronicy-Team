package com.adeasy.advertise.model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private String id;
    private boolean avalability;
    private String orderStatus;
    private String orderDescription;
    private Date time;
    private Order_Customer customer;
    private Order_Payment payment;
    private Order_Item item;

    public Order() {
        this.time = new Date();
        this.avalability = true;
        this.orderStatus = "Processing";
        this.customer = new Order_Customer();
        this.payment = new Order_Payment();
        this.item = new Order_Item();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAvalability() {
        return avalability;
    }

    public void setAvalability(boolean avalability) {
        this.avalability = avalability;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Order_Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Order_Customer customer) {
        this.customer = customer;
    }

    public Order_Payment getPayment() {
        return payment;
    }

    public void setPayment(Order_Payment payment) {
        this.payment = payment;
    }

    public Order_Item getItem() {
        return item;
    }

    public void setItem(Order_Item item) {
        this.item = item;
    }

}
