package com.group3.cmpesocial.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group3.cmpesocial.R;

public class HomeFragment extends Fragment {

    private String title;

    public HomeFragment() {
        title =  "Home";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        return mView;
    }

    public String getTitle(){
        return title;
    }

}
