package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ShowSavedLocationsList extends AppCompatActivity {


    ListView lv_saved_locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_loctions_list);

        lv_saved_locations = findViewById(R.id.lv_wayPoints);
        MyApp myApp = (MyApp)getApplicationContext();
        List<Location> savedLocations = myApp.getMyLocations();

        lv_saved_locations.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                savedLocations));
    }
}