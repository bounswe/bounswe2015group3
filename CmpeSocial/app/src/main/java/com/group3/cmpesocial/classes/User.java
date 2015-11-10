package com.group3.cmpesocial.classes;

import com.google.gson.JsonObject;

/**
 * Created by Tuba on 04/11/15.
 */
public class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    //private String profilePictureLink;
    //private String role;

    public User(){

    }

    public User(JsonObject json){
        id = json.get("id").getAsInt();
        name = json.get("name").getAsString();
        surname = json.get("surname").getAsString();
        email = json.get("email").getAsString();
        password = json.get("password").getAsString();
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
