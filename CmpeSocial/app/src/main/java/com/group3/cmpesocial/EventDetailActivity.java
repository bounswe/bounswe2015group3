package com.group3.cmpesocial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Button editButton;
    private Button doneButton;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        String name = (String) extras.get("name");
        String date = (String) extras.get("date");
        String time = (String) extras.get("time");
        String location = (String) extras.get("location");
        String description = (String) extras.get("description");

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        editButton = (Button) findViewById(R.id.editButton);
        doneButton = (Button) findViewById(R.id.doneButton);

        nameEditText.setText(name);
        dateEditText.setText(date);
        timeEditText.setText(time);
        locationEditText.setText(location);
        descriptionEditText.setText(description);

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

    public void editEvent(View v){
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.VISIBLE);
        nameEditText.setFocusableInTouchMode(true);
        dateEditText.setFocusableInTouchMode(true);
        locationEditText.setFocusableInTouchMode(true);
        descriptionEditText.setFocusableInTouchMode(true);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v);
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(v);
            }
        });
    }

    public void saveEvent(View v){
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.GONE);

        int user_id = getSharedPreferences("prefsCMPE",MODE_PRIVATE).getInt("user_id", 0);

        nameEditText.setFocusableInTouchMode(false);
        dateEditText.setFocusableInTouchMode(false);
        timeEditText.setFocusableInTouchMode(false);
        locationEditText.setFocusableInTouchMode(false);
        descriptionEditText.setFocusableInTouchMode(false);

        String name = nameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("date", date + " " + time);
        json.addProperty("id_user", user_id);
        json.addProperty("location", location);
        json.addProperty("description", description);

        Log.i(TAG, json.toString());
        Ion.with(this)
                .load("http://54.148.86.208:8080/cmpesocial/api/events/update")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (result == null)
                            Log.i(TAG, "result null");
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

    public void pickDate(View v){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateEditText.setText(""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void pickTime(View v){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeEditText.setText(""+hourOfDay+":"+minute+":00");
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
}
