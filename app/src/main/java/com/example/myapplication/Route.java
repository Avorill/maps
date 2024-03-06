package com.example.myapplication;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Route {

    String name;


    double distance;
    long duration;
    long start_date;

    public long getStart_date() {
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

    public double calculateDistance(){
        double calcDistance = 0;

        for(int i = 0 ; i < locations.size() - 1; i++){
            double lat1 = locations.get(i).getLat();
            double lat2 = locations.get(i+1).getLat();

            final int R = 6371;

            double latDistance = Math.toRadians(locations.get(i+1).getLat() - locations.get(i).getLat());
            double lonDistance = Math.toRadians(locations.get(i+1).getLon() - locations.get(i).getLon());
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = R * c * 1000; // convert to meters

            double height = locations.get(i).getAlt() - locations.get(i+1).getAlt();

            dist = Math.pow(dist, 2) + Math.pow(height, 2);
            calcDistance +=dist;

        }
        return calcDistance;
    }


    public String realStartDate(){
        Date date = new Date(start_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String text = sdf.format(start_date);

        return text;
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
           return String.valueOf(days) + " days, " + String.valueOf(hours) + " h, " + String.valueOf(min) + " min";
       }
       else if (hours != 0){
           return  String.valueOf(hours) + " h,  " + String.valueOf(min) + " min,  " + String.valueOf(sec) + " sec";
       } else
           return String.valueOf(min) + " min,  " + String.valueOf(sec) + " sec";
    }


    public Route() {
        this.distance = 0;
    }
}
