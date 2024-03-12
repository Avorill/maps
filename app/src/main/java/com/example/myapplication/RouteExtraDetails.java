package com.example.myapplication;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class RouteExtraDetails extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_extra_details);


        Log.d(TAG, "On create route extra details started");
        String routeId = getIntent().getStringExtra("ID");
        Log.d(TAG, "route id: " + routeId );
        String name = getIntent().getStringExtra("NAME");
        long longDur = getIntent().getLongExtra("LONG_DUR",0);
        long longStart = getIntent().getLongExtra("LONG_START", 0);
        String duration = getIntent().getStringExtra("DURATION");
        String startDate = getIntent().getStringExtra("START_DATE");
        AtomicReference<Boolean> isShared = new AtomicReference<>(getIntent().getBooleanExtra("IS_SHARED", false));
        double distance = Math.round(getIntent().getDoubleExtra("DISTANCE", 0));
        ArrayList<GPSLocation> locations = getIntent().getParcelableArrayListExtra("LOCATIONS");

        TextView nameText = findViewById(R.id.name_id);
        TextView durationText = findViewById(R.id.duration_tv);
        TextView distanceText = findViewById(R.id.distance);
        TextView startDateText = findViewById(R.id.start_date);
        Button shareButton, editButton, deleteButton, showMapButton,hide_button;

        shareButton = findViewById(R.id.btn_share);
        editButton = findViewById(R.id.btn_edit_name);
        deleteButton = findViewById(R.id.btn_delete);
        showMapButton = findViewById(R.id.btn_extra_map);
        hide_button = findViewById(R.id.btn_hide);

        if(isShared.get()){
            shareButton.setVisibility(View.GONE);
            hide_button.setVisibility(View.VISIBLE);
        }
        else {
            shareButton.setVisibility(View.VISIBLE);
            hide_button.setVisibility(View.GONE);
        }

        nameText.setText(name);
        durationText.setText(duration);
        if(distance != 0.0)
            distanceText.setText(String.valueOf(distance));
        else
            distanceText.setText(R.string.no_info);
        startDateText.setText(startDate);

        FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RouteExtraDetails.this, ShowRoutesActivity.class);
                startActivity(intent);
                finish();
            }
        });

//---------------------------------------------------
//        DELETE BUTTON
// --------------------------------------------------
        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RouteExtraDetails.this);

            builder.setMessage(R.string.want_to_delete_this_route)
                    .setPositiveButton("Yes", (dialog, which) -> fdb.collection("routes").document(Objects.requireNonNull(userId)).collection("journeys")
                            .document(Objects.requireNonNull(routeId)).delete()
                            .addOnSuccessListener(unused -> {
                                Log.d(TAG, "Delete from db is successful");
                                Toast.makeText(RouteExtraDetails.this, "Route was deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RouteExtraDetails.this, ShowRoutesActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Exception in deleting route");
                                Toast.makeText(RouteExtraDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

        });
//--------------------------------------------
//        EDIT
//--------------------------------------------
        editButton.setOnClickListener(v -> {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.fragment_edit_route, null);
            builder.setView(dialogView);

            final EditText editText = dialogView.findViewById(R.id.editText);
            editText.setText(name);
            builder.setPositiveButton("OK", (dialog, which) -> {
                String enteredText = editText.getText().toString();
                if(enteredText.equals(name)){
                    return;
                }
                AggregateQuery countQuery = fdb.collection("routes").document(Objects.requireNonNull(userId)).collection("journeys")
                        .whereEqualTo("name", enteredText)
                        .count();
                countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        AggregateQuerySnapshot snapshot = task.getResult();
                        Log.d(TAG, "Count:" + snapshot.getCount());

                        if(snapshot.getCount() == 0){
                            DocumentReference df  = fdb.collection("routes").document(userId).collection("journeys")
                                    .document(Objects.requireNonNull(routeId));
                            df.update("name", enteredText).addOnSuccessListener(un -> Log.d(TAG, "Change name is successful")).addOnFailureListener(e ->
                                    Log.e(TAG, Objects.requireNonNull(e.getMessage())));
                            nameText.setText(enteredText);

                        }
                        else {
                            Log.w(TAG,"This name already exists");
                            Toast.makeText(RouteExtraDetails.this, "You already have trip with this name. Choose another",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Log.w(TAG, "Count failed: ",task.getException());
                    }
                });


            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

        });
//---------------------------------------------------
//        SHOW MAP
//---------------------------------------------------
        showMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(RouteExtraDetails.this, RouteShowOnMapHistory.class);
            intent.putExtra("LOCATIONS", locations);
            intent.putExtra("ACTIVITY", 0);
            intent.putExtra("ID", routeId);
            startActivity(intent);

        }
        );
//---------------------------------------------------
//        SHARE ROUTE
//---------------------------------------------------
        shareButton.setOnClickListener(v -> {
            DocumentReference df  = fdb.collection("routes").document(Objects.requireNonNull(userId)).collection("journeys")
                    .document(Objects.requireNonNull(routeId));
            isShared.set(true);
            df.update("is_shared", true).addOnSuccessListener(un -> {
                shareButton.setVisibility(View.GONE);
                hide_button.setVisibility(View.VISIBLE);
                Map<String, Object> data = new HashMap<>();
                DocumentReference sharedRef = fdb.collection("shared_routes").document(routeId);
                Route route  = new Route(name, distance, longDur, longStart, true, locations);

                data.put("userID", userId);

                sharedRef.set(route)
                        .addOnSuccessListener(unu -> {
                            Log.d(TAG, "Shared route added");
                            sharedRef.update(data);
                            Toast.makeText(RouteExtraDetails.this, "You successfully share route", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                            Toast.makeText(RouteExtraDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        });


            }).addOnFailureListener(e -> {

                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

            });

        });
//---------------------------------------------------
//        HIDE ROUTE
//---------------------------------------------------
        hide_button.setOnClickListener(v ->
        {
            DocumentReference df  = fdb.collection("routes").document(Objects.requireNonNull(userId)).collection("journeys")
                    .document(Objects.requireNonNull(routeId));
            isShared.set(false);
            df.update("is_shared", false).addOnSuccessListener(un -> {
                Log.d(TAG, "You successfully hide you route");
                shareButton.setVisibility(View.VISIBLE);
                hide_button.setVisibility(View.GONE);
                DocumentReference sharedRef = fdb.collection("shared_routes").document(routeId);
                sharedRef.delete().addOnSuccessListener(unu -> {
                            Log.d(TAG, "Route hide successful");
                            Toast.makeText(RouteExtraDetails.this, "You successfully hide route", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                                    Toast.makeText(RouteExtraDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                        );
            }).addOnFailureListener(e -> {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

            });
        });

    }
}