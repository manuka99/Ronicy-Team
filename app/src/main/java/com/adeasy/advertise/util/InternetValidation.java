package com.adeasy.advertise.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/

public class InternetValidation {

    public boolean validateInternet(Context context) {
        try {
            //check connected to a network
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //connected to network

                return true;

                //check if it has internet
//                InetAddress inetAddress = null;
//                Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
//                    @Override
//                    public InetAddress call() {
//                        try {
//                            return InetAddress.getByName("google.com");
//                        } catch (UnknownHostException e) {
//                            return null;
//                        }
//                    }
//                });
//                inetAddress = future.get(12, TimeUnit.SECONDS);
//                future.cancel(true);
//
//                return inetAddress != null && !inetAddress.equals("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
