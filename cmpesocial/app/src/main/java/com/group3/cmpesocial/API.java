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
import com.group3.cmpesocial.classes.Post;
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
    public static final int NO_ACCESS = -4;
    private static final String TAG = API.class.getSimpleName();
    private static final String baseURI = "http://54.148.86.208:8080/cmpesocial/api/";

    //
    // User API methods
    //

    public static int login(JsonObject json, final Context context) {
        Log.d(TAG, "login json " + json.toString());

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
                            Log.d(TAG, "error login " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("type").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                JsonObject user = result.getAsJsonObject("user");
                                int user_id = Integer.parseInt(user.get("id").toString());
                                String name = trimQuotes(user.get("name").toString());
                                String surname = trimQuotes(user.get("surname").toString());
                                String password = trimQuotes(user.get("password").toString());
                                String email = trimQuotes(user.get("email").toString());
                                int user_type = Integer.parseInt(user.get("id").toString());

                                SharedPreferences.Editor editor = context.getSharedPreferences("prefsCMPE", context.MODE_PRIVATE).edit();
                                editor.putBoolean("user_exists", true);
                                editor.putInt("user_id", user_id);
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("email", email);
                                editor.putInt("type", user_type);
                                editor.apply();

                                returnArray[0] = SUCCESS;
                            } else if (type.equalsIgnoreCase("WRONG_PASSWORD")) {
                                returnArray[0] = WRONG_PASSWORD;
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
            Log.d(TAG, "exception login" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int signup(JsonObject json, final Context context) {
        Log.d(TAG, "signup json " + json.toString());

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
                            Log.d(TAG, "error signup" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            returnArray[0] = SUCCESS;
                        } else {
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        try {
            Log.d(TAG, "future : " + mFuture.get().toString());
        } catch (Exception e) {
            Log.d(TAG, "exception signup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateUser(JsonObject json, Context context) {
        Log.d(TAG, "updateUser json " + json.toString());

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
                            Log.d(TAG, "error updateUser" + e.getMessage());
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
            Log.d(TAG, "exception updateUser" + e.getMessage());
        }
        return returnArray[0];
    }

    public static User getUser(JsonObject json, Context context) {
        Log.d(TAG, "getUser json " + json.toString());

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
                            Log.d(TAG, "error getUser" + e.getMessage());
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
            JsonObject userJson = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + userJson.toString());
            mUser = new User(userJson);
        } catch (Exception e) {
            Log.d(TAG, "exception getUser" + e.getMessage());
        }
        return mUser;
    }

    //
    // Event API methods
    //

    public static int createEvent(JsonObject json, Context context) {
        Log.d(TAG, "createEvent json " + json.toString());

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
                            Log.d(TAG, "error createEvent" + e.getMessage());
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
        } catch (Exception e) {
            Log.d(TAG, "exception createEvent" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateEvent(JsonObject json, Context context) {
        Log.d(TAG, "updateEvent json " + json.toString());

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
                            Log.d(TAG, "error updateEvent " + e.getMessage());
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
            Log.d(TAG, "exception updateEvent" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteEvent(JsonObject json, Context context) {
        Log.d(TAG, "deleteEvent json " + json.toString());

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
                            Log.d(TAG, "error deleteEvent" + e.getMessage());
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
            Log.d(TAG, "exception deleteEvent" + e.getMessage());
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
                            Log.d(TAG, "error allEvents" + e.getMessage());
                            returnArray[0] = ERROR;
                            Toast.makeText(context, "an error occurred while updating event list", Toast.LENGTH_SHORT).show();
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equalsIgnoreCase("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                                JsonArray events = result.getAsJsonArray("events");
                                Iterator<JsonElement> iterator = events.iterator();
                                while (iterator.hasNext()) {
                                    JsonObject eventJson = iterator.next().getAsJsonObject();
                                    Log.d(TAG, eventJson.toString());
                                    Event event = new Event(eventJson);
                                    eventsList.add(event);
                                }
                                adapter.addAll(eventsList);
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
            Log.d(TAG, "exception allEvents" + e.getClass() + " " + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<Event> getMyEvents(JsonObject json, Context context) {
        Log.d(TAG, "getMyEvents json " + json.toString());

        ArrayList<Event> mEvents = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/viewByOwned")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error viewMyEvents" + e.getMessage());
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
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray eventsJson = result.getAsJsonArray("events");
            Iterator<JsonElement> iterator = eventsJson.iterator();
            while (iterator.hasNext()) {
                Event event = new Event(iterator.next().getAsJsonObject());
                mEvents.add(event);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception viewMyEvents" + e.getMessage());
        }
        return mEvents;
    }

    public static ArrayList<Event> getJoinedEvents(JsonObject json, Context context) {
        Log.d(TAG, "getJoinedEvents json " + json.toString());

        ArrayList<Event> mEvents = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/viewJoined")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getJoinedEvents" + e.getMessage());
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
                Event event = new Event(iterator.next().getAsJsonObject());
                mEvents.add(event);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getJoinedEvents" + e.getMessage());
        }
        return mEvents;
    }


    public static Event getEvent(JsonObject json, Context context) {
        Log.d(TAG, "getEvent json " + json.toString());

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
                            Log.d(TAG, "error getEvent " + e.getMessage());
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
            JsonObject eventJson = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + eventJson.toString());
            mEvent = new Event(eventJson);
        } catch (Exception e) {
            Log.d(TAG, "exception getEvent" + e.getMessage());
        }
        return mEvent;
    }

    public static ArrayList<User> getEventParticipants(JsonObject json, Context context) {
        Log.d(TAG, "getEventParticipants json " + json.toString());

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
                            Log.d(TAG, "error getEventParticipants" + e.getMessage());
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
            JsonArray usersJson = (JsonArray) mFuture.get();
            Log.d(TAG, "future : " + usersJson.toString());
            Iterator<JsonElement> iterator = usersJson.iterator();
            while (iterator.hasNext()) {
                User user = new User(iterator.next().getAsJsonObject());
                mUsers.add(user);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getEventParticipants" + e.getMessage());
        }
        return mUsers;
    }

    public static int joinEvent(JsonObject json, Context context) {
        Log.d(TAG, "joinEvent json " + json.toString());

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
                            Log.d(TAG, "error joinEvent" + e.getMessage());
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
            Log.d(TAG, "exception joinEvent" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int leaveEvent(JsonObject json, Context context) {
        Log.d(TAG, "leaveEvent json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/leave")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error leaveEvent" + e.getMessage());
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
            Log.d(TAG, "exception leaveEvent" + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<Post> getAllEventPosts(JsonObject json, Context context) {
        ArrayList<Post> eventPosts = new ArrayList<>();
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/getAllPosts")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error getAllEventPosts" + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            String type = trimQuotes(result.get("Result").toString());
                            if (type.equals("SUCCESS")) {
                                // get posts
                                // add them to eventPosts
                                // posts also have their comments in them
                                //JsonArray comments = result.getAsJsonArray("comments");
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
            JsonObject postsObject = (JsonObject) mFuture.get();
            Log.i(TAG, postsObject.toString());
            JsonArray posts = postsObject.getAsJsonArray("posts");
            Log.i(TAG, posts.toString());
            Iterator<JsonElement> iterator = posts.iterator();
            while (iterator.hasNext()) {
                Post post = new Post(iterator.next().getAsJsonObject());
                eventPosts.add(post);
            }



            Log.i(TAG, "future : " + mFuture.get().toString());
        }catch (Exception e){
            Log.i(TAG, "exception getAllEventPosts" + e.getMessage());
        }
        Log.i(TAG, "getAllEventPosts return code: " + returnArray[0]);
        return eventPosts;
    }

    public static int createEventPost(JsonObject json, Context context) {
        Log.d(TAG, "createEventPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/createPost/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error createEventPost" + e.getMessage());
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
            Log.d(TAG, "exception createEventPost" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateEventPost(JsonObject json, Context context) {
        Log.d(TAG, "updateEventPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/updatePost/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error updateEventPost" + e.getMessage());
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
            Log.d(TAG, "exception updateEventPost" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteEventPost(JsonObject json, Context context) {
        Log.d(TAG, "deleteEventPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/deletePost/")
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
        } catch (Exception e) {
            Log.d(TAG, "exception deleteEventPost" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int createEventComment(JsonObject json, Context context) {
        Log.d(TAG, "createEventComment json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/createComment/")
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
        } catch (Exception e) {
            Log.d(TAG, "exception createEventComment" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateEventComment(JsonObject json, Context context) {
        Log.d(TAG, "updateEventComment json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/updateComment/")
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
        } catch (Exception e) {
            Log.d(TAG, "exception updateEventComment" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteEventComment(JsonObject json, Context context) {
        Log.d(TAG, "deleteEventComment json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/deleteComment/")
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
        } catch (Exception e) {
            Log.d(TAG, "exception deleteEventComment" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int addEventTag(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/tag/add")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error addEventTag" + e.getMessage());
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
            Log.d(TAG, "exception addEventTag" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int deleteEventTag(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/tag/delete")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error deleteEventTag" + e.getMessage());
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
            Log.d(TAG, "exception deleteEventTag" + e.getMessage());
        }
        return returnArray[0];
    }

    public static ArrayList<String> getEventTags(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        ArrayList<String> tags = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "events/tag/getTags")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error getEventTags" + e.getMessage());
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
            JsonArray usersJsonArray = result.getAsJsonArray("Users");
            Iterator<JsonElement> iterator = usersJsonArray.iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next().getAsString();
                tags.add(tag);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getEventTags" + e.getMessage());
        }
        return tags;
    }


    //
    // Group API Methods
    //

    public static int createGroup(JsonObject json, Context context) {
        Log.d(TAG, "createGroup json " + json.toString());

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
        } catch (Exception e) {
            Log.d(TAG, "exception createGroup" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateGroup(JsonObject json, Context context) {
        Log.d(TAG, "updateGroup json " + json.toString());

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
                .load(baseURI + "groups/delete")
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
                .load(baseURI + "groups/all")
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

    public static ArrayList<Group> viewMyGroups(JsonObject json, Context context) {
        Log.d(TAG, "viewMyGroups json " + json.toString());

        ArrayList<Group> mGroups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/viewOwned")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error viewMyGroups" + e.getMessage());
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
            JsonObject result = (JsonObject) mFuture.get();
            Log.d(TAG, "future : " + result.toString());
            JsonArray groupsJson = result.getAsJsonArray("groups");
            Iterator<JsonElement> iterator = groupsJson.iterator();
            while (iterator.hasNext()) {
                Group group = new Group(iterator.next().getAsJsonObject());
                mGroups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception viewMyGroups" + e.getMessage());
        }
        return mGroups;
    }

    public static ArrayList<Group> viewJoinedGroups(JsonObject json, Context context) {
        Log.d(TAG, "viewJoinedGroups json " + json.toString());

        ArrayList<Group> mGroups = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/viewWithMembership")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.d(TAG, "error viewJoinedGroups" + e.getMessage());
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
                Group group = new Group(iterator.next().getAsJsonObject());
                mGroups.add(group);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception viewJoinedGroups" + e.getMessage());
        }
        return mGroups;
    }

    public static Group getGroup(JsonObject json, Context context) {
        Log.d(TAG, "getGroup json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/view")
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
                .load(baseURI + "groups/join")
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
                .load(baseURI + "groups/leave")
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
                .load(baseURI + "groups/invite")
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

    public static ArrayList<Object> getAllGroupPosts(JsonObject json, Context context) {
        Log.d(TAG, "getAllGroupPosts json " + json.toString());

        ArrayList<Object> eventPosts = new ArrayList<>();
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(baseURI + "groups/getPosts")
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
                .load(baseURI + "groups/createPost")
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
                .load(baseURI + "groups/updatePost")
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
                .load(baseURI + "events/deletePost/")
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
                .load(baseURI + "groups/createComment")
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
                .load(baseURI + "groups/updateComment")
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
                .load(baseURI + "groups/deleteComment")
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
                .load(baseURI + "groups/getMembers")
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
                .load(baseURI + "groups/tag/add")
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
                .load(baseURI + "groups/tag/delete")
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
                .load(baseURI + "groups/tag/getTags")
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
            JsonArray usersJsonArray = result.getAsJsonArray("Users");
            Iterator<JsonElement> iterator = usersJsonArray.iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next().getAsString();
                tags.add(tag);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getGroupTags" + e.getMessage());
        }
        return tags;
    }

//
//    public static ArrayList<Event> getGroupEvents(JsonObject json, Context context){
//        Log.d(TAG, "getGroupEvents json " + json.toString());
//
//        return null;
//    }

    //
    // Search API
    //

    public static ArrayList<User> searchUsers(JsonObject json, Context context){
        Log.d(TAG, "searchUsers json " + json.toString());

        ArrayList<User> users = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(baseURI + "/search/users")
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
                .load(baseURI + "/search/events")
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
                Event event = new Event(iterator.next().getAsJsonObject());
                events.add(event);
            }
        } catch (Exception e) {
            Log.d(TAG, "searchEvents exception" + e.getMessage());
        }
        return events;
    }


    protected static String trimQuotes(String s) {
        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }

}
