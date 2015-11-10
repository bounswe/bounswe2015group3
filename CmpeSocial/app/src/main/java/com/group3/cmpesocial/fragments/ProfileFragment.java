package com.group3.cmpesocial.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group3.cmpesocial.R;


public class ProfileFragment extends Fragment {

    private final String title = "Profile";
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView emailTextView;
    private Button updateButton;

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
        updateButton = (Button) mView.findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(v);
            }
        });

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

    public void updateView(View v){
        Toast.makeText(getActivity(), "update", Toast.LENGTH_LONG).show();
    }

}
