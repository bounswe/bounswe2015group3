package com.group3.cmpesocial.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.EventAPI;
import com.group3.cmpesocial.API.GroupAPI;
import com.group3.cmpesocial.API.SearchAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.UserAdapter;
import com.group3.cmpesocial.classes.User;

import java.util.ArrayList;


public class InviteUserActivity extends AppCompatActivity {

    private static final String TAG = InviteUserActivity.class.getSimpleName();

    private ListView userListView;
    private boolean searching = false;
    private Toolbar toolbar;
    private Button inviteButton;
    private ImageButton searchButton;
    private EditText searchEditText;
    private ArrayList<User> usersArray;
    private UserAdapter adapter;
    private int id;
    private boolean isEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        isEvent = (boolean) extras.get("isEvent");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userListView = (ListView) findViewById(R.id.userListView);
        inviteButton = (Button) findViewById(R.id.inviteButton);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchEditText.setVisibility(View.GONE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "search clicked");
                if (!searching) {
                    searchEditText.setVisibility(View.VISIBLE);
                    searchButton.setImageResource(R.drawable.ic_clear_white_24dp);
                    searching = true;
                    getSupportActionBar().setTitle("");
                } else {
                    View view = InviteUserActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    searchEditText.setVisibility(View.GONE);
                    searchButton.setImageResource(R.drawable.ic_search_white_24dp);
                    searching = false;
                    getSupportActionBar().setTitle("Invite");
                }
            }
        });
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

        usersArray = new ArrayList<>();

        adapter = new UserAdapter(this, usersArray);
        adapter.setNotifyOnChange(true);
        userListView.setAdapter(adapter);
    }

    public void refreshList(String input) {
        Log.i(TAG, "refresh");
        usersArray.clear();
        adapter.clear();
        JsonObject json = new JsonObject();
        json.addProperty("text", input);
        usersArray = SearchAPI.searchUsers(json, this);
        adapter.addAll(usersArray);
        Log.d(TAG, "usersArray length " + usersArray.size());
    }

    public void invite(View v) {
        Log.i(TAG, "invite");
        ArrayList<User> users = adapter.getSelectedUsers();
        for (int i = 0; i < users.size(); i++) {
            int id_user = (users.get(i)).getId();
            if (isEvent) {
                JsonObject json = new JsonObject();
                json.addProperty("id_user", id_user);
                json.addProperty("id_event", id);
                EventAPI.inviteToEvent(json, this);
            } else {
                JsonObject json = new JsonObject();
                json.addProperty("id_user", id_user);
                json.addProperty("id_group", id);
                GroupAPI.inviteToGroup(json, this);
            }
        }
        Log.i(TAG, "done");
        finish();
    }

}
