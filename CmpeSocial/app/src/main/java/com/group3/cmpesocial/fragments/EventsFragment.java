package com.group3.cmpesocial.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group3.cmpesocial.EventDetailActivity;
import com.group3.cmpesocial.NewEventActivity;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Event;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private static final String TAG = EventsFragment.class.getSimpleName();

    private final String title = "Events";
    private Button createEventButton;
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

        createEventButton = (Button) mView.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) mView.findViewById(R.id.listView);

        eventsArray = new ArrayList<>();
        fillEvents();

        adapter = new EventAdapter(getContext(), eventsArray);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);

        return mView;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void fillEvents() {
        eventsArray.clear();
        JsonObject json = new JsonObject();
        Ion.with(getActivity())
                .load("http://54.148.86.208:8080/cmpesocial/api/events/all")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i(TAG, "got result");
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
                        } else if (result != null) {
                            Log.i(TAG, "result not null");
                            Log.i(TAG, "" + result.toString());
                            String type = trimQuotes(result.get("Result").toString());
                            Log.i(TAG, "type: " + type);
                            if (type.equals("SUCCESS")) {
                                JsonArray events = result.getAsJsonArray("events");
                                Log.i(TAG, events.toString());
                                JsonObject eventt = events.get(0).getAsJsonObject();
                                Log.i(TAG, eventt.toString());
                                Iterator<JsonElement> iterator = events.iterator();
                                while(iterator.hasNext()){
                                    JsonObject eventJson = iterator.next().getAsJsonObject();
                                    Event event = new Event(eventJson);
                                    eventsArray.add(event);
                                }

                                adapter = new EventAdapter(getContext(), eventsArray);
                                listView.setAdapter(adapter);
                            } else{
                                Log.i(TAG, "type: " + type.toString());
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });

    }

    protected String trimQuotes(String s){
        if (s.charAt(0) == '\"' && s.charAt(s.length()-1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
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

            dateTextView.setText(mEvent.getDate());
            titleTextView.setText(mEvent.getName());
            placeTextView.setText(mEvent.getLocation());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra("id", mEvent.getId());
                    intent.putExtra("name", mEvent.getName());
                    intent.putExtra("date", mEvent.getDate());
                    intent.putExtra("location", mEvent.getLocation());
                    intent.putExtra("description", mEvent.getDescription());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}
