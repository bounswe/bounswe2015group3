package com.group3.cmpesocial.API;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Group;
import com.group3.cmpesocial.classes.User;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tuba on 17/12/15.
 */
public class GroupAPI {

    public static final int SUCCESS = 1;
    public static final int NOT_ASSIGNED = 0;
    public static final int ERROR = -1;
    public static final int RESULT_EMPTY = -2;
    public static final int NO_ACCESS = -3;

    private static final String TAG = GroupAPI.class.getSimpleName();

    public static int createGroup(JsonObject json, Context context, String tags) {
        Log.d(TAG, "createGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.createGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error createGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            Log.d(TAG, result.toString());
                            returnArray[0] = SUCCESS;
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
            if (!tags.equals("")) {
                JsonObject groupJson = ((JsonObject) mFuture.get()).getAsJsonObject("group");
                int id = groupJson.get("id").getAsInt();
                String[] tagsArray = tags.split(" ");
                for (int i = 0; i < tagsArray.length; i++) {
                    JsonObject tagsJson = new JsonObject();
                    tagsJson.addProperty("id_event", id);
                    tagsJson.addProperty("tag", tagsArray[i]);
                    GroupAPI.addGroupTag(tagsJson, context);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "exception createGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateGroup(JsonObject json, Context context) {
        Log.d(TAG, "updateGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error updateGroup " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception updateGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteGroup(JsonObject json, Context context) {
        Log.d(TAG, "deleteGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error deleteGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception deleteGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int allGroups(final Context context, final ArrayAdapter<Group> adapter) {
        final int[] returnArray = new int[1];
        final ArrayList<Group> groupsList = new ArrayList<>();
        JsonObject json = new JsonObject();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getAllGroups))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.d(TAG, "error allGroups" + e.getMessage());
                            returnArray[0] = ERROR;
                            Toast.makeText(context, "an error occurred while updating group list", Toast.LENGTH_SHORT).show();
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            Log.i(TAG, result.toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                                JsonArray groups = result.getAsJsonArray("groups");
                                Log.i(TAG, "here");
                                Iterator<JsonElement> iterator = groups.iterator();
                                while (iterator.hasNext()) {
                                    Log.i(TAG, "in");
                                    JsonObject groupJson = iterator.next().getAsJsonObject();
                                    Log.d(TAG, groupJson.toString());
                                    Group group = new Group(groupJson);
                                    groupsList.add(group);
                                }

                                adapter.addAll(groupsList);
                            } else {
                                returnArray[0] = ERROR;
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = ERROR;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception allGroups" + e.getClass() + " " + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<Group> getMyGroups(JsonObject json, Context context) {
        Log.d(TAG, "getMyGroups json " + json.toString());

        ArrayList<Group> mGroups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getMyGroups))
                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getMyGroups" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equalsIgnoreCase("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.d(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonArray result = (JsonArray) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            Iterator<JsonElement> iterator = result.iterator();
            while (iterator.hasNext()) {
                Group group = new Group(iterator.next().getAsJsonObject(), true);
                mGroups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getMyGroups" + e.getMessage());
        }
        return mGroups;
    }

    public static ArrayList<Group> getJoinedGroups(JsonObject json, Context context) {
        Log.d(TAG, "getJoinedGroups json " + json.toString());

        ArrayList<Group> mGroups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getJoinedGroups))
                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getJoinedGroups" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equalsIgnoreCase("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.d(TAG, "result empty");
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
            JsonArray groupsJson = (JsonArray) mFuture.get();
            Iterator<JsonElement> iterator = groupsJson.iterator();
            while (iterator.hasNext()) {
                Group group = new Group(iterator.next().getAsJsonObject(), true);
                mGroups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getJoinedGroups" + e.getMessage());
        }
        return mGroups;
    }

    public static Group getGroup(JsonObject json, Context context) {
        Log.d(TAG, "getGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getGroup" + e.getMessage());
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else if (type.equalsIgnoreCase("Failure")) {
                                returnArray[0] = NO_ACCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                        }
                    }
                });

        try {
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            if (returnArray[0] == NO_ACCESS) {
                Log.d(TAG, "group not visible to the user because of his role");
                Toast.makeText(context, "You cannot view this group.", Toast.LENGTH_SHORT).show();
                return null;
            } else if (returnArray[0] != SUCCESS){
                Log.d(TAG, returnArray[0] + "getGroup unknown error");
                return null;
            } else {
                JsonObject groupJson = result.getAsJsonObject("group");
                boolean isMember = result.get("isMember").getAsBoolean();
                Log.i("groupjson", groupJson.toString());
                Group mGroup = new Group(groupJson, isMember);
                Log.i("here1", "here1");
                return mGroup;
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getGroup" + e.getMessage());
        }
        return new Group();
    }

    public static int joinGroup(JsonObject json, Context context) {
        Log.d(TAG, "joinGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.joinGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error joinGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else if (type.equalsIgnoreCase("failure")) {
                                returnArray[0] = NO_ACCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception joinGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int leaveGroup(JsonObject json, Context context) {
        Log.d(TAG, "leaveGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.leaveGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error leaveGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception leaveGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int inviteToGroup(JsonObject json, Context context) {
        Log.d(TAG, "inviteToGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.inviteToGroup))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error inviteToGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception inviteToGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<Group> getInvitedGroups(JsonObject json, Context context) {
        Log.d(TAG, "getInvitedGroups json " + json.toString());

        ArrayList<Group> mGroups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.viewInvitedGroups))
                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getInvitedGroups" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equalsIgnoreCase("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.d(TAG, "result empty");
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
            JsonArray groupsJson = (JsonArray) mFuture.get();
            Iterator<JsonElement> iterator = groupsJson.iterator();
            while (iterator.hasNext()) {
                Group group = new Group(iterator.next().getAsJsonObject(), true);
                mGroups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getInvitedGroups" + e.getMessage());
        }
        return mGroups;
    }


    public static ArrayList<Object> getAllGroupPosts(JsonObject json, Context context) {
        Log.d(TAG, "getAllGroupPosts json " + json.toString());

        ArrayList<Object> eventPosts = new ArrayList<>();
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getGroupPosts))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getAllGroupPosts" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                // get posts
                                // add them to eventPosts
                                // posts also have their comments in them
                                //JsonArray comments = result.getAsJsonArray("comments");
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception getAllGroupPosts" + e.getMessage());
        }
        Log.d(TAG, "getAllGroupPosts return code: " + returnArray[0]);
        return eventPosts;
    }

    public static int createGroupPost(JsonObject json, Context context) {
        Log.d(TAG, "createGroupPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.createGroupPost))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error createGroupPost" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception createGroupPost" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateGroupPost(JsonObject json, Context context) {
        Log.d(TAG, "updateGroupPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateGroupPost))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error updateGroupPost" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception updateGroupPost" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteGroupPost(JsonObject json, Context context){
        Log.d(TAG, "deleteGroupPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteGroupPost))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error deleteEventPost" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.d(TAG, "exception deleteEventPost" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int createGroupComment(JsonObject json, Context context){
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.createGroupComment))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error createEventComment" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.d(TAG, "exception createEventComment" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateGroupComment(JsonObject json, Context context){
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateGroupComment))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error updateEventComment" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.d(TAG, "exception updateEventComment" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteGroupComment(JsonObject json, Context context){
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteGroupComment))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error deleteEventComment" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.d(TAG, "exception deleteEventComment" + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<User> getGroupMembers(JsonObject json, Context context) {
        Log.d(TAG, "getGroupMembers json " + json.toString());

        ArrayList<User> groupsMembers = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getGroupMembers))
                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getGroupMembers" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equalsIgnoreCase("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else if (type.equalsIgnoreCase("Failure")) {
//                                returnArray[0] = NO_ACCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.d(TAG, "result empty");
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
            JsonArray result = (JsonArray) mFuture.get();
            Iterator<JsonElement> iterator = result.iterator();
            while (iterator.hasNext()) {
                User user = new User(iterator.next().getAsJsonObject());
                groupsMembers.add(user);
            }
        } catch (Exception e) {
            Log.d(TAG, "getGroupMembers exception" + e.getMessage());
        }
        return groupsMembers;
    }

    public static int addGroupTag(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.addGroupTag))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error addGroupTag" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception addGroupTag" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteGroupTag(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteGroupTag))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error deleteGroupTag" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception deleteGroupTag" + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<String> getGroupTags(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        ArrayList<String> tags = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getGroupTags))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getGroupTags" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.d(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray tagsJsonArray = result.getAsJsonArray("tags");
            Iterator<JsonElement> iterator = tagsJsonArray.iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next().getAsString();
                tags.add(tag);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getGroupTags" + e.getMessage());
        }
        return tags;
    }

    protected static String trimQuotes(String s) {
        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }

}
