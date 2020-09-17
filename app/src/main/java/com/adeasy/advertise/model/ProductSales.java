package com.adeasy.advertise.model;

import java.util.HashMap;
import java.util.Map;

public class ProductSales {

    private Order_Item order_item;
    private Integer salesCount;
    private Double totalSales;
    private Map<Double, Integer> priceRangersAnCount;

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

}
