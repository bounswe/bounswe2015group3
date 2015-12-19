package com.group3.cmpesocial.activities.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.EventAPI;
import com.group3.cmpesocial.API.UserAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.UserAdapter;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.Post;
import com.group3.cmpesocial.classes.User;
import com.group3.cmpesocial.imgur.helpers.DocumentHelper;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.services.UploadService;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getSimpleName();

    private Event mEvent;
    private User mUser;

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
    private FloatingActionButton editButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton roleButton;
    private FloatingActionButton imageButton;
    private FloatingActionButton doneButton;
    private Button postButton;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private UserAdapter adapter;
    private PostAdapter adapterPost;

    private String url;
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
    private boolean isOwner = false;
    private boolean hasJoined = false;
    private File chosenFile;

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
        editButton = (FloatingActionButton) findViewById(R.id.editButton);
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
        roleButton = (FloatingActionButton) findViewById(R.id.roleButton);
        imageButton = (FloatingActionButton) findViewById(R.id.imageButton);
        doneButton = (FloatingActionButton) findViewById(R.id.doneButton);
        postButton = (Button) findViewById(R.id.postButton);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        postButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                      public void onClick(View v) {
                               postButton();
            }
        });

        if (isOwner){
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            roleButton.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
        }else{
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            roleButton.setVisibility(View.GONE);
            imageButton.setVisibility(View.GONE);
        }

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user_id);
        mUser = UserAPI.getUser(userJson, this);

        JsonObject json = new JsonObject();
        json.addProperty("id_event", id);
        json.addProperty("id_user", user_id);
        mEvent = EventAPI.getEvent(json, getApplicationContext());
        ArrayList<User> participants = EventAPI.getEventParticipants(json, getApplicationContext());
        if(mEvent.getHasJoined()){
            hasJoined = true;
        }

        JsonObject tagsJson = new JsonObject();
        tagsJson.addProperty("id", id);
        tags = EventAPI.getEventTags(tagsJson, this);
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

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(name);

        periodTextView.setText(Event.periods[periodic]);
        startDateEditText.setText(start_date);
        startTimeEditText.setText(start_time);
        endDateEditText.setText(end_date);
        endTimeEditText.setText(end_time);
        locationEditText.setText(location);
        descriptionEditText.setText(description);

        if(user_id == id_user){
            isOwner = true;
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
        url = mEvent.getUrl();

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
        ArrayList<Post> posts = EventAPI.getAllEventPosts(json2, getApplicationContext());
//        System.out.println(posts.get(0).getPost());
        Collections.reverse(posts);
        adapterPost = new PostAdapter(this,posts);
        listView.setAdapter(adapterPost);
        //Collections.reverse(posts);
        //adapterPost.clear();
        //adapterPost.addAll(posts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (hasJoined){
            getMenuInflater().inflate(R.menu.main_with_leave, menu);
        }else{
            getMenuInflater().inflate(R.menu.main_with_join, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.join:
                joinEvent();
                break;

            case R.id.leave:
                leaveEvent();
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri returnUri;

        if (requestCode != 100) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        returnUri = data.getData();
        String filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);
        Log.d(TAG, "got file");

        new UploadService(this).Execute(chosenFile, new UiCallback());
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "here");
    }

    public void enableEditTexts(boolean enabled){
        tagsEditText.setEnabled(enabled);
        startDateEditText.setEnabled(enabled);
        startTimeEditText.setEnabled(enabled);
        endDateEditText.setEnabled(enabled);
        endTimeEditText.setEnabled(enabled);
        locationEditText.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
        if (enabled) {
            periodTextView.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        }else {
            periodTextView.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
        spinner.setEnabled(enabled);
    }

    public void deleteEvent(View v){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        int result = EventAPI.deleteEvent(json, getApplicationContext());
        if (result == EventAPI.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == EventAPI.SUCCESS){
            Log.i(TAG, "event deleted");
            finish();
        }else if (result == EventAPI.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void editEvent(View v){
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        imageButton.setVisibility(View.VISIBLE);
        roleButton.setVisibility(View.VISIBLE);
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
                pickTime(v, mEvent.getEndTime(), false);
            }
        });
    }

    public void saveEvent(View v){
        Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
        editButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.GONE);
        roleButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);

        enableEditTexts(false);

        String name = mEvent.getName();
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
        if (url == null) {
            json.addProperty("url", mEvent.getUrl());
        } else {
            json.addProperty("url", url);
        }
        json.addProperty("id_group", mEvent.getId_group());

        Log.i(TAG, json.toString());

        int result = EventAPI.updateEvent(json, this);
        if (result == EventAPI.ERROR){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }else if (result == EventAPI.SUCCESS){
            Log.i(TAG, "event updated");
        }else if (result == EventAPI.RESULT_EMPTY){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        updateTags(tagsString, this.tags);
    }

    public void joinEvent(){
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_event", id);
        int result = EventAPI.joinEvent(json, this);
        Log.i(TAG, ""+result);
        if (result == EventAPI.SUCCESS) {
            Toast.makeText(this, "joined event", Toast.LENGTH_SHORT).show();
            adapter.add(mUser);
            hasJoined = true;
            invalidateOptionsMenu();
        }else if (result == EventAPI.NO_ACCESS){
            Toast.makeText(this, "You cannot join this event.", Toast.LENGTH_SHORT).show();
        }
    }

    public void leaveEvent(){
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_event", id);
        int result = EventAPI.leaveEvent(json, this);
        Log.i(TAG, ""+result);
        if (result == EventAPI.SUCCESS) {
            Toast.makeText(this, "left event", Toast.LENGTH_SHORT).show();
            adapter.add(mUser);
            hasJoined = false;
            invalidateOptionsMenu();
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
                                    allowedRoles.add(which+1);
                                } else if (allowedRoles.contains(which+1)) {
                                    // Else, if the item is already in the array, remove it
                                    allowedRoles.remove(Integer.valueOf(which+1));
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

    public void setImage(View v){
        Log.d(TAG, "setImage");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
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
            json.addProperty("id_event", id);
            json.addProperty("tag", tag);
            Log.i(TAG, "delete event tag " + json.toString());
            EventAPI.deleteEventTag(json, this);
        }
        if (!tagsString.equals("")){
            String[] tagsArray = tagsString.split(" ");
            for (int i = 0; i < tagsArray.length; i++){
                if (!tags.contains(tagsArray[i])){
                    JsonObject json = new JsonObject();
                    json.addProperty("id_event", id);
                    json.addProperty("tag", tagsArray[i]);
                    EventAPI.addEventTag(json, this);
                }
            }
        }
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(EventDetailActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            url = imageResponse.data.link;
            Log.d(TAG, imageResponse.data.link);
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Toast.makeText(EventDetailActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(EventDetailActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);

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
            User userTemp = UserAPI.getUser(json, getContext());
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
                EventAPI.deleteEventPost(json, getContext());


                JsonObject json2 = new JsonObject();
                //json2.addProperty("id_user", user_id);
                json2.addProperty("id", id);
                //API.getEvent(json2, getContext());

                ArrayList<Post> posts = EventAPI.getAllEventPosts(json2, getApplicationContext());

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
                        EventAPI.updateEventPost(json, getContext());
                        //API.deleteEventPost(json, getContext());
                        JsonObject json2 = new JsonObject();
                        json2.addProperty("id", p.getEventID());
                        //json2.addProperty("id_user", user_id);
                        //API.getEvent(json2, getContext());
                        ArrayList<Post> posts = EventAPI.getAllEventPosts(json2, getApplicationContext());
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
                        EventAPI.createEventComment(json, getApplicationContext());
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

        EventAPI.createEventPost(json, this);

        JsonObject json2 = new JsonObject();
        json2.addProperty("id", id_event);
        //json2.addProperty("id_user", user_id);
        //API.getEvent(json2, this);


        ArrayList<Post> posts = EventAPI.getAllEventPosts(json2, getApplicationContext());

        Log.i(TAG, json2.toString());
        Collections.reverse(posts);
        adapterPost.clear();
        adapterPost.addAll(posts);
        postEditTextMain.setText("");
        //Post p = posts.get(0);
        //System.out.println(p.getPost());

    }

}
