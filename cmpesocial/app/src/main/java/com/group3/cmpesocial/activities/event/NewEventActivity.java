package com.group3.cmpesocial.activities.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Event;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = NewEventActivity.class.getSimpleName();

    private EditText nameEditText;
    private TextView startDateEditText;
    private TextView startTimeEditText;
    private TextView endDateEditText;
    private TextView endTimeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Spinner spinner;
    private Toolbar toolbar;

    private String new_start_date;
    private int[] new_start_date_array = new int[3];
    private String new_end_date;
    private String new_start_time;
    private int[] new_start_time_array = new int[2];
    private String new_end_time;
    private int periodic;

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startTimeEditText = (EditText) findViewById(R.id.startTimeEditText);
        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        endTimeEditText = (EditText) findViewById(R.id.endTimeEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        spinner = (Spinner) findViewById(R.id.spinner);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v, true);
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(v, true);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v, false);
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(v, false);
            }
        });

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Event.periods));
        spinner.setSelection(periodic);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                periodic = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(periodic);
            }
        });

        new_start_date = "";
        new_end_date = "";
        new_start_time = "";
        new_end_time = "";
    }

    public void createEvent(View v) {
        if (nameEditText.getText() == null || nameEditText.getText().length() == 0 ||
                startDateEditText.getText() == null || startDateEditText.getText().length() == 0 ||
                startTimeEditText.getText() == null || startTimeEditText.getText().length() == 0 ||
                endDateEditText.getText() == null || endDateEditText.getText().length() == 0 ||
                endTimeEditText.getText() == null || endTimeEditText.getText().length() == 0 ||
                locationEditText.getText() == null || locationEditText.getText().length() == 0 ||
                descriptionEditText.getText() == null || descriptionEditText.getText().length() == 0) {
            Toast.makeText(this, "Please fill every field", Toast.LENGTH_LONG).show();
            return;
        }

        String name = nameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = new_start_date + " " + new_start_time;
        String end_date = new_end_date + " " + new_end_time;

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("date", date);
        json.addProperty("end_date", end_date);
        json.addProperty("periodic", periodic);
        json.addProperty("id_user", user_id);
        json.addProperty("location", location);
        json.addProperty("description", description);

        Log.i(TAG, json.toString());

        int result = API.createEvent(json, this);
        if (result == API.ERROR) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        } else if (result == API.SUCCESS) {
            Log.i(TAG, "event created");
            finish();
        } else if (result == API.RESULT_EMPTY) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public void pickDate(View v, final boolean start) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (start) {
                    new_start_date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    new_start_date_array[0] = dayOfMonth;
                    new_start_date_array[1] = monthOfYear;
                    new_start_date_array[2] = year;
                    startDateEditText.setText(dayOfMonth + " " + Event.getMonthName(monthOfYear) + " " + year);
                } else {
                    new_end_date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    endDateEditText.setText(dayOfMonth + " " + Event.getMonthName(monthOfYear) + " " + year);
                }
            }
        }, year, month, day);
        if(!start)
            datePickerDialog.updateDate(new_start_date_array[2], new_start_date_array[1], new_start_date_array[0]);
        datePickerDialog.show();
    }

    public void pickTime(View v, final boolean start) {
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (start) {
                    new_start_time_array[0] = hourOfDay;
                    new_start_time_array[1] = minute;
                    if (minute < 10) {
                        new_start_time = hourOfDay + ":0" + minute + ":00";
                        startTimeEditText.setText(hourOfDay + ":0" + minute);
                    } else {
                        new_start_time = hourOfDay + ":" + minute + ":00";
                        startTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                } else {
                    if (minute < 10) {
                        new_end_time = hourOfDay + ":0" + minute + ":00";
                        endTimeEditText.setText(hourOfDay + ":0" + minute);
                    } else {
                        new_end_time = hourOfDay + ":" + minute + ":00";
                        endTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                }
            }
        }, hour, minute, true);
        if (!start)
            timePickerDialog.updateTime(new_start_time_array[0] + 1, new_start_time_array[1]);
        timePickerDialog.show();
    }

}
