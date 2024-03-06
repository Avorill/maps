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
import java.util.Objects;


public class RouteExtraDetails extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_extra_details);



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
        Button shareButton, editButton, deleteButton, showMapButton;

        shareButton = findViewById(R.id.btn_share);
        editButton = findViewById(R.id.btn_edit_name);
        deleteButton = findViewById(R.id.btn_delete);
        showMapButton = findViewById(R.id.btn_extra_map);

        nameText.setText(name);
        durationText.setText(duration);
        distanceText.setText(String.valueOf(distance));
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
                            df.update("name", enteredText).addOnSuccessListener(un -> Log.d(TAG, "Change name is successfull")).addOnFailureListener(e ->
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

    }
}