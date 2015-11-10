package com.group3.cmpesocial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Tuba on 07/11/15.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private Fragment active;

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null || fragments.size() < 1 || fragments.size() < position)
            return null;

        active = fragments.get(position);
        return active;
    }

    @Override
    public int getCount() {
        if (fragments == null)
            return 0;
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && !titles.isEmpty()) {
            return titles.get(position);
        }
        return super.getPageTitle(position);
    }
}