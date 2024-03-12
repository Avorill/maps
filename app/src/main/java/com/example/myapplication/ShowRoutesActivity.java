package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import static android.content.ContentValues.TAG;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.Objects;


public class ShowRoutesActivity extends AppCompatActivity implements RecycleViewInterface{
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
        setContentView(R.layout.activity_show_routes_axtivity);


        progressBar= new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);


        fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        routeArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(routeArrayList, ShowRoutesActivity.this, this);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();




    }

    private void EventChangeListener() {
        fdb.collection("routes").document(userId).collection("journeys").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        Log.e(TAG, Objects.requireNonNull(error.getMessage()));

                        return;
                    }

                    for(DocumentChange change : Objects.requireNonNull(value).getDocumentChanges()){

                        if(change.getType() == DocumentChange.Type.ADDED){
                            routeArrayList.add(new Pair<>(change .getDocument().getId(), change.getDocument().toObject(Route.class)));
                        }
                        if(change.getDocument().get("is_shared") != null) {
                            routeArrayList.get(routeArrayList.size() - 1).second.setShared((Boolean) change.getDocument().get("is_shared"));

                        } else {
                            routeArrayList.get(routeArrayList.size()-1).second.setShared(false);

                        }
                        myAdapter.notifyDataSetChanged();

                    }



                });



    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ShowRoutesActivity.this, RouteExtraDetails.class);

        intent.putExtra("ID", routeArrayList.get(position).first);
        intent.putExtra("NAME",routeArrayList.get(position).second.getName());
        intent.putExtra("LOCATIONS",routeArrayList.get(position).second.getLocations());
        intent.putExtra("DISTANCE",routeArrayList.get(position).second.getDistance());
        intent.putExtra("DURATION", routeArrayList.get(position).second.getRealDuration(routeArrayList.get(position).second.getDuration()));
        intent.putExtra("LONG_DUR", routeArrayList.get(position).second.getDuration());
        intent.putExtra("LONG_START", routeArrayList.get(position).second.getStartDate());
        intent.putExtra("START_DATE",routeArrayList.get(position).second.realStartDate());
        intent.putExtra("IS_SHARED", routeArrayList.get(position).second.isShared());
        startActivity(intent);
        finish();


    }
}
