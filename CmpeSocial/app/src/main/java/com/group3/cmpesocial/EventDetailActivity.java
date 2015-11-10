package com.group3.cmpesocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getSimpleName();

    private TextView nameTextView;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        Log.i(TAG, ""+id);
        String name = (String) extras.get("name");
        String date = (String) extras.get("date");
        String location = (String) extras.get("location");
        String description = (String) extras.get("description");

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        nameTextView.setText(name);
        dateTextView.setText(date);
        locationTextView.setText(location);
        descriptionTextView.setText(description);

    }

    public void deleteEvent(View v){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        Ion.with(this)
                .load("http://54.148.86.208:8080/cmpesocial/api/events/delete")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
                        } else if (result != null) {
                            Log.i(TAG, "result not null");
                            Log.i(TAG, "" + result.toString());
                            String type = trimQuotes(result.get("Result").toString());
                            Log.i(TAG, "type: " + type);
                            if (type.equals("SUCCESS")) {
                                Toast.makeText(getApplicationContext(), "Event deleted", Toast.LENGTH_LONG).show();
                            } else{
                                Log.i(TAG, "type: " + type.toString());
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
    }

    protected String trimQuotes(String s){
        if (s.charAt(0) == '\"' && s.charAt(s.length()-1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }
}
