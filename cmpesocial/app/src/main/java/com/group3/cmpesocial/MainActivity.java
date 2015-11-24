package com.group3.cmpesocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.group3.cmpesocial.fragments.EventsFragment;
import com.group3.cmpesocial.fragments.GroupsFragment;
import com.group3.cmpesocial.fragments.HomeFragment;
import com.group3.cmpesocial.fragments.MessagesFragment;
import com.group3.cmpesocial.fragments.ProfileFragment;
import com.group3.cmpesocial.fragments.RecommendationsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int HOME = 0;
    private final int RECOMMENDATIONS = 1;
    private final int GROUPS = 2;
    private final int EVENTS = 3;
    private final int PROFILE = 4;
    private final int MESSAGES = 5;
    FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;
    private Button doneButton;
    private Button editButton;
    private ImageButton searchButton;
    private ImageButton deleteButton;
    private RelativeLayout fragment_container;
    private FragmentManager fragmentManager;
    private HomeFragment fragment_home;
    private RecommendationsFragment fragment_recommendations;
    private GroupsFragment fragment_groups;
    private EventsFragment fragment_events;
    private ProfileFragment fragment_profile;
    private MessagesFragment fragment_messages;
    private Fragment[] fragments = {fragment_home, fragment_groups, fragment_events, fragment_profile, fragment_messages};
    private int currentScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_home = new HomeFragment();
        fragment_recommendations = new RecommendationsFragment();
        fragment_groups = new GroupsFragment();
        fragment_events = new EventsFragment();
        fragment_profile = new ProfileFragment();
        fragment_messages = new MessagesFragment();

        fragmentManager = getSupportFragmentManager();
        fragment_container = (RelativeLayout) findViewById(R.id.fragment_container);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment_home);
        fragmentTransaction.commit();
        currentScreen = HOME;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doneButton = (Button) findViewById(R.id.doneButton);
        editButton = (Button) findViewById(R.id.editButton);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);

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
            case R.id.nav_messages:
                if (currentScreen != MESSAGES) {
                    setMessagesFragment();
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
        doneButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_home);
        fragmentTransaction.commit();
        currentScreen = HOME;
        getSupportActionBar().setTitle(fragment_home.getTitle());
    }

    public void setRecommendationsFragment() {
        doneButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_recommendations);
        fragmentTransaction.commit();
        currentScreen = RECOMMENDATIONS;
        getSupportActionBar().setTitle(fragment_recommendations.getTitle());
    }

    public void setGroupsFragment() {
        doneButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_groups);
        fragmentTransaction.commit();
        currentScreen = GROUPS;
        getSupportActionBar().setTitle(fragment_groups.getTitle());
    }

    public void setEventsFragment() {
        doneButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_events);
        fragmentTransaction.commit();
        currentScreen = EVENTS;
        getSupportActionBar().setTitle(fragment_events.getTitle());
    }

    public void setProfileFragment() {
        doneButton.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_profile);
        fragmentTransaction.commit();
        currentScreen = PROFILE;
        getSupportActionBar().setTitle(fragment_profile.getTitle());
    }

    public void setMessagesFragment() {
        doneButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment_messages);
        fragmentTransaction.commit();
        currentScreen = MESSAGES;
        getSupportActionBar().setTitle(fragment_messages.getTitle());
    }

}
