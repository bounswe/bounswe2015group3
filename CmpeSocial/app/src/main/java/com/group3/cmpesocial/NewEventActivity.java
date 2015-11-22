package com.group3.cmpesocial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = NewEventActivity.class.getSimpleName();

    private EditText nameEditText;
    private TextView dateEditText;
    private TextView timeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Button doneButton;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        dateEditText = (TextView) findViewById(R.id.dateTextView);
        timeEditText = (TextView) findViewById(R.id.timeTextView);
        locationEditText = (EditText) findViewById(R.id.locationTextView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
    }

    public void createEvent(View v) {
        if (nameEditText.getText() == null || nameEditText.getText().length() == 0 ||
                dateEditText.getText() == null || dateEditText.getText().length() == 0 ||
                timeEditText.getText() == null || timeEditText.getText().length() == 0 ||
                locationEditText.getText() == null || locationEditText.getText().length() == 0 ||
                descriptionEditText.getText() == null || descriptionEditText.getText().length() == 0 ){
            Toast.makeText(this, "Please fill every field", Toast.LENGTH_LONG).show();
            return;
        }
        String name = nameEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        int user_id = getSharedPreferences("prefsCMPE",MODE_PRIVATE).getInt("user_id", 0);
        Log.i("user_id ", "" + user_id);

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("date", date + " " + time);
        json.addProperty("id_user", user_id);
        json.addProperty("location", location);
        json.addProperty("description", description);

        int result = API.createEvent(json, this);
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event created");
            finish();
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void pickDate(View v){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeEditText.setText(""+hourOfDay+":"+minute+":00");
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

}
