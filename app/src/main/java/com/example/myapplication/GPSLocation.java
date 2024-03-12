package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;

public class GPSLocation implements Parcelable  {
    double alt;
    double lat;

    double lon;


    long time;


    public static final Creator<GPSLocation> CREATOR = new Creator<GPSLocation>() {
        @Override
        public GPSLocation createFromParcel(Parcel source) {
            double alt = source.readDouble();
            double lat = source.readDouble();
            double lon = source.readDouble();
            long time = source.readLong();
            return new GPSLocation(alt,lat,lon, time);
        }

        @Override
        public GPSLocation[] newArray(int size) {
            return new GPSLocation[size];
        }
    };
    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }



    public GPSLocation() {
    }

    public GPSLocation(double alt, double lat, double lon, long time) {
        this.alt = alt;
        this.lat = lat;
        this.time = time;
        this.lon = lon;
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        Log.v(TAG, "writeToParcel..."+ flags);
        dest.writeDouble(alt);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeLong(time);


    }
}
