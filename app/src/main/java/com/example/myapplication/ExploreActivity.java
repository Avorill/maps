package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

//TODO make page for shared_routes with posibilities to show on map, add comment, see author nickname
public class ExploreActivity extends AppCompatActivity implements RecycleViewInterface {

    RecyclerView recyclerView;
    ArrayList<Pair<String, Route>> routeArrayList;
    FirebaseAuth auth;
    FirebaseFirestore fdb;
    String userId;
    MyAdapter myAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);


        fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recycler_view_for_shared);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        routeArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(routeArrayList, ExploreActivity.this, this);
        recyclerView.setAdapter(myAdapter);


        EventChangeListener();


    }

    private void EventChangeListener() {
        fdb.collection("shared_routes")
                .orderBy("startDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());

                        return;
                    }

                    for (DocumentChange change : value.getDocumentChanges()) {

                        if (change.getType() == DocumentChange.Type.ADDED) {
                            routeArrayList.add(new Pair<>(change.getDocument().getId(), change.getDocument().toObject(Route.class)));
                            Log.i("Long", String.valueOf(routeArrayList.get(routeArrayList.size()-1).second.locations.get(0).lon));
                            Log.i("Alt", String.valueOf(routeArrayList.get(routeArrayList.size()-1).second.locations.get(0).alt));
                            Log.i("Lat", String.valueOf(routeArrayList.get(routeArrayList.size()-1).second.locations.get(0).lat));
                            Log.i("Time", String.valueOf(routeArrayList.get(routeArrayList.size()-1).second.locations.get(0).time));
                        }

                        myAdapter.notifyDataSetChanged();

                    }


                });
        Log.d(TAG, "envent");

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ExploreActivity.this, SharedRouteExtra.class);

        intent.putExtra("ID", routeArrayList.get(position).first);
        intent.putExtra("NAME", routeArrayList.get(position).second.getName());
        intent.putExtra("LOCATIONS", routeArrayList.get(position).second.getLocations());
        intent.putExtra("DISTANCE", routeArrayList.get(position).second.getDistance());
        intent.putExtra("DURATION", routeArrayList.get(position).second.getRealDuration(routeArrayList.get(position).second.getDuration()));
        intent.putExtra("START_DATE", routeArrayList.get(position).second.realStartDate());
        startActivity(intent);
        finish();

    }
}