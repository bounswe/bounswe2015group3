package com.group3.cmpesocial.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.User;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Tuba on 17/12/15.
 */
public class UserAPI {

    public static final int SUCCESS = 1;
    public static final int NOT_ASSIGNED = 0;
    public static final int ERROR = -1;
    public static final int WRONG_PASSWORD = -2;
    public static final int RESULT_EMPTY = -3;

    private static final String TAG = UserAPI.class.getSimpleName();

    public static int login(JsonObject json, final Context context) {
        Log.d(TAG, "login json " + json.toString());

        final int[] returnArray = new int[1];
        Future mFuture = Ion.with(context)
                .load(context.getString(R.string.baseURI) + context.getString(R.string.login))
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
                                String url = trimQuotes(user.get("profile_pic_link").toString());
                                int user_type = Integer.parseInt(user.get("id").toString());

                                SharedPreferences.Editor editor = context.getSharedPreferences("prefsCMPE", context.MODE_PRIVATE).edit();
                                editor.putBoolean("user_exists", true);
                                editor.putInt("user_id", user_id);
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("email", email);
                                editor.putString("url", url);
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.signup))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.updateUser))
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
                .load(context.getString(R.string.baseURI) + context.getString(R.string.getUser))
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

    protected static String trimQuotes(String s) {
        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
    }

}
