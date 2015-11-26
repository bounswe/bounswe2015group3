package com.group3.cmpesocial.classes;

import android.util.Log;

import com.google.gson.JsonObject;

/**
 * Created by Tuba on 06/11/15.
 */
public class Event {

    private int id;
    private String name;
    private String date;
    private String year;
    private String hour;
    private String id_user;
    private String location;
    private String description;

    public Event(){

    }

    public Event(JsonObject json){
        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        String date_year_hour = json.get("date").getAsString();
        String id_user = json.get("id_user").getAsString();
        String location = json.get("location").getAsString();
        String description = json.get("description").getAsString();

        String date = date_year_hour.substring(0, date_year_hour.indexOf(','));
        String year_hour = date_year_hour.substring(date_year_hour.indexOf(',') + 1).trim();
        String year = year_hour.substring(0, date_year_hour.indexOf(' ')+1).trim();
        String hour = year_hour.substring(date_year_hour.indexOf(' ') + 1).trim();

        Log.i("date", date);
        Log.i("year", year);
        Log.i("hour", hour);

        this.id = id;
        this.name = name;
        this.date = date;
        this.year = year;
        this.hour = hour;
        this.id_user = id_user;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
