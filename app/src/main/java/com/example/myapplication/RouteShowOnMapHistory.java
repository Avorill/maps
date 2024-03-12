package com.example.myapplication;

import android.Manifest;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;


public class RouteShowOnMapHistory extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    List<GPSLocation> savedLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_show_on_map_history);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

//        Button btn_return = findViewById(R.id.btn_return);
        map = findViewById(R.id.map_history);
        map.setTileSource(TileSourceFactory.MAPNIK);
        requestPermissionsIfNecessary(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE });
        String routeId = getIntent().getStringExtra("ID");
        int activity = getIntent().getIntExtra("ACTIVITY", 0);
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {

                Log.d(TAG, "onBackPressed");
                Intent intent;
                if(activity == 0){
                     intent = new Intent(RouteShowOnMapHistory.this, RouteExtraDetails.class);
                } else
                    intent = new Intent(RouteShowOnMapHistory.this,SharedRouteExtra.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("ID", routeId);
                map.getOverlays().clear();
                startActivity(intent);
                finish();

            }
        });
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(1.0);
        map.setMaxZoomLevel(21.0);
        map.setUseDataConnection(false);
        IMapController mapController = map.getController();
        mapController.setZoom(12.5);


        savedLocations = getIntent().getParcelableArrayListExtra("LOCATIONS");
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (GPSLocation location: savedLocations) {
            GeoPoint point = new GeoPoint(location.getLat(), location.getLon());
            geoPoints.add(point);
            Marker marker = new Marker(map);
            marker.setPosition(point);
            mapController.setCenter(point);
            if(savedLocations.indexOf(location) == 0 || savedLocations.indexOf(location) == savedLocations.size() - 1) {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.red, null);
                marker.setIcon(d);
            } else {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.green, null);
                marker.setIcon(d);
            }
            marker.setTitle("Lat: " + location.getLat() + "; Lon: " + location.getLon());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            Log.d(TAG, "get overlays");
            map.getOverlays().add(marker);
        }

        Polyline line = new Polyline();
        line.setPoints(geoPoints);
        Log.d(TAG, "get overlays polyline");
        map.getOverlayManager().add(line);




   }

    @Override
    protected void onResume() {
        Log.d(TAG, " On resume");
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    //---------------------------------------------------------
    //          REQUESTS PERMISSIONS
    //---------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "On Request Permission Result");
        ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        Log.d(TAG, "Request permission");
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
