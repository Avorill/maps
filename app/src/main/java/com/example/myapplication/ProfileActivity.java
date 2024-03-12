package com.example.myapplication;
import static android.content.ContentValues.TAG;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import com.example.myapplication.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ProfileActivity extends AppCompatActivity {




    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {

        if(firebaseAuth.getCurrentUser() == null){
            Log.d(TAG,"Sign out");
            Toast.makeText(ProfileActivity.this, "Sign Out Successfully",
                   Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

    };
    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();


        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            if(auth.getCurrentUser() != null) {
                DocumentReference documentReference = fdb.collection("users").document(userID);
                documentReference.addSnapshotListener(this, (value, error) -> {
                    if(value != null) {
                        binding.profileName.setText(value.getString("nickname"));
                        binding.profileEmail.setText(value.getString("email"));
                    }
                });
            }
        }

        binding.btnLogout.setOnClickListener(v -> {
            auth.addAuthStateListener(authStateListener);
            auth.signOut();

        });

        binding.btnShowRoutes.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ShowRoutesActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AdditionalInfoActivity.class);
            startActivity(intent);
            finish();
        });


    }
}