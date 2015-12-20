package com.group3.cmpesocial.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.UserAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.ViewPagerAdapter;
import com.group3.cmpesocial.classes.User;
import com.group3.cmpesocial.fragments.JoinedEventsFragment;
import com.group3.cmpesocial.fragments.JoinedGroupsFragment;
import com.group3.cmpesocial.fragments.MyEventsFragment;
import com.group3.cmpesocial.fragments.MyGroupsFragment;
import com.group3.cmpesocial.imgur.helpers.DocumentHelper;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.services.UploadService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserDetailActivity extends AppCompatActivity {

    private static final String TAG = UserDetailActivity.class.getSimpleName();

    private ImageView profileImageView;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private int id;
    private User mUser;
    private String url;

    private static MyEventsFragment myEventsFragment;
    private static JoinedEventsFragment joinedEventsFragment;
    private static MyGroupsFragment myGroupsFragment;
    private static JoinedGroupsFragment joinedGroupsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        mUser = UserAPI.getUser(json, this);

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "herehere");
            }
        });

        String name = mUser.getName();
        String surname = mUser.getSurname();
        collapsingToolbarLayout.setTitle(name + " " + surname);

        myEventsFragment = new MyEventsFragment();
        myEventsFragment.setUserID(id);
        joinedEventsFragment = new JoinedEventsFragment();
        joinedEventsFragment.setUserID(id);
        myGroupsFragment = new MyGroupsFragment();
        myGroupsFragment.setUserID(id);
        joinedGroupsFragment = new JoinedGroupsFragment();
        joinedGroupsFragment.setUserID(id);

        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        fragments.add(myEventsFragment);
        fragments.add(joinedEventsFragment);
        fragments.add(myGroupsFragment);
        fragments.add(joinedGroupsFragment);
        titles.add(myEventsFragment.getTitle());
        titles.add(joinedEventsFragment.getTitle());
        titles.add(myGroupsFragment.getTitle());
        titles.add(joinedGroupsFragment.getTitle());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

        returnUri = data.getData();
        String filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        File chosenFile = new File(filePath);
        Log.d(TAG, "got file");

        new UploadService(this).Execute(chosenFile, new UiCallback());
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "here");
    }

    public void setImage(View v){
        Log.d(TAG, "setImage");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(UserDetailActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            url = imageResponse.data.link;
            Log.d(TAG, imageResponse.data.link);

            Picasso.with(UserDetailActivity.this).load(url).into(profileImageView);
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Toast.makeText(UserDetailActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(UserDetailActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

}