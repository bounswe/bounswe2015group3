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

        int result = API.deleteEvent(json, getApplicationContext());
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event deleted");
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
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

        String dateAPIFormat = "";



        String[] parts = date.split(" ");
        System.out.println(parts[0]);
        int month = 0;
        int day = 0;
        if(parts[0].equals("Jan")) {
            month = 1;
        }else if(parts[0].equals("Feb")) {
            month = 2;
        }else if(parts[0].equals("Mar")) {
            month = 3;
        }else if(parts[0].equals("Apr")) {
            month = 4;
        }else if(parts[0].equals("May")) {
            month = 5;
        }else if(parts[0].equals("Jun")) {
            month = 6;
        }else if(parts[0].equals("Jul")) {
            month = 7;
        }else if(parts[0].equals("Aug")) {
            month = 8;
        }else if(parts[0].equals("Sep")) {
            month = 9;
        }else if(parts[0].equals("Oct")) {
            month = 10;
        }else if(parts[0].equals("Nov")) {
            month = 11;
        }else if(parts[0].equals("Dec")) {
            month = 12;
        }else {
            System.out.println("hata");
        }
        day = Integer.parseInt(parts[1]);
        dateAPIFormat = "2015-" + month + "-" + day;
        System.out.println(dateAPIFormat);



        String timeAPIFormat = "";
        String[] partsTime = time.split(" ");
        System.out.println(partsTime[0]);
        System.out.println(partsTime[1]);

        String end_date = "";
        int periodic = 0;

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("date", "Nov 28, 2015 12:00:00 AM");
        json.addProperty("end_date", "Nov 28, 2015 12:00:00 AM");
        json.addProperty("periodic", periodic);
        json.addProperty("date_of_creation", "Nov 21, 2015 4:17:27 PM");
        json.addProperty("id_user", 1);
        json.addProperty("location", location);
        json.addProperty("description", description);



        Log.i(TAG, json.toString());

        int result = API.updateEvent(json, getApplicationContext());
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event updated");
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
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
