package com.group3.cmpesocial.classes;

import com.google.gson.JsonObject;

/**
 * Created by Tuba on 06/11/15.
 */
public class Post {
    private String content;
    private int id_user;
    private int id_event;
    private String content_url;
    private String date;
    private int id;
    //private JsonArray comments;

    public Post(String p) {
        content = p;

    }

    public Post(JsonObject json) {
        //this();
        id = json.get("id").getAsInt();
        content = json.get("content").getAsString();
        content_url = json.get("content_url").getAsString();
        id_user = json.get("id_user").getAsInt();
        date = json.get("date").getAsString();
        id_event = json.get("id_event").getAsInt();
        //comments = json.get("comments").getAsJsonArray();

    }

    public String getContentURL() {
        return content_url;
    }
    public String getPost() {
        return content;
    }
    public int getID() {
        return id;
    }
    public int getEventID() {
        return id_event;
    }
    public int getUserID() {
        return id_user;
    }

}