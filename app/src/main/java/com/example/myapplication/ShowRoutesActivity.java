package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import static android.content.ContentValues.TAG;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//TODO add option to share and delete and rename route
public class ShowRoutesActivity extends AppCompatActivity implements RecycleViewInterface{
    RecyclerView recyclerView;
    ArrayList<Route> routeArrayList;
    FirebaseAuth auth;
     FirebaseFirestore fdb;
     String userId;
    MyAdapter myAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routes_axtivity);


        ProgressBar progressBar= new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);


        fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        routeArrayList = new ArrayList<Route>();
        myAdapter = new MyAdapter(routeArrayList, ShowRoutesActivity.this, this);
        recyclerView.setAdapter(myAdapter);



        recyclerView.setAdapter(myAdapter);

        EventChangeListener();




    }

    private void EventChangeListener() {
        fdb.collection("routes").document(userId).collection("journeys").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e(TAG, error.getMessage());

                            return;
                        }

                        for(DocumentChange change : value.getDocumentChanges()){

                            if(change.getType() == DocumentChange.Type.ADDED){
                                routeArrayList.add(change.getDocument().toObject(Route.class));
                            }

                            myAdapter.notifyDataSetChanged();

                        }



                    }
                });



    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ShowRoutesActivity.this, RouteExtraDetails.class);


        intent.putExtra("NAME",routeArrayList.get(position).getName());
        intent.putExtra("LOCATIONS",routeArrayList.get(position).getLocations());
        intent.putExtra("DISTANCE",routeArrayList.get(position).getDistance());
        intent.putExtra("DURATION", routeArrayList.get(position).getRealDuration(routeArrayList.get(position).getDuration()));
        intent.putExtra("START_DATE",routeArrayList.get(position).realStartDate());

        startActivity(intent);


    }
}
