package com.example.myapplication;
import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApp  extends Application {

    private static MyApp singleton;

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

//    public void renewMyApp(){
//       singleton = this;
//       myLocations = new ArrayList<>();
//    }

    private boolean stopAndSave = false;

    private List<Location> myLocations;
    public MyApp getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    }

}
