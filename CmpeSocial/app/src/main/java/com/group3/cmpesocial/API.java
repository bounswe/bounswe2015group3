package com.group3.cmpesocial;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Tuba on 21/11/15.
 */
public class API {

    public static final int ERROR = -1;
    public static final int SUCCESS = 0;
    public static final int WRONG_PASSWORD = -2;
    public static final int RESULT_EMPTY = -3;
    private static final String TAG = API.class.getSimpleName();
    private static final String baseURI = "http://54.148.86.208:8080/cmpesocial/api/";

    public static int login(JsonObject json, final Context context) {
        final int[] returnArray = new int[1];
        Ion.with(context)
                .load(baseURI + "login")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i(TAG, "got result");
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
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
        return returnArray[0];
    }

    public static int signup(JsonObject json, final Context context) {
        final int[] returnArray = new int[1];
        Ion.with(context)
                .load(baseURI + "signup")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i(TAG, "got result");
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            returnArray[0] = SUCCESS;
                        } else {
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        return returnArray[0];
    }

    public static int createEvent(JsonObject json, Context context) {
        final int[] returnArray = new int[1];
        Ion.with(context)
                .load(baseURI + "events/create")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i("event", "got result");
                        if (e != null) {
                            Log.i("event", "error " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            returnArray[0] = SUCCESS;
                        } else {
                            Log.i("event", "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        return returnArray[0];
    }

    public static int updateEvent(JsonObject json, Context context){
        final int[] returnArray = new int[1];
        Ion.with(context)
                .load(baseURI + "events/update")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            Log.i(TAG, "result not null");
                            String type = trimQuotes(result.get("Result").toString());
                            Log.i(TAG, "type: " + type);
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else{
                                Log.i(TAG, "type: " + type.toString());
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        return returnArray[0];
    }

    public static int deleteEvent(JsonObject json, Context context){
        final int[] returnArray = new int[1];
        Ion.with(context)
                .load(baseURI + "events/delete")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
                            returnArray[0] = ERROR;
                        } else if (result != null) {
                            Log.i(TAG, "result not null");
                            Log.i(TAG, "" + result.toString());
                            String type = trimQuotes(result.get("Result").toString());
                            Log.i(TAG, "type: " + type);
                            if (type.equals("SUCCESS")) {
                                returnArray[0] = SUCCESS;
                            } else{
                                Log.i(TAG, "type: " + type.toString());
                                returnArray[0] = ERROR;
                            }
                        } else {
                            Log.i(TAG, "result empty");
                            returnArray[0] = RESULT_EMPTY;
                        }
                    }
                });
        return returnArray[0];
    }

    protected static String trimQuotes(String s) {
        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }

}
