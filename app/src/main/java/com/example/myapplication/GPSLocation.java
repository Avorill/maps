package com.example.myapplication;

public class GPSLocation {

    double lon;
    double lat;
    double alt;

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    long time;

    public GPSLocation() {
    }

    public GPSLocation(double lat, double lon, long time, double alt) {
        this.lon = lon;
        this.lat = lat;
        this.time = time;
        this.alt = alt;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
