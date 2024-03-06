package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;

public class GPSLocation implements Parcelable  {

    double lon;
    double lat;
    double alt;
    long time;


    public static final Creator<GPSLocation> CREATOR = new Creator<GPSLocation>() {
        @Override
        public GPSLocation createFromParcel(Parcel source) {
            double lon = source.readDouble();
            double lat = source.readDouble();
            double alt = source.readDouble();
            long time = source.readLong();
            return new GPSLocation(lat,lon,time,alt);
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        Log.v(TAG, "writeToParcel..."+ flags);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeDouble(alt);
        dest.writeLong(time);


    }
}
