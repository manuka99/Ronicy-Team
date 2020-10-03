package com.adeasy.advertise.callback;

import com.adeasy.advertise.model.Advertisement;

import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public interface AdvertismentSearchCallback {
    public void onSearchComplete(List<String> ids, List<Advertisement> advertisementList);
}
