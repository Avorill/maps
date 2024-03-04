package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


//TODO make page for all routes in user db. View like recycler list with name? duration and author labels
//TODO add option to share and delete and rename route
public class ShowRoutesAxtivity extends AppCompatActivity {
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
        myAdapter = new MyAdapter(routeArrayList, ShowRoutesAxtivity.this);
        recyclerView.setAdapter(myAdapter);

        routeArrayList = new ArrayList<Route>();

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
                            progressBar.setVisibility(View.GONE);
                            return;
                        }

                        for(DocumentChange change : value.getDocumentChanges()){

                            if(change.getType() == DocumentChange.Type.ADDED){
                                routeArrayList.add(change.getDocument().toObject(Route.class));
                            }

                            myAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }



                    }
                });



    }


}
