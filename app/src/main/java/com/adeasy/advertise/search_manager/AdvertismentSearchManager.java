package com.adeasy.advertise.search_manager;

import android.util.Log;

import com.adeasy.advertise.callback.AdvertismentSearchCallback;
import com.adeasy.advertise.config.Configurations;
import com.adeasy.advertise.model.Advertisement;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class AdvertismentSearchManager {

    private Client client;
    private Index index;
    private AdvertismentSearchCallback advertismentSearchCallback;

    private static final String INDEX_NAME = "Advertisement";
    private static final String TAG = "AdvertismentSearchManag";

    public AdvertismentSearchManager(AdvertismentSearchCallback advertismentSearchCallback) {
        client = new Client(Configurations.ALGOLIA_APP_ID, Configurations.ALGOLIA_SEARCH_KEY);
        index = client.getIndex(INDEX_NAME);
        this.advertismentSearchCallback = advertismentSearchCallback;
    }

    public void searchAdsHome(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            Query query = new Query(keyword)
                    .setAttributesToRetrieve("id", "title", "price", "placedDate", "categoryID")
                    .setHitsPerPage(50);
            index.searchAsync(query, new CompletionHandler() {
                @Override
                public void requestCompleted(JSONObject content, AlgoliaException error) {
                    if (advertismentSearchCallback != null) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<String> adIDs = new ArrayList<>();

                            List<Advertisement> advertisements = new ArrayList<>();

                            for (int i = 0; i < hits.length(); ++i) {
                                JSONObject hit_object = hits.getJSONObject(i);
                                adIDs.add(hit_object.getString("id"));

                                Advertisement advertisement = new Advertisement();
                                advertisement.setId(hit_object.getString("id"));
                                advertisement.setTitle(hit_object.getString("title"));
                                advertisement.setPrice(hit_object.getDouble("price"));

                                String placedDate = hit_object.getString("placedDate");
                                advertisement.setCategoryID(hit_object.getString("categoryID"));
                                //advertisement.setPreetyTime(placedDate);
                                //advertisement.setPlacedDate();
                                advertisements.add(advertisement);
                            }
                            advertismentSearchCallback.onSearchComplete(adIDs, advertisements);
                            //Log.i(TAG, " hits: " + content.getString("hits"));
                            Log.i(TAG, " adIDs: " + adIDs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void destroy() {
        advertismentSearchCallback = null;
    }

}
