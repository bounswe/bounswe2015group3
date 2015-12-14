package com.group3.cmpesocial.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.event.EventDetailActivity;
import com.group3.cmpesocial.activities.group.GroupDetailActivity;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.Group;
import com.group3.cmpesocial.classes.User;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView surnameTextView;
    private ListView eventListView;
    private ListView groupListView;

    private int id;
    private User mUser;
    private ArrayList<Event> events;
    private ArrayList<Group> groups;
    private EventAdapter eventAdapter;
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        mUser = API.getUser(json, this);
        events = API.getJoinedEvents(json, this);
        groups = API.viewJoinedGroups(json, this);

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        surnameTextView = (TextView) findViewById(R.id.surnameTextView);
        eventListView = (ListView) findViewById(R.id.eventListView);
        groupListView = (ListView) findViewById(R.id.groupListView);

        nameTextView.setText(mUser.getName());
        surnameTextView.setText(mUser.getSurname());

        eventAdapter = new EventAdapter(this, events);
        eventListView.setAdapter(eventAdapter);

        groupAdapter = new GroupAdapter(this, groups);
        groupListView.setAdapter(groupAdapter);
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

            int[] eventStartDate = mEvent.getStartDate();
            String date = Event.getMonthName(eventStartDate[1]) + " " + eventStartDate[0];

            dateTextView.setText(date);
            titleTextView.setText(mEvent.getName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                    intent.putExtra("id", mEvent.getId());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    public class GroupAdapter extends ArrayAdapter<Group> {

        public GroupAdapter(Context context, List objects) {
            super(context, R.layout.item_group, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group, parent, false);

            final Group mGroup = getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);

            titleTextView.setText(mGroup.getName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("group fragment", "here");
                    Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                    intent.putExtra("id", mGroup.getId());
                    startActivity(intent);
                }
            });

            return convertView;

        }
    }
}