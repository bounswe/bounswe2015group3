package com.group3.cmpesocial.activities.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;

public class NewGroupActivity extends AppCompatActivity {

    private static final String TAG = NewGroupActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText descriptionEditText;

    private Toolbar toolbar;


    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void createGroup(View v) {
        if (nameEditText.getText() == null || nameEditText.getText().length() == 0 ||
                descriptionEditText.getText() == null || descriptionEditText.getText().length() == 0 ){
            Toast.makeText(this, "Please fill every field", Toast.LENGTH_LONG).show();
            return;
        }

        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("id_admin", user_id);
        json.addProperty("type", 1);
        json.addProperty("group_url", "www.sample.com");
        json.addProperty("description", description);

        Log.i(TAG, json.toString());

        int result = API.createGroup(json, this);
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "group created");
            finish();
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}
