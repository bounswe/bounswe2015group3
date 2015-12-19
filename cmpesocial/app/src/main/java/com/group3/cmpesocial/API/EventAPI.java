package com.group3.cmpesocial.API;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Event;
import com.group3.cmpesocial.classes.Post;
import com.group3.cmpesocial.classes.User;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tuba on 17/12/15.
 */
public class EventAPI {

    public static final int SUCCESS = 1;
    public static final int NOT_ASSIGNED = 0;
    public static final int ERROR = -1;
    public static final int RESULT_EMPTY = -2;
    public static final int NO_ACCESS = -3;


    private static final String TAG = EventAPI.class.getSimpleName();

    public static int createEvent(JsonObject json, Context context, String tags) {
        Log.d(TAG, "createEvent json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.createEvent))
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
            if (!tags.equals("")) {
                JsonObject eventJson = ((JsonObject) mFuture.get()).getAsJsonObject("event");
                int id = eventJson.get("id").getAsInt();
                String[] tagsArray = tags.split(" ");
                for (int i = 0; i < tagsArray.length; i++) {
                    JsonObject tagsJson = new JsonObject();
                    tagsJson.addProperty("id_event", id);
                    tagsJson.addProperty("tag", tagsArray[i]);
                    EventAPI.addEventTag(tagsJson, context);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "exception createEvent" + e.getMessage());
        }
        return returnArray[0];
    }

    public static int updateEvent(JsonObject json, Context context) {
        Log.d(TAG, "updateEvent json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateEvent))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteEvent))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getAllEvents))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getMyEvents))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getJoinedEvents))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getEvent))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getEventParticipants))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.joinEvent))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.leaveEvent))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getAllEventPosts))
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
        } catch (Exception e) {
            Log.i(TAG, "exception getAllEventPosts" + e.getMessage());
        }
        Log.i(TAG, "getAllEventPosts return code: " + returnArray[0]);
        return eventPosts;
    }

    public static int createEventPost(JsonObject json, Context context) {
        Log.d(TAG, "createEventPost json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.createEventPost))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateEventPost))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteEventPost))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.createEventComment))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateEventComment))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteEventComment))
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
        Log.d(TAG, "addEventTag json " + json.toString());
        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.addEventTag))
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
        Log.d(TAG, "deleteEventTag json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.deleteEventTag))
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
        Log.d(TAG, "getEventTags json" + json.toString());
        final int[] returnArray = new int[1];
        ArrayList<String> tags = new ArrayList<>();
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getEventTags))
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
            JsonArray tagsJsonArray = result.getAsJsonArray("tags");
            Log.d(TAG, "tags array " + tagsJsonArray.toString());
            Iterator<JsonElement> iterator = tagsJsonArray.iterator();
            while (iterator.hasNext()) {
                JsonObject tagJsonObject = iterator.next().getAsJsonObject();
                String tag = tagJsonObject.get("tag").getAsString();
                tags.add(tag);
            }
        } catch (Exception e) {
            Log.d(TAG, "exception getEventTags " + e.getMessage());
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
