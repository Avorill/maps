package com.example.myapplication;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {




    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            if(firebaseAuth.getCurrentUser() == null){
                Log.d(TAG,"Sign out");
                Toast.makeText(ProfileActivity.this, "Sign Out Successfully",
                       Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
//            } else {
//                Log.w(TAG,"Sign out failed");
//                Toast.makeText(ProfileActivity.this, "Sign Out Failed",
//                        Toast.LENGTH_SHORT).show();
            }

        }
    };
    private FirebaseFirestore fdb;
    private String userID;
    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        MyApp myApp = (MyApp) getApplicationContext();


        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if(auth.getCurrentUser() != null) {
                DocumentReference documentReference = fdb.collection("users").document(userID);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null) {
                            binding.profileName.setText(value.getString("nickname"));
                            binding.profileEmail.setText(value.getString("email"));
                        }
                    }
                });
            }
        }

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.addAuthStateListener(authStateListener);
                auth.signOut();

            }
        });


    }
}