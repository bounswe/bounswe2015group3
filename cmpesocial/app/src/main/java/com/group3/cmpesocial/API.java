package com.group3.cmpesocial;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.Group;
import com.group3.cmpesocial.classes.User;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tuba on 21/11/15.
 */
public class API {

    public static final int ERROR = -1;
    public static final int NOT_ASSIGNED = 0;
    public static final int SUCCESS = 1;
    public static final int WRONG_PASSWORD = -2;
    public static final int RESULT_EMPTY = -3;
    private static final String TAG = API.class.getSimpleName();
    private static final String baseURI = "http://54.148.86.208:8080/cmpesocial-temp/api/";

    public static int login(JsonObject json, final Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "login")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error login " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("type").toString());
                            if (type.equals("SUCCESS")) {
                                JsonObject user = result.getAsJsonObject("user");
                                int user_id = Integer.parseInt(user.get("id").toString());
                                String name = trimQuotes(user.get("name").toString());
                                String surname = trimQuotes(user.get("surname").toString());
                                String password = trimQuotes(user.get("password").toString());
                                String email = trimQuotes(user.get("email").toString());

                                SharedPreferences.Editor editor = context.getSharedPreferences("prefsCMPE", context.MODE_PRIVATE).edit();
                                editor.putBoolean("user_exists", true);
                                editor.putInt("user_id", user_id);
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("email", email);
                                editor.commit();

                                returnArray[0] = SUCCESS;
                            } else if (type.equals("WRONG_PASSWORD")) {
                                returnArray[0] = WRONG_PASSWORD;
                            }

                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception login" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int signup(JsonObject json, final Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "signup")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error signup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            returnArray[0] = SUCCESS;
                        } else {
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception signup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateUser(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "updateUser")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error updateUser" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception updateUser" + e.getMessage());
        }
        return returnArray[0];
    }

    public static User getUser(JsonObject json, Context context){
        User mUser = new User();
        Future mFuture = Ion.with(context)
                .load(baseURI + "getUser")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error getUser" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equals("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonObject userJson = (JsonObject) mFuture.get();
            Log.i(TAG, "future : " + userJson.toString());
            mUser = new User(userJson);
        }catch (Exception e){
            Log.i(TAG, "exception getUser" + e.getMessage());
        }
        return mUser;
    }

    public static int createEvent(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/create")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error createEvent" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            Log.i(TAG, result.toString());
                            returnArray[0] = SUCCESS;
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception createEvent" + e.getMessage());
        }
        return returnArray[0];
    }
    public static int createGroup(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/create")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error createGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            Log.i(TAG, result.toString());
                            returnArray[0] = SUCCESS;
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception createGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateEvent(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/update")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error updateEvent " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception updateEvent" + e.getMessage());
        }
        return returnArray[0];
    }
    public static int updateGroup(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/update")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error updateGroup " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception updateGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteEvent(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/delete")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error deleteEvent" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception deleteEvent" + e.getMessage());
        }
        return returnArray[0];
    }
    public static int deleteGroup(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/delete")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error deleteGroup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception deleteGroup" + e.getMessage());
        }
        return returnArray[0];
    }


    public static int allEvents(final Context context, final ArrayAdapter<Event> adapter) {
        final int[] returnArray = new int[1];
        final ArrayList<Event> eventsList = new ArrayList<>();
        JsonObject json = new JsonObject();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/all")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.i(TAG, "error allEvents" + e.getMessage());
                            returnArray[0] = ERROR;
                            Toast.makeText(context, "an error occurred while updating event list", Toast.LENGTH_SHORT).show();
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                                JsonArray events = result.getAsJsonArray("events");
                                Iterator<JsonElement> iterator = events.iterator();
                                while (iterator.hasNext()) {
                                    JsonObject eventJson = iterator.next().getAsJsonObject();
                                    Log.i(TAG, eventJson.toString());
                                    Event event = new Event(eventJson);
                                    eventsList.add(event);
                                }
                                adapter.addAll(eventsList);
                            } else {
                                returnArray[0] = ERROR;
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = ERROR;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception allEvents" + e.getClass() + " " + e.getMessage());
        }
        return returnArray[0];
    }
    public static int allGroups(final Context context, final ArrayAdapter<Group> adapter) {
        final int[] returnArray = new int[1];


        final ArrayList<Group> groupsList = new ArrayList<>();
        JsonObject json = new JsonObject();
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/all")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.i(TAG, "error allGroups" + e.getMessage());
                            returnArray[0] = ERROR;
                            Toast.makeText(context, "an error occurred while updating group list", Toast.LENGTH_SHORT).show();
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;

                                JsonArray groups = result.getAsJsonArray("groups");
                                Iterator<JsonElement> iterator = groups.iterator();
                                while (iterator.hasNext()) {
                                    JsonObject groupJson = iterator.next().getAsJsonObject();
                                    Log.i(TAG, groupJson.toString());
                                    Group group = new Group(groupJson);
                                    Log.d(TAG, "onCreate() Restoring previous state");
                                    groupsList.add(group);
                                }

                                adapter.addAll(groupsList);
                            } else {
                                returnArray[0] = ERROR;
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = ERROR;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception allGroups" + e.getClass() + " " + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<Event> viewMyEvents(JsonObject json, Context context){
        ArrayList<Event> mEvents = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/view")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error viewMyEvents" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equals("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonObject result = (JsonObject) mFuture.get();
            Log.i(TAG, "future : " + result.toString());
            JsonArray eventsJson = result.getAsJsonArray("events");
            Iterator<JsonElement> iterator = eventsJson.iterator();
            while (iterator.hasNext()) {
                Event event = new Event(iterator.next().getAsJsonObject());
                mEvents.add(event);
            }
        }catch (Exception e){
            Log.i(TAG, "exception viewMyEvents" + e.getMessage());
        }
        return mEvents;
    }
    public static ArrayList<Group> viewMyGroups(JsonObject json, Context context){
        ArrayList<Group> mGroups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/view")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error viewMyGroups" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equals("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonObject result = (JsonObject) mFuture.get();
            Log.i(TAG, "future : " + result.toString());
            JsonArray groupsJson = result.getAsJsonArray("groups");
            Iterator<JsonElement> iterator = groupsJson.iterator();
            while (iterator.hasNext()) {
                Group group = new Group(iterator.next().getAsJsonObject());
                mGroups.add(group);
            }
        }catch (Exception e){
            Log.i(TAG, "exception viewMyEvents" + e.getMessage());
        }
        return mGroups;
    }


    public static Event getEvent(JsonObject json, Context context){
        Event mEvent = new Event();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/viewDetail")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error getEvent" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equals("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonObject eventJson = (JsonObject) mFuture.get();
            Log.i(TAG, "future : " + eventJson.toString());
            mEvent = new Event(eventJson);
        }catch (Exception e){
            Log.i(TAG, "exception getEvent" + e.getMessage());
        }
        return mEvent;
    }
    public static Group getGroup(JsonObject json, Context context){
        Group mGroup = new Group();
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/viewDetail")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error getGroup" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equals("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonObject groupJson = (JsonObject) mFuture.get();
            Log.i(TAG, "future : " + groupJson.toString());
            mGroup = new Group(groupJson);
        }catch (Exception e){
            Log.i(TAG, "exception getGroup" + e.getMessage());
        }
        return mGroup;
    }

    public static ArrayList<User> getEventParticipants(JsonObject json, Context context){
        ArrayList<User> mUsers = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/getParticipants")
                .setJsonObjectBody(json)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error getEventParticipants" + e.getMessage());
                        } else if (result != null) {
//                            String type = trimQuotes(result.get("Result").toString());
//                            if (type.equals("SUCCESS")) {
//                                returnArray[0] = SUCCESS;
//                            } else {
//                                returnArray[0] = ERROR;
//                            }
                        } else {
                            Log.i(TAG, "result empty");
                        }
                    }
                });
        try {
            JsonArray usersJson = (JsonArray) mFuture.get();
            Log.i(TAG, "future : " + usersJson.toString());
            Iterator<JsonElement> iterator = usersJson.iterator();
            while (iterator.hasNext()) {
                User user = new User(iterator.next().getAsJsonObject());
                mUsers.add(user);
            }
        }catch (Exception e){
            Log.i(TAG, "exception getEventParticipants" + e.getMessage());
        }
        return mUsers;
    }

    public static int joinEvent(JsonObject json, Context context){
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/join")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error joinEvent" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else {
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception joinEvent" + e.getMessage());
        }
        return returnArray[0];
    }

    protected static String trimQuotes(String s) {
        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }

}