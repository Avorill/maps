package com.example.myapplication;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.concurrent.TimeUnit;

public class Route {

    String name;


    double distance;
    long duration;
    long start_date;

    public long getStartDate() {
        return start_date;
    }

    public void setStartDate(long start_date) {
        this.start_date = start_date;
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

        return sdf.format(start_date);
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

//       long sec = TimeUnit.MILLISECONDS.toSeconds(duration);
//       long min = TimeUnit.SECONDS.toMinutes(sec);
//       long hours = TimeUnit.MINUTES.toHours(min);
//       long days = TimeUnit.HOURS.toDays(hours);
       if(days != 0){
           return days + " days, " + hours + " h, " + min + " min";
       }
       else if (hours != 0){
           return hours + " h,  " + min + " min,  " + sec + " sec";
       } else
           return min + " min,  " + sec + " sec";
    }


    public Route() {
        this.distance = 0;
    }
}
