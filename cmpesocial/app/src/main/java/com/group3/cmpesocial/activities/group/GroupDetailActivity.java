package com.group3.cmpesocial.activities.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.event.NewEventActivity;
import com.group3.cmpesocial.adapters.EventAdapter;
import com.group3.cmpesocial.adapters.UserAdapter;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.Group;
import com.group3.cmpesocial.classes.User;

import java.util.ArrayList;

public class GroupDetailActivity extends AppCompatActivity {

    private static final String TAG = GroupDetailActivity.class.getSimpleName();

    private Group mGroup;
    private User mUser;

    private EditText nameEditText;
    private EditText labelsEditText;
    private EditText descriptionEditText;
    private RecyclerView membersRecyclerView;
    private RecyclerView eventsRecyclerView;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private Button doneButton;
    private Button joinButton;
    private Button leaveButton;
    private FloatingActionButton createEventButton;
    private Toolbar toolbar;

    private UserAdapter userAdapter;
    private EventAdapter eventAdapter;

    private int id;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        labelsEditText = (EditText) findViewById(R.id.labelsEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        membersRecyclerView = (RecyclerView) findViewById(R.id.membersRecyclerView);
        eventsRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        editButton = (ImageButton) findViewById(R.id.editButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        leaveButton = (Button) findViewById(R.id.leaveButton);
        createEventButton = (FloatingActionButton) findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewEventActivity.class);
                startActivity(intent);
            }
        });

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user_id);
        mUser = API.getUser(userJson, this);

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        mGroup = API.getGroup(json, getApplicationContext());

        String name = mGroup.getName();
        String description = mGroup.getDescription();
        int id_user = mGroup.getId_user();

        nameEditText.setText(name);
        descriptionEditText.setText(description);

        if(user_id == id_user){
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        JsonObject membersJson = new JsonObject();
        json.addProperty("id", id);
        ArrayList<User> members = API.getGroupMembers(membersJson, this);

        membersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        membersRecyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(members,this);
        membersRecyclerView.setAdapter(userAdapter);

        JsonObject eventsJson = new JsonObject();
        json.addProperty("id", id);
        ArrayList<Event> events = API.getGroupEvents(eventsJson, this);

        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(events, this);
        eventsRecyclerView.setAdapter(eventAdapter);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGroup(v);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroup(v);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroup(v);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup();
            }
        });
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup();
            }
        });

        enableEditTexts(false);
    }

    public void enableEditTexts(boolean enabled){
        nameEditText.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
    }

    public void deleteGroup(View v){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        int result = API.deleteGroup(json, getApplicationContext());
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event deleted");
            finish();
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void editGroup(View v){
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.VISIBLE);

        enableEditTexts(true);
    }

    public void saveGroup(View v){
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.GONE);

        enableEditTexts(false);

        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("id_admin", user_id);
        json.addProperty("description", description);
        //json.addProperty("type",1);

        Log.i(TAG, json.toString());

        int result = API.updateGroup(json, this);
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event updated");
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void joinGroup(){
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_event", id);

        int result = API.joinEvent(json, this);
        if (result == API.SUCCESS) {
            Toast.makeText(this, "joined group", Toast.LENGTH_SHORT).show();
            userAdapter.add(mUser);
            joinButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
        }else if (result == API.NO_ACCESS){
            Toast.makeText(this, "You cannot join this event unless you are invited.", Toast.LENGTH_SHORT).show();
        }
    }

    public void leaveGroup(){

    }

}
