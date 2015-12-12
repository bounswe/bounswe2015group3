package com.group3.cmpesocial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

    private EditText nameEditText;
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
    private Button doneButton;
    private Button joinButton;
    private Button leaveButton;
    private Button postButton;

    private PostAdapter adapterPost;
    private UserAdapter adapter;

    private String new_start_date;
    private String new_start_time;
    private String new_end_date;
    private String new_end_time;
    private int new_period;

    private ArrayList<Post> postsArray;

    private int id;
    private int user_id;
    private User currentUser;
    private Event currentEvent;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        //View mView = inflater.inflate(R.layout.activity_event_detail, container, false);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnTouchListener(new ListView.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int action = event.getAction();
                switch (action)
                {
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
        //setListViewHeightBasedOnChildren(listView);
        //View header = getLayoutInflater().inflate(R.layout.activity_event_detail, null);

        //listView.addHeaderView(header);

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
        postEditTextMain = (EditText) findViewById(R.id.postEditTextMain);
        spinner = (Spinner) findViewById(R.id.spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editButton = (ImageButton) findViewById(R.id.editButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        postButton = (Button) findViewById(R.id.postButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        leaveButton = (Button) findViewById(R.id.leaveButton);

        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user_id);
        currentUser = API.getUser(userJson, this);

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
        postButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postButton();
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
        postsArray = new ArrayList<Post>();
        // Create the adapter to convert the array to views
        adapterPost = new PostAdapter(this, postsArray);


        // Attach the adapter to a ListView

        listView.setAdapter(adapterPost);
        //postsArray.add(newPost1);
        //postsArray.add(newPost2);
        //adapterPost.addAll(postsArray);

        JsonObject json2 = new JsonObject();
        json2.addProperty("id", id);
        API.getEvent(json2, this);


        ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());

        Log.i(TAG, json2.toString());
        Collections.reverse(posts);
        adapterPost.addAll(posts);


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

        Toast.makeText(this, "joined event", Toast.LENGTH_SHORT).show();

        Log.i(TAG, currentUser.toString());
        adapter.add(currentUser);
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
                    new_start_time = hourOfDay + ":" + minute + ":00";
                    if(minute < 10)
                        startTimeEditText.setText(hourOfDay + ":0" + minute);
                    else
                        startTimeEditText.setText(hourOfDay + ":" + minute);
                }else{
                    new_end_time = hourOfDay + ":" + minute + ":00";
                    if(minute < 10)
                        endTimeEditText.setText(hourOfDay + ":0" + minute);
                    else
                        endTimeEditText.setText(hourOfDay + ":" + minute);
                }
            }
        }, hour, minute, true);
        timePickerDialog.updateTime(time[0], time[1]);
        timePickerDialog.show();
    }

    public class PostAdapter extends ArrayAdapter<Post> {
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
            }
        };

    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        private ArrayList<User> users;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder{
            // each data item is just a string in this case
            public TextView nameTextView;
            public Button profileButton;
            public int id;
            public ViewHolder(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
                profileButton = (Button) itemView.findViewById(R.id.profileButton);
                profileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UserDetailActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
            }
            public void setId(int id){
                this.id = id;
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
            holder.setId(mUser.getId());
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

    public void postButton() {
        String post = postEditTextMain.getText().toString();
        Post newPost = new Post(post);
        //postsArray.add(newPost);
        //adapterPost.clear();
        //adapterPost.addAll(postsArray);
        //listView.setAdapter(adapterPost);
        adapterPost = new PostAdapter(this, postsArray);


        // Attach the adapter to a ListView

        listView.setAdapter(adapterPost);
        postsArray.add(0,newPost);


        long id_event = id;
        long id_user = currentUser.getId();
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
        API.getEvent(json2, this);


        ArrayList<Post> posts = API.getAllEventPosts(json2, getApplicationContext());

        Log.i(TAG, json2.toString());
        Collections.reverse(posts);
        adapterPost.clear();
        adapterPost.addAll(posts);
        postEditTextMain.setText("");
        //Post p = posts.get(0);
        //System.out.println(p.getPost());



    }



    //public class Utility {
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    //}
}
