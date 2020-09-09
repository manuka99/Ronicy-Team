package com.adeasy.advertise.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.adeasy.advertise.ui.administration.advertisement.ApprovedRejected;
import com.adeasy.advertise.ui.administration.advertisement.NewAds;
import com.adeasy.advertise.ui.administration.advertisement.StatisticAds;

public class AdminAdPageAdapter extends FragmentPagerAdapter {

    private int no_of_tabs;

    public AdminAdPageAdapter(@NonNull FragmentManager fm, int no_of_tabs) {
        super(fm);
        this.no_of_tabs = no_of_tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new NewAds();

            case 1:
                return new ApprovedRejected();

            case 2:
                return new StatisticAds();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return no_of_tabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "New ads";

            case 1:
                return "Approved/Rejected";

            case 2:
                return "Statistics";

            default:
                return null;
        }
    }

}
