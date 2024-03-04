package com.example.myapplication;

import java.util.ArrayList;

public class Route {

    String name;



    long duration;
    long start_date;

    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }

    public Route(String name) {
        this.name = name;
    }

    public Route(String name, String userId, long duration, long start_date, ArrayList<GPSLocation> locations) {
        this.name = name;
        this.duration = duration;
        this.start_date = start_date;
        this.locations = locations;
    }

    ArrayList<GPSLocation> locations;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ArrayList<GPSLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<GPSLocation> locations) {
        this.locations = locations;
    }



    public Route() {
    }
}
