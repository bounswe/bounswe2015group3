package com.group3.cmpesocial.classes;

import com.google.gson.JsonObject;

/**
 * Created by Tuba on 04/11/15.
 */
public class User {

    private static final int UNDERGRADUATE = 1;
    private static final int GRADUATE = 2;
    private static final int FACULTY_MEMBER = 3;
    private static final int STAFF = 4;
    private static final int ALUMNI = 5;

    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String profilePictureURL;
    private int type;

    public User(){

    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User(JsonObject json){
        id = json.get("id").getAsInt();
        name = json.get("name").getAsString();
        surname = json.get("surname").getAsString();
        email = json.get("email").getAsString();
        password = json.get("password").getAsString();
        //profilePictureURL = json.get("profilePictureURL").getAsString();
        //type = json.get("type").getAsInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
