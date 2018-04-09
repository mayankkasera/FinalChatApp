package com.example.mayank.finalchatapp;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mayank on 10/1/18.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position)
        {
            case 2:
                return fragment = new RequestFragment();
            case 0:
                return fragment = new ChatFragment();
            case 1:
                return fragment = new FrendsFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int Position)
    {
        switch (Position)
        {
            case 2:
                return "Request";
            case 0:
                return "Chat";
            case 1:
                return "Frends";
            default:
                return null;
        }
    }
}
