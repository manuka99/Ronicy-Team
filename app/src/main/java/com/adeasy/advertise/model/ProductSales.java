package com.adeasy.advertise.model;

import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ProductSales implements Serializable {

    private Order_Item order_item;
    private Integer salesCount;
    private Double totalSales;
    private Map<Double, Integer> priceRangersAnCount;
    private String preetyCurrency;

    public ProductSales(){
        order_item = new Order_Item();
        priceRangersAnCount = new HashMap<>();
    }

    public Order_Item getOrder_item() {
        return order_item;
    }

    public void setOrder_item(Order_Item order_item) {
        this.order_item = order_item;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Map<Double, Integer> getPriceRangersAnCount() {
        return priceRangersAnCount;
    }

    public void setPriceRangersAnCount(Map<Double, Integer> priceRangersAnCount) {
        this.priceRangersAnCount = priceRangersAnCount;
    }

    @Exclude
    public String getPreetyCurrency() {
        return new DoubleToCurrencyFormat().setStringValue(String.valueOf(totalSales));
    }

    @Exclude
    public void setPreetyCurrency(String preetyCurrency) {
        this.preetyCurrency = preetyCurrency;
    }

}
