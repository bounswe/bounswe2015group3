package com.group3.cmpesocial.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;


public class ProfileFragment extends Fragment {

    private final String title = "Profile";
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView emailTextView;
    private Button changePasswordButton;
    private int id;
    private String name;
    private String surname;
    private String email;

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

        changePasswordButton = (Button) mView.findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(v);
            }
        });

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefsCMPE", Context.MODE_PRIVATE);
        id = prefs.getInt("user_id", 0);
        name = prefs.getString("name", "def_name");
        surname = prefs.getString("surname", "def_surname");
        email = prefs.getString("email", "def_email");

        nameTextView.setText(name);
        surnameTextView.setText(surname);
        emailTextView.setText(email);

        return mView;
    }

    public String getTitle(){
        return title;
    }

    public void changePassword(View v){
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
                        if (password.length() == 0){
                            dialog.cancel();
                        }else{
                            password = password.trim();
                            JsonObject json = new JsonObject();
                            json.addProperty("id", id);
                            json.addProperty("name", name);
                            json.addProperty("surname", surname);
                            json.addProperty("password", password);
                            json.addProperty("email", email);
                            Log.i("API", json.toString());
                            API.updateUser(json, getActivity());
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

}
