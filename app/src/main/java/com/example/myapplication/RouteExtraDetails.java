package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.TextView;


import java.util.ArrayList;

public class RouteExtraDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_extra_details);

        String name = getIntent().getStringExtra("NAME");
        String duration = getIntent().getStringExtra("DURATION");
        String startDate = getIntent().getStringExtra("START_DATE");
        double distance = Math.round(getIntent().getDoubleExtra("DISTANCE", 0));

        TextView nameText = findViewById(R.id.name_id);
        TextView durationText = findViewById(R.id.duration_tv);
        TextView distanceText = findViewById(R.id.distance);
        TextView startDateText = findViewById(R.id.start_date);

        nameText.setText(name);
        durationText.setText(duration);
        distanceText.setText(String.valueOf(distance));
        startDateText.setText(startDate);


        ArrayList<GPSLocation> locations = new ArrayList<GPSLocation>();
    }
}