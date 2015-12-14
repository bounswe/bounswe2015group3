package com.group3.cmpesocial.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.event.EventDetailActivity;
import com.group3.cmpesocial.classes.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchEventFragment extends Fragment {

    private static final String TAG = SearchEventFragment.class.getSimpleName();

    public boolean searching = false;

    private ListView listView;
    private EditText searchEditText;
    private ArrayList<Event> eventsArray;
    private EventAdapter adapter;

    public SearchEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_events, container, false);

        listView = (ListView) mView.findViewById(R.id.listView);
        searchEditText = (EditText) getActivity().findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                refreshList(input);
            }
        });

        eventsArray = new ArrayList<>();

        adapter = new EventAdapter(getContext(), eventsArray);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);

        return mView;
    }

    public void refreshList(String input) {
        Log.i(TAG, "refresh");
        eventsArray.clear();
        adapter.clear();
        JsonObject json = new JsonObject();
        json.addProperty("text",input);
        eventsArray = API.searchEvents(json, getContext());
    }

    public class EventAdapter extends ArrayAdapter<Event> {

        public EventAdapter(Context context, List objects) {
            super(context, R.layout.item_event, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);

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
