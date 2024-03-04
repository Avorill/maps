package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import static android.content.ContentValues.TAG;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//TODO make page for all routes in user db. View like recycler list with name? duration and author labels
//TODO add option to share and delete and rename route
public class ShowRoutesAxtivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Route> routeArrayList;
    FirebaseAuth auth;
     FirebaseFirestore fdb;
     String userId;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_routes_axtivity);
        fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recycler_view);
        FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fdb.collection("routes").document(userId);
        routeArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(routeArrayList, this);
        recyclerView.setAdapter(myAdapter);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error != null){
//                    Log.w(TAG, "Listen failed on show routes",error);
//                    return;
//
//
//                }
//
//                if(value != null && value.exists()){
//                    Log.d(TAG, "Current data: " + value.getData());
//                    List<String> subcollections = new ArrayList<>();
//
//
//
//
//                    System.out.println(" Hello work");
//
//                } else {
//                    Log.d(TAG, "Current data: null");
//                }
//            }
//        });
        documentReference.addSnapshotListener(this, (value, e) ->
                {

                    if(e != null){
                    Log.w(TAG, "Listen failed on show routes",e);
                    return;
                    }
                    if(value != null && value.exists()) {
                        Log.d(TAG, "Current data: " + value.getData());
                        List<String> subCollections = new ArrayList<>();
                        DocumentReference routeReference = fdb.collection("users").document(userId);
                        AtomicInteger route_count = new AtomicInteger();
                        routeReference.addSnapshotListener(this, (v, err) ->{
                            if (v != null && v.exists())
                                route_count.set(v.get("route_count", Integer.TYPE));
                            }
                        );
                        for(int i = 0; i < route_count.intValue();i++){
                                Route route = new Route();
                                route.setUserId(userId);
                                route.setName(" ");




                        }




                    }
                    else {
                        Log.d(TAG, "Current data: null");
                    }



                });

    }
}