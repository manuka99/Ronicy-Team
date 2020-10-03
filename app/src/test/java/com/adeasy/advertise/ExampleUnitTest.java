package com.adeasy.advertise;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.administration.advertisement.StatisticAds;
import com.adeasy.advertise.ui.administration.order.OrderSales;
import com.adeasy.advertise.ui.administration.order.ProductStatisticsFragment;
import com.adeasy.advertise.ui.administration.order.SingleProductAnalysis;

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

    @Before
    public void setUp() {
        statisticAds = new StatisticAds();
        orderSales = new OrderSales();
        productStatisticsFragment = new ProductStatisticsFragment();
    }

    @Test
    private void is_sortDataByAdvertisemntList_correct(){
        List<Advertisement> ads = new ArrayList<>();

        ads.add(new Advertisement());

    }


}