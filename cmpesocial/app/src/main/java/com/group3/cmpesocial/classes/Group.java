package com.group3.cmpesocial.classes;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Burak on 06/11/15.
 */
public class Group {

    private int id;
    private String name;
    private int id_user;
    private String description;
    private String groupURL;
    private boolean isMember;
    private String pictureURL;
    private ArrayList<Integer> type;

    public Group() {
        type = new ArrayList<>();
    }

    public Group(JsonObject json) {
        this();

        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        int id_admin = json.get("id_admin").getAsInt();
        String description = json.get("description").getAsString();
        String group_url = json.get("group_url").getAsString();
        boolean member = json.get("isMember").getAsBoolean();
        String types = json.get("type").getAsString();
        Log.i("types", types);
        String[] type_array = type_array = types.split(",");

        this.id = id;
        this.name = name;
        this.id_user = id_admin;
        this.description = description;
        this.groupURL = group_url;
        this.isMember = member;
        if (type_array != null && !types.equals("")) {
            for (int i = 0; i < type_array.length; i++) {
                type.add(Integer.parseInt(type_array[i]));
            }
        } else {
            type.add(0);
        }
    }

    public Group(JsonObject json, boolean isMember) {
        this();

        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        int id_admin = json.get("id_admin").getAsInt();
        String description = json.get("description").getAsString();
        String group_url = json.get("group_url").getAsString();
        String types = json.get("type").getAsString();
        Log.i("types", types);
        String[] type_array = types.split(",");

        this.id = id;
        this.name = name;
        this.id_user = id_admin;
        this.description = description;
        this.groupURL = group_url;
        this.isMember = isMember;
        if (type_array != null && !types.equals("")) {
            for (int i = 0; i < type_array.length; i++) {
                type.add(Integer.parseInt(type_array[i]));
            }
        } else {
            type.add(0);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupURL() {
        return groupURL;
    }

    public void setGroupURL(String groupURL) {
        this.groupURL = groupURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public ArrayList<Integer> getType() {
        return type;
    }

    public void setType(ArrayList<Integer> type) {
        this.type = type;
    }
}
