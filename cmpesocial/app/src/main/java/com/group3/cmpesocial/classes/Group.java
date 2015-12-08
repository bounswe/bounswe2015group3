package com.group3.cmpesocial.classes;

import com.google.gson.JsonObject;

/**
 * Created by Burak on 06/11/15.
 */
public class Group {

    private int id;
    private String name;
    private int[] startDate;
    private int[] startTime;
    private int period;
    private int[] endDate;
    private int[] endTime;
    private int id_user;
    private String location;
    private String description;

    public static final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final String[] periods = {"None", "Weekly", "Monthly", "Yearly"};

    public Group(){

    }

    public Group(JsonObject json){
        this();
        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();

        int id_admin = json.get("id_admin").getAsInt();
        //String location = json.get("location").getAsString();
        String description = json.get("description").getAsString();
        String group_url = json.get("group_url").getAsString();



        this.id = id;
        this.name = name;

        this.id_user = id_admin;

        this.description = description;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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




}
