package com.group3.cmpesocial.classes;

/**
 * Created by Tuba on 09/11/15.
 */
public class Recommendation {

    public static final int TYPE_EVENT = 0;
    public static final int TYPE_GROUP = 1;

    private int type;
    private String title;

    public Recommendation(int type, String title) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
