package com.group3.cmpesocial.API;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Event;
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
public class SearchAPI {

    public static final int SUCCESS = 1;
    public static final int NOT_ASSIGNED = 0;
    public static final int ERROR = -1;

    private static final String TAG = SearchAPI.class.getSimpleName();

    public static ArrayList<User> searchUsers(JsonObject json, Context context){
        Log.d(TAG, "searchUsers json " + json.toString());

        ArrayList<User> users = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.searchUsers))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error searchUsers" + e.getMessage());
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
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray usersJsonArray = result.getAsJsonArray("Users");
            Iterator<JsonElement> iterator = usersJsonArray.iterator();
            while (iterator.hasNext()) {
                User user = new User(iterator.next().getAsJsonObject());
                users.add(user);
            }
        } catch (Exception e) {
            Log.d(TAG, "searchUsers exception" + e.getMessage());
        }
        return users;
    }

    public static ArrayList<Event> searchEvents(JsonObject json, Context context){
        Log.d(TAG, "searchEvents json " + json.toString());

        ArrayList<Event> events = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.searchEvents))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error searchEvents" + e.getMessage());
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
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray eventsJsonArray = result.getAsJsonArray("Events");
            Iterator<JsonElement> iterator = eventsJsonArray.iterator();
            while (iterator.hasNext()) {
                Event event = new Event(iterator.next().getAsJsonObject(), false);
                events.add(event);
            }
        } catch (Exception e) {
            Log.d(TAG, "searchEvents exception" + e.getMessage());
        }
        return events;
    }

    public static ArrayList<Event> getRecommendedEvents(JsonObject json, Context context){
        Log.d(TAG, "getRecommendedEvents json " + json.toString());

        ArrayList<Event> events = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.recommendedEvents))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getRecommendedEvents" + e.getMessage());
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
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray eventsJsonArray = result.getAsJsonArray("Events");
            Iterator<JsonElement> iterator = eventsJsonArray.iterator();
            while (iterator.hasNext()) {
                Event event = new Event(iterator.next().getAsJsonObject(), false);
                events.add(event);
            }
        } catch (Exception e) {
            Log.d(TAG, "getRecommendedEvents exception" + e.getMessage());
        }
        return events;
    }

    public static ArrayList<Group> getRecommendedGroups(JsonObject json, Context context){
        Log.d(TAG, "getRecommendedGroups json " + json.toString());

        ArrayList<Group> groups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.recommendedGroups))
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getRecommendedGroups" + e.getMessage());
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
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray groupsJsonArray = result.getAsJsonArray("Events");
            Iterator<JsonElement> iterator = groupsJsonArray.iterator();
            while (iterator.hasNext()) {
                Group group = new Group(iterator.next().getAsJsonObject(), false);
                groups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "searchEvents exception" + e.getMessage());
        }
        return groups;
    }

    protected static String trimQuotes(String s) {
        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }

}
