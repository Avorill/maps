package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;

import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;


import java.io.Serializable;
import java.util.ArrayList;

import java.util.concurrent.atomic.AtomicReference;


public class SharedRouteExtra extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_route_extra);


        Log.d(TAG, "On create route extra details started");
        String routeId = getIntent().getStringExtra("ID");
        Log.d(TAG, "route id: " + routeId );
        String name = getIntent().getStringExtra("NAME");
        String duration = getIntent().getStringExtra("DURATION");
        String startDate = getIntent().getStringExtra("START_DATE");
        double distance = Math.round(getIntent().getDoubleExtra("DISTANCE", 0));
        ArrayList<GPSLocation> locations = getIntent().getParcelableArrayListExtra("LOCATIONS");

        TextView nameText = findViewById(R.id.name_id);
        TextView durationText = findViewById(R.id.duration_tv);
        TextView distanceText = findViewById(R.id.distance);
        TextView startDateText = findViewById(R.id.start_date);
        Button  showMapButton;

        showMapButton = findViewById(R.id.btn_shared_extra_map);

        nameText.setText(name);
        durationText.setText(duration);
        if(distance != 0.0)
            distanceText.setText(String.valueOf(distance));
        else
            distanceText.setText("No Information");
        startDateText.setText(startDate);

        FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SharedRouteExtra.this, ExploreActivity.class);
                startActivity(intent);
                finish();
            }
        });

//---------------------------------------------------
//        SHOW MAP
//---------------------------------------------------
        showMapButton.setOnClickListener(v -> {
                    Intent intent = new Intent(SharedRouteExtra.this, RouteShowOnMapHistory.class);
                    intent.putExtra("LOCATIONS", (Serializable) locations);
                    intent.putExtra("ID", routeId);
                    intent.putExtra("ACTIVITY", 1);
                    startActivity(intent);
                }
        );

    }
}