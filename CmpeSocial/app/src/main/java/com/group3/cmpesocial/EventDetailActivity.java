package com.group3.cmpesocial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getSimpleName();

    private Event mEvent;

    private EditText nameEditText;
    private EditText startDateEditText;
    private EditText startTimeEditText;
    private EditText endDateEditText;
    private EditText endTimeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private ListView participantsListView;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private Button doneButton;
    private Button joinButton;
    private Button leaveButton;


    private UserAdapter adapter;

    private String new_start_date;
    private String new_start_time;
    private String new_end_date;
    private String new_end_time;
    private int new_period;

    private int id;
    private int user_id;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startTimeEditText = (EditText) findViewById(R.id.startTimeEditText);
        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        endTimeEditText = (EditText) findViewById(R.id.endTimeEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        spinner = (Spinner) findViewById(R.id.spinner);
        //participantsListView = (ListView) findViewById(R.id.participantsListView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editButton = (ImageButton) findViewById(R.id.editButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        leaveButton = (Button) findViewById(R.id.leaveButton);

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        setListViewHeightBasedOnChildren(participantsListView);
//        participantsListView.setOnTouchListener(new ListView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        // Disallow ScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        // Allow ScrollView to intercept touch events.
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//
//                // Handle ListView touch events.
//                v.onTouchEvent(event);
//                return true;
//            }
//        });

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        mEvent = API.getEvent(json, getApplicationContext());
        ArrayList<User> participants = API.getEventParticipants(json, getApplicationContext());
        Iterator iterator = participants.iterator();
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            if (user.getId() == user_id){
                joinButton.setVisibility(View.GONE);
                leaveButton.setVisibility(View.VISIBLE);
            }
        }

        String name = mEvent.getName();
        int id_user = mEvent.getId_user();
        final int periodic = mEvent.getPeriod();
        String start_date = mEvent.getShowStartDate();
        String start_time = mEvent.getShowStartTime();
        String end_date = mEvent.getShowEndDate();
        String end_time = mEvent.getShowEndTime();
        String location = mEvent.getLocation();
        String description = mEvent.getDescription();

        nameEditText.setText(name);
        startDateEditText.setText(start_date);
        startTimeEditText.setText(start_time);
        endDateEditText.setText(end_date);
        endTimeEditText.setText(end_time);
        locationEditText.setText(location);
        descriptionEditText.setText(description);

        if(user_id == id_user){
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UserAdapter(participants);
        recyclerView.setAdapter(adapter);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Event.periods));
        spinner.setSelection(periodic);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new_period = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(periodic);
            }
        });

        Log.i(TAG, "participants " + participants.toString());
        //participantsListView.setAdapter(adapter);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent(v);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(v);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent(v);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinEvent();
            }
        });

        enableEditTexts(false);

        new_start_date = mEvent.getStartDateString();
        new_start_time = mEvent.getStartTimeString();
        new_end_date = mEvent.getEndDateString();
        new_end_time = mEvent.getEndTimeString();
    }

    public void enableEditTexts(boolean enabled){
        nameEditText.setEnabled(enabled);
        startDateEditText.setEnabled(enabled);
        startTimeEditText.setEnabled(enabled);
        endDateEditText.setEnabled(enabled);
        endTimeEditText.setEnabled(enabled);
        locationEditText.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
        spinner.setEnabled(enabled);
    }

    public void deleteEvent(View v){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        int result = API.deleteEvent(json, getApplicationContext());
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event deleted");
            finish();
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void editEvent(View v){
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.VISIBLE);

        enableEditTexts(true);

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v, mEvent.getStartDate(), true);
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("API start time", mEvent.getStartTime()[0] + ":" + mEvent.getStartTime()[1]);
                pickTime(v, mEvent.getStartTime(), true);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v, mEvent.getEndDate(), false);
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("API end time", mEvent.getEndTime()[0] + ":" + mEvent.getEndTime()[1]);
                pickTime(v, mEvent.getEndTime(), false);
            }
        });
    }

    public void saveEvent(View v){
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.GONE);

        enableEditTexts(false);

        String name = nameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = new_start_date + " " + new_start_time;
        String end_date = new_end_date + " " + new_end_time;

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("date", date);
        json.addProperty("end_date", end_date);
        json.addProperty("periodic", new_period);
        json.addProperty("id_user", user_id);
        json.addProperty("location", location);
        json.addProperty("description", description);

        Log.i(TAG, json.toString());

        int result = API.updateEvent(json, this);
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event updated");
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void joinEvent(){
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_event", id);
        int result = API.joinEvent(json, this);
        // Do something with result
    }

    public void pickDate(View v, int[] date, final boolean start){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (start){
                    new_start_date = year + "-" + month + "-" + day;
                    startDateEditText.setText(year + " " + Event.getMonthName(monthOfYear) + " " + dayOfMonth);
                }else{
                    new_end_date = year + "-" + month + "-" + day;
                    endDateEditText.setText(year + " " + Event.getMonthName(monthOfYear) + " " + dayOfMonth);
                }
            }
        }, year, month, day);
        datePickerDialog.updateDate(date[2], date[1], date[0]);
        datePickerDialog.show();
    }

    public void pickTime(View v, int[] time, final boolean start){
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(start) {
                    new_start_time = hourOfDay + ":" + minute + ":00";
                    startTimeEditText.setText(hourOfDay + ":" + minute);
                }else{
                    new_end_time = hourOfDay + ":" + minute + ":00";
                    endTimeEditText.setText(hourOfDay + ":" + minute);
                }
            }
        }, hour, minute, true);
        timePickerDialog.updateTime(time[0], time[1]);
        timePickerDialog.show();
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        private ArrayList<User> users;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView nameTextView;
            public Button profileButton;
            public ViewHolder(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
                profileButton = (Button) itemView.findViewById(R.id.profileButton);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public UserAdapter(ArrayList<User> users) {
            this.users = users;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_detail_user, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            User mUser = users.get(position);
            String name = mUser.getName();
            String surname = mUser.getSurname();
            holder.nameTextView.setText(name + " " + surname);
            holder.profileButton.setText(name.charAt(0) + "" + surname.charAt(0));
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return users.size();
        }

        public void add(User item) {
            users.add(item);
            notifyItemInserted(getItemCount());
        }

        public void remove(User item) {
            int position = users.indexOf(item);
            users.remove(position);
            notifyItemRemoved(position);
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
