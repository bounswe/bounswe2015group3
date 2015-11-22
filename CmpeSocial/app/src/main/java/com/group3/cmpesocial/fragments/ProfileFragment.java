package com.group3.cmpesocial.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.group3.cmpesocial.R;


public class ProfileFragment extends Fragment {

    private final String title = "Profile";
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView emailTextView;

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

        profileImageView = (ImageView) mView.findViewById(R.id.profileImageView);
        nameTextView = (TextView) mView.findViewById(R.id.nameTextView);
        surnameTextView = (TextView) mView.findViewById(R.id.surnameTextView);
        emailTextView = (TextView) mView.findViewById(R.id.emailTextView);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefsCMPE", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "def_name");
        String surname = prefs.getString("surname", "def_surname");
        String email = prefs.getString("email", "def_email");

        nameTextView.setText(name);
        surnameTextView.setText(surname);
        emailTextView.setText(email);

        return mView;
    }

    public String getTitle(){
        return title;
    }

    public void changePassword(View v){

    }

    public void changeEmailAddress(View v){

    }

}
