package com.group3.cmpesocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.event.EventDetailActivity;
import com.group3.cmpesocial.classes.Event;

import java.util.List;

/**
 * Created by Tuba on 20/12/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List objects;

    public EventAdapter(Context context, List objects) {
        super(context, R.layout.item_event, objects);
        this.context = context;
        this.objects = objects;
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
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("id", mEvent.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}

