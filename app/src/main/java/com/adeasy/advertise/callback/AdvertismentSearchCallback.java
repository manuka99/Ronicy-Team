package com.adeasy.advertise.callback;

import com.adeasy.advertise.model.Advertisement;

import java.util.List;

public interface AdvertismentSearchCallback {
    public void onSearchComplete(List<String> ids, List<Advertisement> advertisementList);
}
