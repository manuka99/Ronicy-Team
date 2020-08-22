package com.adeasy.advertise.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Order implements Serializable {

    private String id;
    private boolean avalability;
    private String orderStatus;
    private String orderDescription;
    private Date placedDate;
    private String deliveryEstimatedDate;
    private Date deliveredDate;
    private Order_Customer customer;
    private Order_Payment payment;
    private Order_Item item;

    public Order() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);

        this.placedDate = new Date();
        this.avalability = true;
        this.orderStatus = "Processing";
        this.customer = new Order_Customer();
        this.payment = new Order_Payment();
        this.item = new Order_Item();
        this.deliveryEstimatedDate =  dateFormat.format(c.getTime());
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

    public Date getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Date placedDate) {
        this.placedDate = placedDate;
    }

    public String getDeliveryEstimatedDate() {
        return deliveryEstimatedDate;
    }

    public void setDeliveryEstimatedDate(String deliveryEstimatedDate) {
        this.deliveryEstimatedDate = deliveryEstimatedDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
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
