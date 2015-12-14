package com.group3.cmpesocial.activities.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.UserAdapter;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.Post;
import com.group3.cmpesocial.classes.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getSimpleName();

    private Event mEvent;
    private User mUser;

    private EditText nameEditText;
    private EditText tagsEditText;
    private TextView periodTextView;
    private EditText startDateEditText;
    private EditText startTimeEditText;
    private EditText endDateEditText;
    private EditText endTimeEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private EditText postEditTextMain;
    private RecyclerView recyclerView;
    private ListView listView;
    private Spinner spinner;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private ImageButton inviteButton;
    private Button doneButton;
    private Button joinButton;
    private Button leaveButton;
    private Button postButton;

    private UserAdapter adapter;
    private PostAdapter adapterPost;

    private String new_start_date;
    private String new_start_time;
    private String new_end_date;
    private String new_end_time;
    private int new_period;

    private ArrayList<Post> postsArray;
    private ArrayList<Integer> allowedRoles;
    private ArrayList<String> tags;

    private int id;
    private int user_id;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        tagsEditText = (EditText) findViewById(R.id.tagsEditText);
        periodTextView = (TextView) findViewById(R.id.periodTextView);
        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startTimeEditText = (EditText) findViewById(R.id.startTimeEditText);
        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        endTimeEditText = (EditText) findViewById(R.id.endTimeEditText);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        postEditTextMain = (EditText) findViewById(R.id.postEditTextMain);
        spinner = (Spinner) findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editButton = (ImageButton) findViewById(R.id.editButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        inviteButton = (ImageButton) findViewById(R.id.inviteButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        postButton = (Button) findViewById(R.id.postButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        leaveButton = (Button) findViewById(R.id.leaveButton);

        postButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                      public void onClick(View v) {
                               postButton();
            }
        });

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user_id);
        mUser = API.getUser(userJson, this);

        JsonObject json = new JsonObject();
        json.addProperty("id_event", id);
        json.addProperty("id_user", user_id);
        mEvent = API.getEvent(json, getApplicationContext());
        ArrayList<User> participants = API.getEventParticipants(json, getApplicationContext());
        if(mEvent.getHasJoined()){
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
        }else{
            joinButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
        }

        JsonObject tagsJson = new JsonObject();
        json.addProperty("id", id);
        tags = API.getEventTags(tagsJson, this);
        Iterator iterator = tags.iterator();
        String tagsString = "";
        while (iterator.hasNext())
            tagsString += iterator.next() + " ";
        tagsEditText.setText(tagsString);

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
        periodTextView.setText(Event.periods[periodic]);
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
        adapter = new UserAdapter(participants, this);
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

        enableEditTexts(false);

        new_start_date = mEvent.getStartDateString();
        new_start_time = mEvent.getStartTimeString();
        new_end_date = mEvent.getEndDateString();
        new_end_time = mEvent.getEndTimeString();

        // Add item to adapter
        Post newPost1 = new Post("Can'ın odasını boyamayı gönülden istiyorum.");
        Post newPost2 = new Post("Bence Ali duvara bir şeyler çizsin, biz de onu izleyip eğlenelim.");


        // Construct the data source
        //postsArray = new ArrayList<Post>();
        // Create the adapter to convert the array to views
        //adapterPost = new PostAdapter(this, postsArray);


        // Attach the adapter to a ListView

        //listView.setAdapter(adapterPost);
        //postsArray.add(newPost1);
        //postsArray.add(newPost2);
        //adapterPost.addAll(postsArray);

        //JsonObject json2 = new JsonObject();
        //json2.addProperty("id_event", id);
        //json2.addProperty("id_user", user_id);
        //API.getEvent(json2, this);


        //ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());


        JsonObject json2 = new JsonObject();
        json2.addProperty("id", id);
        //json2.addProperty("id_user", user_id);
        //API.getEvent(json2, getApplicationContext());
        ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());
        System.out.println(posts.get(0).getPost());
        Collections.reverse(posts);
        adapterPost = new PostAdapter(this,posts);
        listView.setAdapter(adapterPost);
        //Collections.reverse(posts);
        //adapterPost.clear();
        //adapterPost.addAll(posts);
    }

    public void enableEditTexts(boolean enabled){
        nameEditText.setEnabled(enabled);
        tagsEditText.setEnabled(enabled);
        startDateEditText.setEnabled(enabled);
        startTimeEditText.setEnabled(enabled);
        endDateEditText.setEnabled(enabled);
        endTimeEditText.setEnabled(enabled);
        locationEditText.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
        spinner.setEnabled(enabled);
        if (enabled)
            periodTextView.setVisibility(View.GONE);
        else
            periodTextView.setVisibility(View.VISIBLE);
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
        String tagsString = "";
        if (tagsEditText.getText() != null) {
            tagsString = tagsEditText.getText().toString().trim();
        }
        String location = locationEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = new_start_date + " " + new_start_time;
        String end_date = new_end_date + " " + new_end_time;
        String type = "";
        if (allowedRoles != null) {
            for (int i = 0; i < allowedRoles.size(); i++) {
                type += String.valueOf(allowedRoles.get(i)) + ",";
            }
            type = type.substring(0, type.length() - 1);
        }else{
            type = "0";
        }
        Log.i("type", type);

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("date", date);
        json.addProperty("end_date", end_date);
        json.addProperty("periodic", new_period);
        json.addProperty("id_user", user_id);
        json.addProperty("location", location);
        json.addProperty("description", description);
        json.addProperty("type", type);

        Log.i(TAG, json.toString());

        int result = API.updateEvent(json, this);
        if (result == API.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Log.i(TAG, "event updated");
        }else if (result == API.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        updateTags(tagsString, this.tags);
    }

    public void joinEvent(View v){
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_event", id);
        int result = API.joinEvent(json, this);
        Log.i(TAG, ""+result);
        if (result == API.SUCCESS) {
            Toast.makeText(this, "joined event", Toast.LENGTH_SHORT).show();
            adapter.add(mUser);
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
        }else if (result == API.NO_ACCESS){
            Toast.makeText(this, "You cannot join this event unless you are invited.", Toast.LENGTH_SHORT).show();
        }
    }

    public void leaveEvent(View v){
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_event", id);
        int result = API.leaveEvent(json, this);
        Log.i(TAG, ""+result);
        if (result == API.SUCCESS) {
            Toast.makeText(this, "left event", Toast.LENGTH_SHORT).show();
            adapter.add(mUser);
            joinButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
        }else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void invite(View v){

    }

    public void setRoles(View v){
        Toast.makeText(this, "roles", Toast.LENGTH_SHORT).show();
        allowedRoles = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Who can join?")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.roles_array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    allowedRoles.add(which);
                                } else if (allowedRoles.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    allowedRoles.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
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
                    new_start_date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    startDateEditText.setText(dayOfMonth + " " + Event.getMonthName(monthOfYear) + " " + year);
                }else{
                    new_end_date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                    endDateEditText.setText(dayOfMonth + " " + Event.getMonthName(monthOfYear) + " " + year);
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
                    if(minute < 10){
                        new_start_time = hourOfDay + ":0" + minute + ":00";
                        startTimeEditText.setText(hourOfDay + ":0" + minute);
                    }else {
                        new_start_time = hourOfDay + ":" + minute + ":00";
                        startTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                }else{
                    if(minute < 10){
                        new_end_time = hourOfDay + ":0" + minute + ":00";
                        endTimeEditText.setText(hourOfDay + ":0" + minute);
                    }else {
                        new_end_time = hourOfDay + ":" + minute + ":00";
                        endTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                }
            }
        }, hour, minute, true);
        timePickerDialog.updateTime(time[0], time[1]);
        timePickerDialog.show();
    }

    public void updateTags(String tagsString, ArrayList<String> tags){
        Iterator iterator = tags.iterator();
        while (iterator.hasNext()){
            String tag = (String) iterator.next();
            JsonObject json = new JsonObject();
            json.addProperty("id_group", id);
            json.addProperty("tag", tag);
            API.deleteGroupTag(json, this);
        }
        if (!tagsString.equals("")){
            String[] tagsArray = tagsString.split(" ");
            for (int i = 0; i < tagsArray.length; i++){
                if (!tags.contains(tagsArray[i])){
                    JsonObject json = new JsonObject();
                    json.addProperty("id_group", id);
                    json.addProperty("tag", tagsArray[i]);
                    API.addGroupTag(json, this);
                }
            }
        }
    }

    public class PostAdapter extends ArrayAdapter<Post> {
        TextView t;
        //Button b2;
        public PostAdapter(Context context, List objects) {
            super(context, R.layout.item_post, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
            }

            Post mPost = getItem(position);

            TextView postTextView = (TextView) convertView.findViewById(R.id.postEditText);
            Button b = (Button) convertView.findViewById(R.id.deletePost);
            b.setTag(position);
            b.setOnClickListener(delete);

            Button b2 = (Button) convertView.findViewById(R.id.updatePost);
            b2.setTag(position);
            b2.setOnClickListener(update);

            Button b3 = (Button) convertView.findViewById(R.id.comment);
            b3.setTag(position);
            b3.setOnClickListener(comment);

            t = (TextView) convertView.findViewById(R.id.postEditText);
            t.setTag(position);
            int userIDTemp = mPost.getUserID();

            //b.setTag(position);
            JsonObject json = new JsonObject();
            json.addProperty("id", userIDTemp);
            User userTemp = API.getUser(json, getContext());
            String userNameTemp = userTemp.getName();
            String userSurnameTemp = userTemp.getSurname();
            String nameTemp = " - " + userNameTemp + " " + userSurnameTemp;
            //System.out.println(nameTemp);
            String post = mPost.getPost();
            SpannableString ss1 = new SpannableString(post + nameTemp);
            //System.out.println(ss1);

            ss1.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                    post.length() + 1, ss1.length(), 0);
            ss1.setSpan(new ForegroundColorSpan(Color.BLUE), post.length() + 1, ss1.length(), 0);


            //postTextView.setText(post + nameTemp);
            postTextView.setText(ss1);


            return convertView;
        }

        private View.OnClickListener delete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                // System.out.println(position);
                int size = adapterPost.getCount();
                //System.out.println(size-position);
                //int id_post = size-position;
                Post p = adapterPost.getItem(position);
                int id_post = p.getID();
                //   System.out.println(id_post);


                JsonObject json = new JsonObject();
                json.addProperty("id", id_post);
                API.deleteEventPost(json, getContext());


                JsonObject json2 = new JsonObject();
                //json2.addProperty("id_user", user_id);
                json2.addProperty("id", id);
                //API.getEvent(json2, getContext());

                ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());

                //Log.i(TAG, json2.toString());
                Collections.reverse(posts);
                adapterPost.clear();
                adapterPost.addAll(posts);


                /*int result = API.deleteEvent(json, getApplicationContext());
                if (result == API.ERROR){
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }else if (result == API.SUCCESS){
                    Log.i(TAG, "event deleted");
                    finish();
                }else if (result == API.RESULT_EMPTY){
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }*/
            }
        };
        private View.OnClickListener update = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                final Post p = adapterPost.getItem(position);
                String text = p.getPost();
                //e.setClickable(true);

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("Update Post");
                alert.setMessage("You can make changes on your post");

                // Set an EditText view to get user input
                final EditText input = new EditText(getContext());
                input.setText(text);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int id_post = p.getID();
                        JsonObject json = new JsonObject();
                        json.addProperty("id", id_post);
                        json.addProperty("id_user", p.getUserID());
                        json.addProperty("id_event", p.getEventID());
                        json.addProperty("content", input.getText().toString());
                        json.addProperty("content_url", p.getContentURL());
                        API.updateEventPost(json, getContext());
                        //API.deleteEventPost(json, getContext());
                        JsonObject json2 = new JsonObject();
                        json2.addProperty("id", p.getEventID());
                        //json2.addProperty("id_user", user_id);
                        //API.getEvent(json2, getContext());
                        ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());
                        Collections.reverse(posts);
                        adapterPost.clear();
                        adapterPost.addAll(posts);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();



            }
        };
        private View.OnClickListener comment = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                final Post p = adapterPost.getItem(position);
                String text = p.getPost();
                //e.setClickable(true);

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("Comment");
                alert.setMessage("You can comment on this post here");

                // Set an EditText view to get user input
                final EditText input = new EditText(getContext());
                //input.setText(text);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int id_post = p.getID();
                        JsonObject json = new JsonObject();
                        json.addProperty("id_post", id_post);
                        json.addProperty("id_user", user_id);
                        json.addProperty("id_event", p.getEventID());
                        json.addProperty("content", "Bu da bir comment işte.");
                        //json.addProperty("content_url", p.getContentURL());
                        System.out.println(json.toString());
                        API.createEventComment(json, getApplicationContext());
                        /*API.updateEventPost(json, getContext());
                        //API.deleteEventPost(json, getContext());
                        JsonObject json2 = new JsonObject();
                        json2.addProperty("id", id);
                        API.getEvent(json2, getContext());
                        ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());
                        Collections.reverse(posts);
                        adapterPost.clear();
                        adapterPost.addAll(posts);*/
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();



            }
        };
    }


    /*public class PostAdapter extends ArrayAdapter<Post> {
        public PostAdapter(Context context, List objects) {
            super(context, R.layout.item_post, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
            }

            Post mPost = getItem(position);

            TextView postTextView = (TextView) convertView.findViewById(R.id.postEditText);
            Button b = (Button) convertView.findViewById(R.id.deletePost);
            b.setTag(position);
            b.setOnClickListener(myButtonClickListener);

            int userIDTemp = mPost.getUserID();

            //b.setTag(position);
            JsonObject json = new JsonObject();
            json.addProperty("id", userIDTemp);
            User userTemp = API.getUser(json, getContext());
            String userNameTemp = userTemp.getName();
            String userSurnameTemp = userTemp.getSurname();
            String nameTemp = " - " + userNameTemp + " " + userSurnameTemp;
            //System.out.println(nameTemp);
            String post = mPost.getPost();
            SpannableString ss1=  new SpannableString(post + nameTemp);
            //System.out.println(ss1);

            ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC),
                    post.length()+1,ss1.length(), 0);
            ss1.setSpan(new ForegroundColorSpan(Color.BLUE), post.length()+1, ss1.length(), 0);




            //postTextView.setText(post + nameTemp);
            postTextView.setText(ss1);


            return convertView;
        }
        private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                System.out.println(position);
                int size = adapterPost.getCount();
                //System.out.println(size-position);
                //int id_post = size-position;
                Post p = adapterPost.getItem(position);
                int id_post = p.getID();
                System.out.println(id_post);


                JsonObject json = new JsonObject();
                json.addProperty("id", id_post);
                API.deleteEventPost(json, getContext());



                JsonObject json2 = new JsonObject();
                json2.addProperty("id", id);
                API.getEvent(json2, getContext());

                ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());

                //Log.i(TAG, json2.toString());
                Collections.reverse(posts);
                adapterPost.clear();
                adapterPost.addAll(posts);


                /*int result = API.deleteEvent(json, getApplicationContext());
                if (result == API.ERROR){
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }else if (result == API.SUCCESS){
                    Log.i(TAG, "event deleted");
                    finish();
                }else if (result == API.RESULT_EMPTY){
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }*/
            //}
        //};

    //}*/

    public void postButton() {
        String post = postEditTextMain.getText().toString();
        Post newPost = new Post(post);
        //postsArray.add(newPost);
        //adapterPost.clear();
        //adapterPost.addAll(postsArray);
        //listView.setAdapter(adapterPost);
        //adapterPost = new PostAdapter(this, postsArray);


        // Attach the adapter to a ListView

        //listView.setAdapter(adapterPost);
        //postsArray.add(0,newPost);


        long id_event = id;
        long id_user = mUser.getId();
        String content = post;
        String content_url = "dummy";

        JsonObject json = new JsonObject();
        json.addProperty("id_event", id_event);
        json.addProperty("id_user", id_user);
        json.addProperty("content", content);
        json.addProperty("content_url", content_url);

        Log.i(TAG, json.toString());

        API.createEventPost(json, this);

        JsonObject json2 = new JsonObject();
        json2.addProperty("id", id_event);
        //json2.addProperty("id_user", user_id);
        //API.getEvent(json2, this);


        ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());

        Log.i(TAG, json2.toString());
        Collections.reverse(posts);
        adapterPost.clear();
        adapterPost.addAll(posts);
        postEditTextMain.setText("");
        //Post p = posts.get(0);
        //System.out.println(p.getPost());

    }


}
