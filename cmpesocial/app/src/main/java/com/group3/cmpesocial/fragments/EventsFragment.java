package com.group3.cmpesocial.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.group3.cmpesocial.API;
import com.group3.cmpesocial.EventDetailActivity;
import com.group3.cmpesocial.NewEventActivity;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private static final String TAG = EventsFragment.class.getSimpleName();

    private final String title = "Events";
    private FloatingActionButton createEventButton;
    private ListView listView;
    private ArrayList<Event> eventsArray;
    private EventAdapter adapter;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_events, container, false);

        createEventButton = (FloatingActionButton) mView.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });

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
        adapter.clear();
        int result = API.allEvents(getContext(), adapter);
        if(result == -1){
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public class EventAdapter extends ArrayAdapter<Event> {

        public EventAdapter(Context context, List objects) {
            super(context, R.layout.item_event, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            }
            final Event mEvent = getItem(position);

            TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            TextView placeTextView = (TextView) convertView.findViewById(R.id.placeTextView);

            int[] eventStartDate = mEvent.getStartDate();
            String date = Event.getMonthName(eventStartDate[1]) + " " + eventStartDate[0];

            dateTextView.setText(date);
            titleTextView.setText(mEvent.getName());
            placeTextView.setText(mEvent.getLocation());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra("id", mEvent.getId());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}
