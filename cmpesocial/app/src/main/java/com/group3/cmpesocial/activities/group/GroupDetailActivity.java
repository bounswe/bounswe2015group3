package com.group3.cmpesocial.activities.group;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.EventAPI;
import com.group3.cmpesocial.API.GroupAPI;
import com.group3.cmpesocial.API.UserAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.InviteUserActivity;
import com.group3.cmpesocial.activities.event.NewEventActivity;
import com.group3.cmpesocial.adapters.RVEventAdapter;
import com.group3.cmpesocial.adapters.RVUserAdapter;
import com.group3.cmpesocial.classes.Group;
import com.group3.cmpesocial.classes.Post;
import com.group3.cmpesocial.classes.PostG;
import com.group3.cmpesocial.classes.User;
import com.group3.cmpesocial.imgur.helpers.DocumentHelper;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.services.UploadService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GroupDetailActivity extends AppCompatActivity {

    private static final String TAG = GroupDetailActivity.class.getSimpleName();

    private Group mGroup;
    private User mUser;

    private PostAdapter adapterPost;

    private EditText tagsEditText;
    private EditText descriptionEditText;
    private RecyclerView membersRecyclerView;
    private RecyclerView eventsRecyclerView;
    private FloatingActionButton editButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton roleButton;
    private FloatingActionButton imageButton;
    private FloatingActionButton createEventButton;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ImageView image;
    private EditText postEditTextMain;
    private Button postButton;
    private ListView listView;

    private RVUserAdapter RVUserAdapter;
    private RVEventAdapter RVEventAdapter;

    private int id;
    private int user_id;
    private boolean isMember;

    private ArrayList<Integer> allowedRoles;
    private ArrayList<String> tags;
    private File chosenFile;
    private String url;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = (int) extras.get("id");
        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        listView = (ListView) findViewById(R.id.listViewGroupPost);
        postButton = (Button) findViewById(R.id.postButtonGroup);
        postEditTextMain = (EditText) findViewById(R.id.postEditTextMainGroup);
        tagsEditText = (EditText) findViewById(R.id.tagsEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        membersRecyclerView = (RecyclerView) findViewById(R.id.membersRecyclerView);
        eventsRecyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        editButton = (FloatingActionButton) findViewById(R.id.editButton);
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
        roleButton = (FloatingActionButton) findViewById(R.id.roleButton);
        imageButton = (FloatingActionButton) findViewById(R.id.imageButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    editButton.setImageResource(R.drawable.ic_mode_edit_white_24dp);
                    editGroup();
                } else {
                    editButton.setImageResource(R.drawable.ic_check_white_24dp);
                    saveGroup();
                }
            }
        });

        createEventButton = (FloatingActionButton) findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewEventActivity.class);
                startActivity(intent);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        image = (ImageView) findViewById(R.id.image);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user_id);
        mUser = UserAPI.getUser(userJson, this);

        JsonObject json = new JsonObject();
        json.addProperty("id_group", id);
        json.addProperty("id_user", user_id);
        mGroup = GroupAPI.getGroup(json, getApplicationContext());
        if (mGroup == null)
            return;
        isMember = mGroup.isMember();
        url = mGroup.getGroupURL();
        if (url != null && !url.equals("")){
            Picasso.with(this).load(url).into(image);
        }

        JsonObject tagsJson = new JsonObject();
        tagsJson.addProperty("id", id);
        tags = GroupAPI.getGroupTags(tagsJson, this);
        Iterator iterator = tags.iterator();
        String tagsString = "";
        while (iterator.hasNext())
            tagsString += iterator.next() + " ";
        tagsEditText.setText(tagsString);

        String name = mGroup.getName();
        String description = mGroup.getDescription();
        int id_user = mGroup.getId_user();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(name);

        descriptionEditText.setText(description);

        if (user_id != id_user) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) editButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            editButton.setLayoutParams(p);
            editButton.setVisibility(View.GONE);
            p = (CoordinatorLayout.LayoutParams) deleteButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            deleteButton.setLayoutParams(p);
            deleteButton.setVisibility(View.GONE);
            p = (CoordinatorLayout.LayoutParams) roleButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            roleButton.setLayoutParams(p);
            roleButton.setVisibility(View.GONE);
            p = (CoordinatorLayout.LayoutParams) imageButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            imageButton.setLayoutParams(p);
            imageButton.setVisibility(View.GONE);
        }

        JsonObject membersJson = new JsonObject();
        membersJson.addProperty("id", id);
        ArrayList<User> members = GroupAPI.getGroupMembers(membersJson, this);
        if (members == null)
            members = new ArrayList<>();

        Log.d(TAG, members.toString());
        membersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        membersRecyclerView.setLayoutManager(mLayoutManager);
        RVUserAdapter = new RVUserAdapter(members, this);
        membersRecyclerView.setAdapter(RVUserAdapter);

//        JsonObject eventsJson = new JsonObject();
//        eventsJson.addProperty("id_group", id);
//        json.addProperty("id_user", user_id);
//        ArrayList<Event> events = GroupAPI.getGroupEvents(eventsJson, this);
//        if (events == null)
//            events = new ArrayList<>();
//
//        eventsRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager eLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        eventsRecyclerView.setLayoutManager(eLayoutManager);
//        eventAdapter = new RVEventAdapter(events, this);
//        eventsRecyclerView.setAdapter(eventAdapter);

        enableEditTexts(false);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postButtonGroup();
            }
        });

        JsonObject json2 = new JsonObject();
        json2.addProperty("id", id);
        //json2.addProperty("id_user", user_id);
        //API.getEvent(json2, getApplicationContext());
        ArrayList<PostG> posts = GroupAPI.getAllGroupPosts(json2, getApplicationContext());
//        System.out.println(posts.get(0).getPost());
        Collections.reverse(posts);
        adapterPost = new PostAdapter(this, posts);
        listView.setAdapter(adapterPost);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isMember) {
            getMenuInflater().inflate(R.menu.main_with_leave, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_with_join, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.join:
                joinGroup();
                break;

            case R.id.leave:
                leaveGroup();
                break;

            case R.id.invite:
                Log.i(TAG, "invite");
                invite();
                break;

            case android.R.id.home:
                finish();
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

    public void enableEditTexts(boolean enabled) {
        tagsEditText.setEnabled(enabled);
        descriptionEditText.setEnabled(enabled);
    }

    public void deleteGroup(View v) {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        int result = GroupAPI.deleteGroup(json, getApplicationContext());
        if (result == GroupAPI.ERROR) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        } else if (result == GroupAPI.SUCCESS) {
            Log.i(TAG, "event deleted");
            finish();
        } else if (result == GroupAPI.RESULT_EMPTY) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void editGroup() {
        Toast.makeText(this, "edit", Toast.LENGTH_LONG).show();
        enableEditTexts(true);
    }

    public void saveGroup() {
        Toast.makeText(this, "group updated", Toast.LENGTH_LONG).show();
        enableEditTexts(false);

        if (descriptionEditText.getText() == null || descriptionEditText.getText().equals("")) {
            Toast.makeText(this, "Please don't leave any empty fields", Toast.LENGTH_SHORT).show();
        }

        String name = mGroup.getName();
        String tagsString = "";
        if (tagsEditText.getText() != null) {
            tagsString = tagsEditText.getText().toString().trim();
        }
        String description = descriptionEditText.getText().toString().trim();
        String type = "";
        if (allowedRoles != null) {
            for (int i = 0; i < allowedRoles.size(); i++) {
                type += String.valueOf(allowedRoles.get(i)) + ",";
            }
            type = type.substring(0, type.length() - 1);
        } else {
            type = "0";
        }
        Log.i("type", type);

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("id_admin", user_id);
        json.addProperty("description", description);
        json.addProperty("type", type);
        json.addProperty("group_url", url);

        Log.i(TAG, json.toString());

        int result = GroupAPI.updateGroup(json, this);
        if (result == GroupAPI.ERROR) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        } else if (result == GroupAPI.SUCCESS) {
            Log.i(TAG, "event updated");
        } else if (result == GroupAPI.RESULT_EMPTY) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }

        updateTags(tagsString, this.tags);
    }

    public void joinGroup() {
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_group", id);

        int result = GroupAPI.joinGroup(json, this);
        if (result == GroupAPI.SUCCESS) {
            Toast.makeText(this, "joined group", Toast.LENGTH_SHORT).show();
            RVUserAdapter.add(mUser);
            isMember = true;
            invalidateOptionsMenu();
        } else if (result == GroupAPI.NO_ACCESS) {
            Toast.makeText(this, "You cannot join this group due to your user type.", Toast.LENGTH_SHORT).show();
        }
    }

    public void leaveGroup() {
        JsonObject json = new JsonObject();
        json.addProperty("id_user", user_id);
        json.addProperty("id_group", id);

        int result = GroupAPI.leaveGroup(json, this);
        if (result == GroupAPI.SUCCESS) {
            Toast.makeText(this, "left group", Toast.LENGTH_SHORT).show();
            RVUserAdapter.remove(mUser);
            isMember = false;
            invalidateOptionsMenu();
        } else if (result == GroupAPI.NO_ACCESS) {
            Toast.makeText(this, "You cannot join this event unless you are invited.", Toast.LENGTH_SHORT).show();
        }
    }

    public void invite() {
        Intent intent = new Intent(this, InviteUserActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("isEvent", false);
        startActivity(intent);
    }

    public void setRoles(View v) {
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
                                    allowedRoles.add(which + 1);
                                } else if (allowedRoles.contains(which + 1)) {
                                    // Else, if the item is already in the array, remove it
                                    allowedRoles.remove(Integer.valueOf(which + 1));
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

    public void setImage(View v) {
        Log.d(TAG, "setImage");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    public void updateTags(String tagsString, ArrayList<String> tags) {
        Iterator iterator = tags.iterator();
        while (iterator.hasNext()) {
            String tag = (String) iterator.next();
            JsonObject json = new JsonObject();
            json.addProperty("id_group", id);
            json.addProperty("tag", tag);
            GroupAPI.deleteGroupTag(json, this);
        }
        if (!tagsString.equals("")) {
            String[] tagsArray = tagsString.split(" ");
            for (int i = 0; i < tagsArray.length; i++) {
                if (!tags.contains(tagsArray[i])) {
                    JsonObject json = new JsonObject();
                    json.addProperty("id_group", id);
                    json.addProperty("tag", tagsArray[i]);
                    GroupAPI.addGroupTag(json, this);
                }
            }
        }
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(GroupDetailActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            url = imageResponse.data.link;
            Log.d(TAG, imageResponse.data.link);

            Picasso.with(GroupDetailActivity.this).load(url).into(image);
            saveGroup();
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Toast.makeText(GroupDetailActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GroupDetailActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postButtonGroup() {
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


        long id_group = id;
        long id_user = mUser.getId();
        String content = post;
        String content_url = "dummy";

        JsonObject json = new JsonObject();
        json.addProperty("id_group", id_group);
        json.addProperty("id_user", id_user);
        json.addProperty("post_text", content);
        json.addProperty("post_url", content_url);

        Log.i(TAG, json.toString());

        GroupAPI.createGroupPost(json,this);
        //EventAPI.createEventPost(json, this);

        JsonObject json2 = new JsonObject();
        json2.addProperty("id", id_group);
        //json2.addProperty("id_user", user_id);
        //API.getEvent(json2, this);


        ArrayList<PostG> posts = GroupAPI.getAllGroupPosts(json2, getApplicationContext());

        Log.i(TAG, json2.toString());
        Collections.reverse(posts);
        adapterPost.clear();
        adapterPost.addAll(posts);
        postEditTextMain.setText("");
        //Post p = posts.get(0);
        //System.out.println(p.getPost());

    }

    public class PostAdapter extends ArrayAdapter<PostG> {
        TextView t;
        /*private View.OnClickListener delete = new View.OnClickListener() {
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
                }
            }
        };*/
        /*private View.OnClickListener update = new View.OnClickListener() {
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
        };*/
        /*private View.OnClickListener comment = new View.OnClickListener() {
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
        };*/
        //Button b2;
        public PostAdapter(Context context, List objects) {
            super(context, R.layout.item_post, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);

            PostG mPost = getItem(position);

            TextView postTextView = (TextView) convertView.findViewById(R.id.postEditText);
            Button b = (Button) convertView.findViewById(R.id.deletePost);
            b.setTag(position);
            //b.setOnClickListener(delete);

            Button b2 = (Button) convertView.findViewById(R.id.updatePost);
            b2.setTag(position);
            //b2.setOnClickListener(update);



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
    }

}
