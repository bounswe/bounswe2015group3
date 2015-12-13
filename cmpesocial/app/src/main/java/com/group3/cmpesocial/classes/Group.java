package com.group3.cmpesocial.classes;

import com.google.gson.JsonObject;

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
    private int[] type;

    public Group(){

    }

    public Group(JsonObject json){
        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        int id_admin = json.get("id_admin").getAsInt();
        String description = json.get("description").getAsString();
        String group_url = json.get("group_url").getAsString();
        boolean member = json.get("isMember").getAsBoolean();
        String types = json.get("type").getAsString();
        String[] type_array = types.split(",");

        this.id = id;
        this.name = name;
        this.id_user = id_admin;
        this.description = description;
        this.groupURL = group_url;
        this.isMember = member;
        for (int i = 0; i < type_array.length; i ++){
            this.type[i] = Integer.parseInt(type_array[i]);
        }
    }

    public Group(JsonObject json, boolean isMember){
        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        int id_admin = json.get("id_admin").getAsInt();
        String description = json.get("description").getAsString();
        String group_url = json.get("group_url").getAsString();
        String types = json.get("type").getAsString();
        String[] type_array = types.split(",");

        this.id = id;
        this.name = name;
        this.id_user = id_admin;
        this.description = description;
        this.groupURL = group_url;
        this.isMember = isMember;
        for (int i = 0; i < type_array.length; i ++){
            this.type[i] = Integer.parseInt(type_array[i]);
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

    public int[] getType() {
        return type;
    }

    public void setType(int[] type) {
        this.type = type;
    }
}
