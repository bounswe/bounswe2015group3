package com.group3.cmpesocial.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.EventAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.EventAdapter;
import com.group3.cmpesocial.classes.Event;

import java.util.ArrayList;

/**
 * Created by Tuba on 20/12/15.
 */
public class MyEventsFragment extends Fragment{
    private static final String TAG = MyEventsFragment.class.getSimpleName();

    private final String title = "My Events";

    private View mView;
    private ListView listView;
    private static ArrayList<Event> eventsArray;
    private EventAdapter adapter;
    private JsonObject json;
    private int user_id;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    public void setUserID(int id){
        this.user_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_events, container, false);

        listView = (ListView) mView.findViewById(R.id.listView);

        eventsArray = new ArrayList<>();

        adapter = new EventAdapter(getContext(), eventsArray);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);

        json = new JsonObject();
        json.addProperty("id", user_id);

        refreshList();

        return mView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public String getTitle() {
        return title;
    }

    public void refreshList() {
        Log.i(TAG, "refresh");
        eventsArray.clear();
        adapter.clear();
        eventsArray = EventAPI.getMyEvents(json, getContext());
        adapter.addAll(eventsArray);
    }

    public View getmView() {
        return mView;
    }

}
