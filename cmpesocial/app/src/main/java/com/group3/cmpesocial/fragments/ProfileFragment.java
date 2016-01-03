package com.group3.cmpesocial.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.UserAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.ViewPagerAdapter;
import com.group3.cmpesocial.classes.User;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.services.UploadService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileFragment extends Fragment {

    private final static String TAG = ProfileFragment.class.getSimpleName();
    private final static int MY_EVENTS = 0;
    private final static int JOINED_EVENTS = 1;
    private final static int MY_GROUPS = 2;
    private final static int JOINED_GROUPS = 3;
    private MyEventsFragment myEventsFragment;
    private JoinedEventsFragment joinedEventsFragment;
    private MyGroupsFragment myGroupsFragment;
    private JoinedGroupsFragment joinedGroupsFragment;
    private final String title = "Profile";
    private TextView nameTextView;
    private ImageView profileImageView;
    private TextView roleTextView;
    private ProgressBar progressBar;
    private FloatingActionButton changePasswordButton;
    private FloatingActionButton changePictureButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int id;
    private String name;
    private String surname;
    private String email;
    private int type;
    private String url;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = (TextView) mView.findViewById(R.id.nameTextView);
        profileImageView = (ImageView) mView.findViewById(R.id.profileImageView);
        roleTextView = (TextView) mView.findViewById(R.id.roleTextView);
        progressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
        tabLayout = (TabLayout) mView.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        changePictureButton = (FloatingActionButton) mView.findViewById(R.id.changePictureButton);
        changePasswordButton = (FloatingActionButton) mView.findViewById(R.id.changePasswordButton);
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefsCMPE", Context.MODE_PRIVATE);
        id = prefs.getInt("user_id", 0);
        name = prefs.getString("name", "def_name");
        surname = prefs.getString("surname", "def_surname");
        email = prefs.getString("email", "def_email");
        url = prefs.getString("url", "");
        type = prefs.getInt("type", 1);

        nameTextView.setText(name + " " + surname);
        roleTextView.setText(User.TYPES[type]);
        if (url != null && !url.equals(""))
            Picasso.with(getContext()).load(url).into(profileImageView);

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

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return mView;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    public void changePassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Change your password");
        alertDialog.setMessage("Enter new password");

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText() == null)
                            dialog.cancel();
                        String password = input.getText().toString();
                        if (password.length() == 0) {
                            dialog.cancel();
                        } else {
                            password = password.trim();
                            JsonObject json = new JsonObject();
                            json.addProperty("id", id);
                            json.addProperty("name", name);
                            json.addProperty("surname", surname);
                            json.addProperty("password", password);
                            json.addProperty("email", email);
                            json.addProperty("profile_pic_link", url);
                            json.addProperty("type", type);
                            UserAPI.updateUser(json, getActivity());
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void changePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 451);
    }

    public void uploadImage(File chosenFile) {
        new UploadService(getContext()).Execute(chosenFile, new UiCallback());
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "here");
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
            url = imageResponse.data.link;
            Log.d(TAG, imageResponse.data.link);

            Picasso.with(getContext()).load(url).into(profileImageView);

        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
