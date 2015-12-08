package com.group3.cmpesocial.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group3.cmpesocial.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    private final String title = "Messages";

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    public String getTitle(){
        return title;
    }

}
