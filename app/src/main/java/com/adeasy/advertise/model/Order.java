package com.adeasy.advertise.model;

import com.adeasy.advertise.util.CommonConstants;
import com.google.firebase.firestore.Exclude;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Order implements Serializable {

    private String id;
    private boolean avalability;
    private String orderStatus;
    private String orderDescription;
    private Date placedDate;
    private String deliveryEstimatedDate;
    private Date deliveredDate;

    private User customer;
    private Order_Payment payment;
    private Order_Item item;

    private String preetyTime;

    public Order() {

    }

    public Order(boolean avalability) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);

        this.placedDate = new Date();
        this.avalability = avalability;
        this.orderStatus = CommonConstants.ORDER_PROCESSING;
        this.customer = new User();
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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
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
