package com.adeasy.advertise;

import android.util.Log;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.administration.advertisement.StatisticAds;
import com.adeasy.advertise.ui.administration.order.OrderSales;
import com.adeasy.advertise.ui.administration.order.ProductStatisticsFragment;
import com.adeasy.advertise.ui.administration.order.SingleProductAnalysis;
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

    @Test
    public void is_sortDataByAdvertisemntList_correct(){
        List<Advertisement> ads = new ArrayList<>();

        //in calender function month goes from 0 - 11, jan - 0, feb - 1
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020),true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020),false, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(1, 2020),true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020),false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020),true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(4, 2020),false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020),false, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(7, 2020),true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020),false, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020),true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(0, 2020),false, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020),true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(11, 2020),false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020),true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020),false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020),true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(8, 2020),true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020),false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(4, 2020),false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(1, 2020),true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(1, 2020),false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020),false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020),true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(7, 2020),true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(7, 2020),true, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020),true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020),true, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020),false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(6, 2020),false, true, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020),true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(3, 2020),false, false, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020),true, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(0, 2020),false, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(0, 2020),true, true, false));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(9, 2020),false, false, true));
        ads.add(new Advertisement(CommonFunctions.getDateONMonthAndYear(2, 2020),true, false, false));
        List<int[]> data = statisticAds.sortDataByAdvertisemntList(ads);

        //approved ads vs unapproved ads
        assertArrayEquals("approved ads vs unappoved 1", new int[]{19, 17} ,data.get(0));

        // otherFlagsFromApprovedAds
        //live , not available, buynow
        assertArrayEquals("otherFlagsFromApprovedAds 1", new int[]{9, 10, 9} ,data.get(1));

        // otherFlagsFromUnApprovedAds
        //live , not available, buynow
        assertArrayEquals("otherFlagsFromUnApprovedAds 1", new int[]{8, 9, 9} ,data.get(2));

        //12 months data
        assertArrayEquals("all ads placed in 12 months", new int[]{3, 3, 5, 5, 2, 0, 5, 3, 4, 5, 0, 1} ,data.get(3));

    }


}