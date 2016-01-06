package com.group3.cmpesocial.activities.group;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.GroupAPI;
import com.group3.cmpesocial.API.UserAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.InviteUserActivity;
import com.group3.cmpesocial.activities.event.NewEventActivity;
import com.group3.cmpesocial.adapters.RVEventAdapter;
import com.group3.cmpesocial.adapters.RVUserAdapter;
import com.group3.cmpesocial.classes.Group;
import com.group3.cmpesocial.classes.User;
import com.group3.cmpesocial.imgur.helpers.DocumentHelper;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.services.UploadService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GroupDetailActivity extends AppCompatActivity {

    private static final String TAG = GroupDetailActivity.class.getSimpleName();

    private Group mGroup;
    private User mUser;

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

}
