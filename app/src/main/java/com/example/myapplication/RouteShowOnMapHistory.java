package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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


        Button btn_return = findViewById(R.id.btn_return);
        map = (MapView) findViewById(R.id.map_history);
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE });
        String routeId = getIntent().getStringExtra("ID");
//TODO routeextradetails is closed, but shouldnt
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RouteShowOnMapHistory.this, RouteExtraDetails.class);
                intent.putExtra("ID", routeId);
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
            marker.setTitle("Lat: " + location.getLat() + "; Lon: " + location.getLon());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            map.getOverlays().add(marker);
        }

        Polyline line = new Polyline();
        line.setPoints(geoPoints);
        map.getOverlayManager().add(line);



        btn_return.setOnClickListener(v -> {
            Intent i = new Intent(RouteShowOnMapHistory.this, RouteExtraDetails.class);
            i.putExtra("ID", routeId);
            startActivity(i);
            finish();

        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
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
