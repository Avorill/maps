package com.example.myapplication;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class Route {

    String name;



    double distance;
    long duration;
    long startDate;
    boolean shared = false;
    String ID;

    private String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long start_date) {
        this.startDate = start_date;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Route(String name) {
        this.name = name;
        this.distance = 0;
    }


    ArrayList<GPSLocation> locations;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public double getDistance(){
        return distance;
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

    public String realStartDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        return sdf.format(startDate);
    }
    public String getRealDuration(long duration){
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        if(days != 0){
            duration -= TimeUnit.DAYS.toMillis(days);
        }

        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        if(hours != 0){
            duration -= TimeUnit.HOURS.toMillis(hours);
        }

        long min = TimeUnit.MILLISECONDS.toMinutes(duration);
        if(min != 0){
            duration -= TimeUnit.MINUTES.toMillis(min);
        }

        long sec = TimeUnit.MILLISECONDS.toSeconds(duration);


       if(days != 0){
           return days + " days, " + hours + " h, " + min + " min";
       }
       else if (hours != 0){
           return hours + " h,  " + min + " min,  " + sec + " sec";
       } else
           return min + " min,  " + sec + " sec";
    }
    public Route(String name, double distance, long duration, long start_date, boolean shared, ArrayList<GPSLocation> locations) {
        this.name = name;
        this.distance = distance;
        this.duration = duration;
        this.startDate = start_date;
        this.shared = shared;
        this.locations = locations;
    }

    public Route() {
        this.distance = 0;
    }
}
