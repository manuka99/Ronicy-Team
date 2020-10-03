package com.adeasy.advertise;

import android.util.Log;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.model.ProductSales;
import com.adeasy.advertise.ui.administration.advertisement.StatisticAds;
import com.adeasy.advertise.ui.administration.order.OrderSales;
import com.adeasy.advertise.ui.administration.order.ProductStatisticsFragment;
import com.adeasy.advertise.ui.administration.order.SingleProductAnalysis;
import com.adeasy.advertise.util.CommonConstants;
import com.adeasy.advertise.util.CommonFunctions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {

    private StatisticAds statisticAds;
    private OrderSales orderSales;
    private ProductStatisticsFragment productStatisticsFragment;

    private static final String TAG = "ExampleUnitTest";

    @Before
    public void setUp() {
        statisticAds = new StatisticAds();
        orderSales = new OrderSales();
        productStatisticsFragment = new ProductStatisticsFragment();
    }

    //Test case by manuka yasas IT19133850
    @Test
    public void is_sortDataByAdvertisemntList_correct() {
        List<Advertisement> ads = new ArrayList<>();

        //in calender function month goes from 0 - 11, jan - 0, feb - 1
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020), true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020), false, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(1, 2020), true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020), false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020), true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(4, 2020), false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020), false, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(7, 2020), true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020), false, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020), true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(0, 2020), false, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020), true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(11, 2020), false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020), true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020), false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020), true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020), true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020), false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(4, 2020), false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(1, 2020), true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(1, 2020), false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020), false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020), true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(7, 2020), true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(7, 2020), true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020), true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020), true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020), false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020), false, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020), true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020), false, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020), true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(0, 2020), false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(0, 2020), true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020), false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020), true, false, false));
        List<int[]> data = statisticAds.sortDataByAdvertisemntList(ads);

        //approved ads vs unapproved ads
        assertArrayEquals("approved ads vs unappoved 1", new int[]{19, 17}, data.get(0));

        // otherFlagsFromApprovedAds
        //live , not available, buynow
        assertArrayEquals("otherFlagsFromApprovedAds 1", new int[]{9, 10, 9}, data.get(1));

        // otherFlagsFromUnApprovedAds
        //live , not available, buynow
        assertArrayEquals("otherFlagsFromUnApprovedAds 1", new int[]{8, 9, 9}, data.get(2));

        //12 months data(no of ads posted in each month)
        assertArrayEquals("all ads placed in 12 months", new int[]{3, 3, 5, 5, 2, 0, 5, 3, 4, 5, 0, 1}, data.get(3));

    }

    //Test case by manuka yasas IT19133850
    @Test
    public void is_sortDataByOrderList_correct() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(1, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(2, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(3, 2020), CommonConstants.ORDER_PROCESSING, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(4, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(5, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(5, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(5, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(7, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(7, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(8, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(9, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(10, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_PROCESSING, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(1, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(9, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(0, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(0, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(0, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(3, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(3, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(3, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(91, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(1, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(1, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(2, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(0, 2020), CommonConstants.ORDER_PROCESSING, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_PROCESSING, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(11, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(2, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(2, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(3, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(3, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(4, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_DELIVERED, CommonConstants.PAYMENT_PAID, CommonConstants.PAYMENT_PAYHERE));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(6, 2020), CommonConstants.ORDER_CANCELLED, CommonConstants.PAYMENT_NOT_PAID, CommonConstants.PAYMENT_COD));
        orders.add(new Order(CommonFunctions.getDateONMonthAndYear(9, 2020), CommonConstants.ORDER_SHIPPED, CommonConstants.PAYMENT_REFUNDED, CommonConstants.PAYMENT_PAYHERE));

        List<int[]> data = orderSales.sortDataByOrderList(orders);

        //no of orders delivered and paid vs cancelled
        //no of orders delivered and paid = [0]
        //no of orders cancelled =[1]
        assertArrayEquals("delivered and paid vs cancelled", new int[]{9, 16}, data.get(0));

        //otherFlagsFromCompletedOrders
        //payhere = [0]
        //cod = [1]
        assertArrayEquals("otherFlagsFromCompletedOrders", new int[]{4, 5}, data.get(1));

        //otherFlagsFromCancelledOrders
        //payhere = [0]
        //cod = [1]
        assertArrayEquals("otherFlagsFromCancelledOrders", new int[]{5, 11}, data.get(2));

        //12 months data from completed orders(delivered and paid)
        //january = [0], february = [1], ....... december = [11]
        assertArrayEquals("all completed order placed in 12 months", new int[]{1, 1, 2, 2, 1, 0, 1, 0, 0, 0, 0, 1}, data.get(3));
    }

    //Test case by manuka yasas IT19133850
    @Test
    public void is_calculateProductSales_correct() {
        List<Order> orders = new ArrayList<>();

        orders.add(new Order("1", 123.00));
        orders.add(new Order("1", 123.00));
        orders.add(new Order("1", 136.00));
        orders.add(new Order("1", 153.00));
        orders.add(new Order("1", 183.00));
        orders.add(new Order("1", 123.00));

        orders.add(new Order("2", 400.00));
        orders.add(new Order("2", 500.00));
        orders.add(new Order("2", 400.00));
        orders.add(new Order("2", 410.00));

        List<ProductSales> productSales = productStatisticsFragment.calculateProductSales(orders);
        for (ProductSales productSale : productSales) {
            if (productSale.getOrder_item().getId().equals("1")) {
                assertEquals("total sales count for product Id 1", (Integer) 6, productSale.getSalesCount());
                assertEquals("total sales (LKR) for product Id 1", (Double) 841.00, productSale.getTotalSales());
                assertEquals("total sales count for product Id 1 at product price 123", (Integer) 3, productSale.getPriceRangersAnCount().get(123.00));
                assertEquals("total sales count for product Id 1 at product price 183", (Integer) 1, productSale.getPriceRangersAnCount().get(183.00));
                assertEquals("total sales count for product Id 1 at product price 153", (Integer) 1, productSale.getPriceRangersAnCount().get(153.00));
                assertEquals("total sales count for product Id 1 at product price 153", (Integer) null, productSale.getPriceRangersAnCount().get(200.00));
            } else if (productSale.getOrder_item().getId().equals("2")) {
                assertEquals("total sales count for product Id 2", (Integer) 4, productSale.getSalesCount());
                assertEquals("total sales (LKR) for product Id 2", (Double) 1710.00, productSale.getTotalSales());
                assertEquals("total sales count for product Id 2 at product price 400.00", (Integer) 2, productSale.getPriceRangersAnCount().get(400.00));
                assertEquals("total sales count for product Id 2 at product price 500.00", (Integer) 1, productSale.getPriceRangersAnCount().get(500.00));
                assertEquals("total sales count for product Id 2 at product price 410.00", (Integer) 1, productSale.getPriceRangersAnCount().get(410.00));
                assertEquals("total sales count for product Id 2 at product price 420.00", (Integer) null, productSale.getPriceRangersAnCount().get(420.00));
            }
        }

    }

}