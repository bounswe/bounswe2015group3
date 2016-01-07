package com.group3.cmpesocial.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.group3.cmpesocial.API.EventAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.EventAdapter;
import com.group3.cmpesocial.classes.Event;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private static final String TAG = EventsFragment.class.getSimpleName();

    private final String title = "Events";

    private View mView;
    private ListView listView;
    private static ArrayList<Event> eventsArray;
    private EventAdapter adapter;

    public EventsFragment() {
        // Required empty public constructor
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

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshList() {
        Log.i(TAG, "refresh");
        eventsArray.clear();
        int result = EventAPI.allEvents(getContext(), adapter);
        if(result != EventAPI.SUCCESS){
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}