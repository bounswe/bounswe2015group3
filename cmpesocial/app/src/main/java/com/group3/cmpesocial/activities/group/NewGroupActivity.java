package com.group3.cmpesocial.activities.group;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.GroupAPI;
import com.group3.cmpesocial.API.UserAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.User;
import com.group3.cmpesocial.imgur.helpers.DocumentHelper;
import com.group3.cmpesocial.imgur.imgurmodel.ImageResponse;
import com.group3.cmpesocial.imgur.services.UploadService;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewGroupActivity extends AppCompatActivity {

    private static final String TAG = NewGroupActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText tagsEditText;
    private EditText descriptionEditText;

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private int user_id;
    private User mUser;
    private ArrayList<Integer> allowedRoles;
    private String url = "";
    private File chosenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        user_id = getSharedPreferences("prefsCMPE", MODE_PRIVATE).getInt("user_id", 0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        tagsEditText = (EditText) findViewById(R.id.tagsEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        url = "";
        JsonObject json = new JsonObject();
        json.addProperty("id", user_id);
        mUser = UserAPI.getUser(json, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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

    public void createGroup(View v) {
        if (nameEditText.getText() == null || nameEditText.getText().length() == 0 ||
                descriptionEditText.getText() == null || descriptionEditText.getText().length() == 0) {
            Toast.makeText(this, "Please fill every field", Toast.LENGTH_LONG).show();
            return;
        }

        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String type = "";
        if (allowedRoles != null) {
            if (!allowedRoles.contains(mUser.getType())){
                allowedRoles.add(mUser.getType());
            }
            for (int i = 0; i < allowedRoles.size(); i++) {
                type += String.valueOf(allowedRoles.get(i)) + ",";
            }
            type = type.substring(0, type.length() - 1);
        } else {
            type = "1,2,3,4,5";
        }

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("id_admin", user_id);
        json.addProperty("type", 1);
        json.addProperty("group_url", "www.sample.com");
        json.addProperty("description", description);
        json.addProperty("type", type);

        Log.i(TAG, json.toString());

        String tags = "";
        if (tagsEditText.getText() != null && !tagsEditText.getText().toString().trim().equals("")) {
            tags = tagsEditText.getText().toString().trim();
        }
        int result = GroupAPI.createGroup(json, this, tags);
        if (result == GroupAPI.ERROR) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        } else if (result == GroupAPI.SUCCESS) {
            Log.i(TAG, "group created");
            finish();
        } else if (result == GroupAPI.RESULT_EMPTY) {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
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

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(NewGroupActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            url = imageResponse.data.link;
            Log.d(TAG, imageResponse.data.link);
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Toast.makeText(NewGroupActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NewGroupActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
