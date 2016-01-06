package com.group3.cmpesocial.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvitesFragment extends Fragment {

    private final String title = "Invites";

    private InvitedEventsFragment eventsFragment;
    private InvitedGroupsFragment groupsFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    public InvitesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_recommendations, container, false);

        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) mView.findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        eventsFragment = new InvitedEventsFragment();
        groupsFragment = new InvitedGroupsFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        fragments.add(eventsFragment);
        titles.add(eventsFragment.getTitle());
        fragments.add(groupsFragment);
        titles.add(groupsFragment.getTitle());

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return mView;
    }

    public String getTitle(){
        return title;
    }

}
