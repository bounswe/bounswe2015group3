package com.group3.cmpesocial;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.group3.cmpesocial.fragments.EventsFragment;
import com.group3.cmpesocial.fragments.GroupsFragment;
import com.group3.cmpesocial.fragments.HomeFragment;
import com.group3.cmpesocial.fragments.MessagesFragment;
import com.group3.cmpesocial.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private RelativeLayout fragment_container;
    private FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private HomeFragment fragment_home;
    private GroupsFragment fragment_groups;
    private EventsFragment fragment_events;
    private ProfileFragment fragment_profile;
    private MessagesFragment fragment_messages;
    private Fragment[] fragments = {fragment_home, fragment_groups, fragment_events, fragment_profile, fragment_messages};
    private int currentScreen;
    private final int HOME = 0;
    private final int GROUPS = 1;
    private final int EVENTS = 2;
    private final int PROFILE = 3;
    private final int MESSAGES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment_home = new HomeFragment();
        fragment_groups = new GroupsFragment();
        fragment_events = new EventsFragment();
        fragment_profile = new ProfileFragment();
        fragment_messages = new MessagesFragment();

        fragmentManager = getFragmentManager();
        fragment_container = (RelativeLayout) findViewById(R.id.fragment_container);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment_home);
        fragmentTransaction.commit();
        currentScreen = HOME;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show();
                if (currentScreen != HOME) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment_home);
                    fragmentTransaction.commit();
                    currentScreen = HOME;
                    getSupportActionBar().setTitle(fragment_home.getTitle());
                }
                break;
            case R.id.nav_groups:
                Toast.makeText(this, "Groups", Toast.LENGTH_LONG).show();
                if (currentScreen != GROUPS) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment_groups);
                    fragmentTransaction.commit();
                    currentScreen = GROUPS;
                    getSupportActionBar().setTitle(fragment_groups.getTitle());
                }
                break;
            case R.id.nav_events:
                Toast.makeText(this, "Events", Toast.LENGTH_LONG).show();
                if (currentScreen != EVENTS) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment_events);
                    fragmentTransaction.commit();
                    currentScreen = EVENTS;
                    getSupportActionBar().setTitle(fragment_events.getTitle());
                }
                break;

            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
                if (currentScreen != PROFILE) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment_profile);
                    fragmentTransaction.commit();
                    currentScreen = PROFILE;
                    getSupportActionBar().setTitle(fragment_profile.getTitle());
                }
                break;
            case R.id.nav_messages:
                Toast.makeText(this, "Messages", Toast.LENGTH_LONG).show();
                if (currentScreen != MESSAGES) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment_messages);
                    fragmentTransaction.commit();
                    currentScreen = MESSAGES;
                    getSupportActionBar().setTitle(fragment_messages.getTitle());
                }
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
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

}
