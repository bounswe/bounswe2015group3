package com.group3.cmpesocial.classes;

import com.google.gson.JsonObject;

/**
 * Created by Tuba on 06/11/15.
 */
public class Event {

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

    public Event(){
        startDate = new int[3];
        startTime = new int[2];
        endDate = new int[3];
        endTime = new int[2];
    }

    public Event(JsonObject json){
        this();
        int id = json.get("id").getAsInt();
        String name = json.get("name").getAsString();
        String start_date_year_hour = json.get("date").getAsString();
        int periodic = json.get("periodic").getAsInt();
        String end_date_year_hour = json.get("end_date").getAsString();
        int id_user = json.get("id_user").getAsInt();
        String location = json.get("location").getAsString();
        String description = json.get("description").getAsString();

        String start_date = start_date_year_hour.substring(0, start_date_year_hour.indexOf(','));
        String start_month_name = start_date.substring(0, 3).trim();
        String start_day = start_date.substring(4).trim();
        String start_year_hour = start_date_year_hour.substring(start_date_year_hour.indexOf(',') + 1).trim();
        String start_year = start_year_hour.substring(0, start_date_year_hour.indexOf(' ') + 1).trim();
        String start_time = start_year_hour.substring(start_date_year_hour.indexOf(' ') + 1).trim();

        String end_date = end_date_year_hour.substring(0, end_date_year_hour.indexOf(','));
        String end_month_name = end_date.substring(0, 3).trim();
        String end_day = end_date.substring(4).trim();
        String end_year_hour = end_date_year_hour.substring(end_date_year_hour.indexOf(',') + 1).trim();
        String end_year = end_year_hour.substring(0, end_date_year_hour.indexOf(' ') + 1).trim();
        String end_time = end_year_hour.substring(end_date_year_hour.indexOf(' ') + 1).trim();

        this.id = id;
        this.name = name;
        this.startDate[0] = Integer.parseInt(start_day);
        this.startDate[1] = getMonthIndex(start_month_name);
        this.startDate[2] = Integer.parseInt(start_year);
        this.startTime = getTime(start_time);
        this.period = periodic;
        this.endDate[0] = Integer.parseInt(end_day);
        this.endDate[1] = getMonthIndex(end_month_name);
        this.endDate[2] = Integer.parseInt(end_year);
        this.endTime = getTime(end_time);
        this.id_user = id_user;
        this.location = location;
        this.description = description;
    }

    public static int[] getTime(String time){
        String[] splitted = time.split(":");
        int[] mTime = new int[2];
        mTime[0] = Integer.parseInt(splitted[0]);
        mTime[1] = Integer.parseInt(splitted[1]);
        if (time.contains("PM") && mTime[0] != 12){
            mTime[0] += 12;
        }
        return mTime;
    }

    public static int getMonthIndex(String month){
        for (int i = 0; i < monthNames.length; i++){
            if (month.equals(monthNames[i])){
                return i;
            }
        }
        return 0;
    }

    public static String getMonthName(int index){
        if (index > 0 && index < 12){
            return monthNames[index];
        }else{
            return "";
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int[] getEndDate() {
        return endDate;
    }

    public void setEndDate(int[] endDate) {
        this.endDate = endDate;
    }

    public int[] getEndTime() {
        return endTime;
    }

    public void setEndTime(int[] endTime) {
        this.endTime = endTime;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getMonthNames() {
        return monthNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getStartDate() {
        return startDate;
    }

    public void setStartDate(int[] startDate) {
        this.startDate = startDate;
    }

    public int[] getStartTime() {
        return startTime;
    }

    public void setStartTime(int[] startTime) {
        this.startTime = startTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getStartDateString(){
        return startDate[2] + "-" + startDate[1] + "-" + startDate[0];
    }

    public String getStartTimeString(){
        return startTime[0] + ":" + startTime[1] + ":00";
    }

    public String getEndDateString(){
        return endDate[2] + "-" + endDate[1] + "-" + endDate[0];
    }

    public String getEndTimeString(){
        return endTime[0] + ":" + endTime[1] + ":00";
    }

    public String getShowStartDate(){
        return startDate[0] + " " + getMonthName(startDate[1]) + " " + startDate[2];
    }

    public String getShowStartTime(){
        if(startTime[1] < 10){
            return startTime[0] + ":" + startTime[1] + "0";
        } else {
            return startTime[0] + ":" + startTime[1];
        }
    }

    public String getShowEndDate(){
        return endDate[0] + " " + getMonthName(endDate[1]) + " " + endDate[2];
    }

    public String getShowEndTime(){
        if (endTime[1] < 10){
            return endTime[0] + ":" + endTime[1] + "0";
        } else {
            return endTime[0] + ":" + endTime[1];
        }
    }


}
