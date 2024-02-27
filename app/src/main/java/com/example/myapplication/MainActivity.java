package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final long DEFAULT_UPDATE_INTERVAL = 30;
    public static final long FASTEST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_address, tv_updates, tv_sensor,
    tv_wayPointCounts;
    Button btn_newWayPoint, btn_showWayPointList, btn_showMap, btn_showProfile, btn_homePage;
    Switch sw_locationupdates, sw_gps;
    boolean updateOn= false;
    //current location
    Location currentLocation;
    //list of saved location
    List<Location> savedLocations;
    //Google APIs for location services.
    FusedLocationProviderClient fusedLocationProviderClient;
    //location request is a config file for all settings related to FusedLocation providerClient
    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_address = findViewById(R.id.tv_address);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointList= findViewById(R.id.btn_showWayPointList);
        tv_wayPointCounts = findViewById(R.id.tv_countOfCrumbs);
        btn_showMap = findViewById(R.id.btn_showMap);
        btn_showProfile = findViewById(R.id.btn_showProfile);
        btn_homePage = findViewById(R.id.btn_homePage);

        //set all properties of Locationrequest

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                1000 * DEFAULT_UPDATE_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL* 1000)
                .build();
        //event triggered every time whenever the update
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //save the location
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateUIvalues(location);
                   savedLocations.add(location);
                }

            }
        };
        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the gps location

                //add location to a global list
                MyApp myApp = (MyApp)getApplicationContext();
                savedLocations = myApp.getMyLocations();
                savedLocations.add(currentLocation);
            }
        });


        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ShowSavedLoctionsList.class);
                startActivity(i);
            }
        });
        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
        btn_showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        btn_homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    //most accurate - use GPS
                    locationRequest = new LocationRequest.Builder(locationRequest)
                            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                            .build();
                    tv_sensor.setText("Using GPS Sensors");
                } else {
                    locationRequest = new LocationRequest.Builder(locationRequest)
                            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                            .build();
                    tv_sensor.setText("Using Tower + WIFI");
                }
            }
        });
        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationupdates.isChecked()) {
                    //turn on location tracking
                    startLocationUpdates();
                    updateGPS();
                } else {
                    //turn off
                    stopLocationUpdates();
                }
            }
        });
        updateGPS();
    }

    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location is not being tracked");
        tv_lat.setText("Not Tracking loction");
        tv_lon.setText("Not tracking location");
        tv_speed.setText("Not tracking location");
        tv_address.setText("Not tracking location");
        tv_accuracy.setText("Not tracking location");
        tv_altitude.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                else{
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateGPS() {
    //get permissions from the user to track GPS
    //get the current location from the fused client
    //update the UI
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //we got permissions. Put values of location.XXX into the UI components
                            updateUIvalues(location);
                            currentLocation = location;
                          //  savedLocations.add(currentLocation);

                        }
                    });
        }
        else {
            //permissions not granted yet.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_FINE_LOCATION);
            }
        }
}

    private void updateUIvalues(Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Обновление всех компонентов TextView новыми значениями местоположения
                tv_lat.setText(String.valueOf(location.getLatitude()));
                tv_lon.setText(String.valueOf(location.getLongitude()));
                tv_accuracy.setText(String.valueOf(location.getAccuracy()));

                if(location.hasAltitude()) {
                    tv_altitude.setText(String.valueOf(location.getAltitude()));
                } else {
                    tv_altitude.setText("Not available");
                }
                if(location.hasSpeed()) {
                    tv_speed.setText(String.valueOf(location.getSpeed()));
                } else {
                    tv_speed.setText("Not available");
                }

                Geocoder geocoder = new Geocoder(MainActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    assert addressList != null;
                    tv_address.setText(addressList.get(0).getAddressLine(0));
                } catch (Exception e) {
                    tv_address.setText("Unable to get street address");
                }

                MyApp myApp = (MyApp)getApplicationContext();
                savedLocations = myApp.getMyLocations();

                // Показать количество сохраненных точек маршрута
                tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));
            }
        });
    }


}