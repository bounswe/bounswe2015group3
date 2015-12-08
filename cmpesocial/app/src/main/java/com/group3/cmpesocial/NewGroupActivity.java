package com.group3.cmpesocial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.classes.Group;

import java.util.Calendar;

public class NewGroupActivity extends AppCompatActivity {

    private static final String TAG = NewGroupActivity.class.getSimpleName();

    private EditText nameEditText;
    private TextView startDateEditText;
    private TextView startTimeEditText;
    private TextView endDateEditText;
    private TextView endTimeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Spinner spinner;
    private Button doneButton;

    private Toolbar toolbar;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private String new_start_date;
    private String new_end_date;
    private String new_start_time;
    private String new_end_time;
    private int periodic;

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);

        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        spinner = (Spinner) findViewById(R.id.spinner);
        doneButton = (Button) findViewById(R.id.doneButton);

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
            String date_of_creation = "";
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("id_user", user_id);
        json.addProperty("date_of_creation", date_of_creation);
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
