package com.group3.cmpesocial.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.EventAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.EventAdapter;
import com.group3.cmpesocial.classes.Event;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String title;
    private int user_id;
    private String name;
    private TextView nameTextView;
    private ListView listView;
    private static ArrayList<Event> eventsArray;
    private EventAdapter adapter;

    public HomeFragment() {
        title =  "CmpeSocial";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefsCMPE", Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        name = prefs.getString("name", "def_name");

        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        nameTextView = (TextView) mView.findViewById(R.id.nameTextView);
        nameTextView.setText("Welcome back, " + name.toUpperCase() + "!");
        listView = (ListView) mView.findViewById(R.id.listView);
        eventsArray = new ArrayList<>();

        adapter = new EventAdapter(getContext(), eventsArray);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);

        JsonObject json = new JsonObject();
        json.addProperty("id", user_id);

        eventsArray.clear();
        adapter.clear();
        eventsArray = EventAPI.getJoinedEvents(json, getContext());
        adapter.addAll(eventsArray);

        return mView;
    }

    public String getTitle(){
        return title;
    }

}
