package com.group3.cmpesocial.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.event.NewEventActivity;
import com.group3.cmpesocial.activities.group.NewGroupActivity;
import com.group3.cmpesocial.fragments.EventsFragment;
import com.group3.cmpesocial.fragments.GroupsFragment;
import com.group3.cmpesocial.fragments.HomeFragment;
import com.group3.cmpesocial.fragments.InvitesFragment;
import com.group3.cmpesocial.fragments.ProfileFragment;
import com.group3.cmpesocial.fragments.RecommendationsFragment;
import com.group3.cmpesocial.fragments.SearchEventFragment;
import com.group3.cmpesocial.imgur.helpers.DocumentHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    private final int HOME = 0;
    private final int RECOMMENDATIONS = 1;
    private final int GROUPS = 2;
    private final int EVENTS = 3;
    private final int PROFILE = 4;
    private final int INVITES = 5;
    private final int SEARCH_EVENT = 6;
    private final int SEARCH_GROUP = 7;

    FragmentTransaction fragmentTransaction;

    private boolean searching = false;

    private Toolbar toolbar;
    private ImageButton searchButton;
    private EditText searchEditText;
    private FloatingActionButton createButton;

    private FragmentManager fragmentManager;
    private HomeFragment fragment_home;
    private RecommendationsFragment fragment_recommendations;
    private GroupsFragment fragment_groups;
    private EventsFragment fragment_events;
    private ProfileFragment fragment_profile;
    private InvitesFragment fragment_invites;
    private SearchEventFragment fragment_search_event;
//    private SearchGroupFragment fragment_search_group;
    private Fragment[] fragments = {fragment_home, fragment_groups, fragment_events, fragment_profile, fragment_invites, fragment_search_event};
    private int currentScreen;
    private String currentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_home = new HomeFragment();
        fragment_recommendations = new RecommendationsFragment();
        fragment_groups = new GroupsFragment();
        fragment_events = new EventsFragment();
        fragment_profile = new ProfileFragment();
        fragment_invites = new InvitesFragment();
        fragment_search_event = new SearchEventFragment();
//        fragment_search_group = new SearchGroupFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment_home);
        fragmentTransaction.commit();
        currentScreen = HOME;
        currentTitle = fragment_home.getTitle();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);

        createButton = (FloatingActionButton) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentScreen == EVENTS){
                    Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                    startActivity(intent);
                } else if (currentScreen == GROUPS){
                    Intent intent = new Intent(MainActivity.this, NewGroupActivity.class);
                    startActivity(intent);
                } else if (currentScreen == SEARCH_EVENT) {
                    Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                    startActivity(intent);
                } else if (currentScreen == SEARCH_GROUP) {
                    Intent intent = new Intent(MainActivity.this, NewGroupActivity.class);
                    startActivity(intent);
                }
            }
        });

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
                    if (currentScreen == EVENTS){
                        searchEvents();
                    } else if (currentScreen == GROUPS){
                        searchGroups();
                    }
                } else {
                    View view = MainActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    searchEditText.setVisibility(View.GONE);
                    searchButton.setImageResource(R.drawable.ic_search_white_24dp);
                    searching = false;
                    getSupportActionBar().setTitle(currentTitle);
                }
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences("prefsCMPE", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "def_name");
        String surname = prefs.getString("surname", "def_surname");
        View header = navigationView.getHeaderView(0);
        TextView headerName = (TextView) header.findViewById(R.id.headerName);
        headerName.setText(name + " " + surname);

        setEventsFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri returnUri;

        if (requestCode != 100) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == 451) {
            returnUri = data.getData();
            String filePath = DocumentHelper.getPath(this, returnUri);
            //Safety check to prevent null pointer exception
            if (filePath == null || filePath.isEmpty()) return;
            File chosenFile = new File(filePath);
            Log.d(TAG, "got file");
            fragment_profile.uploadImage(chosenFile);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                if (currentScreen != HOME) {
                    setEventsFragment();
                }
                break;
            case R.id.nav_recommendations:
                if (currentScreen != HOME) {
                    setRecommendationsFragment();
                }
                break;
            case R.id.nav_groups:
                if (currentScreen != GROUPS) {
                    setGroupsFragment();
                }
                break;
            case R.id.nav_events:
                if (currentScreen != EVENTS) {
                    setEventsFragment();
                }
                break;

            case R.id.nav_profile:
                if (currentScreen != PROFILE) {
                    setProfileFragment();
                }
                break;
            case R.id.nav_invites:
                if (currentScreen != INVITES) {
                    setInvitesFragment();
                }
                break;
            case R.id.nav_logout:
                getSharedPreferences("prefsCMPE", MODE_PRIVATE).edit().clear().commit();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.nav_about_us:
                Toast.makeText(this, "About Us", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_send_feedback:
                Toast.makeText(this, "Send Feedback", Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setHomeFragment() {
        searchButton.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_home);
        fragmentTransaction.commit();
        currentScreen = HOME;
        currentTitle = fragment_home.getTitle();
        getSupportActionBar().setTitle(currentTitle);
    }

    public void setRecommendationsFragment() {
        searchButton.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_recommendations);
        fragmentTransaction.commit();
        currentScreen = RECOMMENDATIONS;
        currentTitle = fragment_recommendations.getTitle();
        getSupportActionBar().setTitle(currentTitle);
    }

    public void setGroupsFragment() {
        searchButton.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.VISIBLE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_groups);
        fragmentTransaction.commit();
        currentScreen = GROUPS;
        currentTitle = fragment_groups.getTitle();
        getSupportActionBar().setTitle(currentTitle);
    }

    public void setEventsFragment() {
        searchButton.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.VISIBLE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_events);
        fragmentTransaction.commit();
        currentScreen = EVENTS;
        currentTitle = fragment_events.getTitle();
        getSupportActionBar().setTitle(currentTitle);
    }

    public void setProfileFragment() {
        searchButton.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_profile);
        fragmentTransaction.commit();
        currentScreen = PROFILE;
        currentTitle = fragment_profile.getTitle();
        getSupportActionBar().setTitle(currentTitle);
    }

    public void setInvitesFragment() {
        searchButton.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);
        createButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_invites);
        fragmentTransaction.commit();
        currentScreen = INVITES;
        currentTitle = fragment_invites.getTitle();
        getSupportActionBar().setTitle(currentTitle);
    }

    public void searchEvents(){
        createButton.setVisibility(View.VISIBLE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_search_event);
        fragmentTransaction.commit();
        currentScreen = SEARCH_EVENT;
    }

    public void searchGroups(){
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment_search_event);
//        fragmentTransaction.commit();
//        currentScreen = SEARCH_EVENT;
    }

}
