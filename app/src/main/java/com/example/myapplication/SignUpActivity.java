package com.example.myapplication;

import static android.content.ContentValues.TAG;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivitySignUpBinding;

import com.google.firebase.FirebaseNetworkException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
   // DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fdb;
    private String userID;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity( intent );
            finish();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fdb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    //    databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(v -> {
            binding.signupProgressBar.setVisibility(View.VISIBLE);
            String nickname = binding.signupNickname.getText().toString().trim();
            String email = binding.signupEmail.getText().toString().trim();
            String password = binding.signupPassword.getText().toString().trim();
            String confirmPassword = binding.signupConfirm.getText().toString().trim();
            if(email.equals("") || password.equals("") || confirmPassword.equals("") ||
                    nickname.equals("")){
                Toast.makeText(SignUpActivity.this,"All fields are mandatory",
                        Toast.LENGTH_SHORT).show();
            } else if (password.equals(confirmPassword)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            binding.signupProgressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(SignUpActivity.this, "Account created",
                                        Toast.LENGTH_SHORT).show();
                                userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                DocumentReference documentReference = fdb.collection("users")
                                        .document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("nickname",nickname);

                                user.put("email",email);
                                user.put("route_count", 0);
                                documentReference.set(user)
                                        .addOnSuccessListener(unused -> Log.d(TAG,"onSuccess: user profile created for " + userID))
                                        .addOnFailureListener(e -> Log.w(TAG, "onFailure: " + e.getMessage()));
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthUserCollisionException e) {

                                    Toast.makeText(SignUpActivity.this, "Email already in use ",
                                            Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "FirebaseAuthUserCollisionException");

                                } catch (FirebaseNetworkException e) {

                                    Toast.makeText(SignUpActivity.this, "Network error",
                                            Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "FirebaseNetworkException");

                                } catch (Exception e) {
                                    Toast.makeText(SignUpActivity.this, "Registration failed.",
                                       Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Unknown exception");
                                }
                                // If sign in fails, display a message to the user.
//                                Log.d(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(SignUpActivity.this, "Registration failed.",
//                                        Toast.LENGTH_SHORT).show();

                            }
                        });




            } else {
                Toast.makeText(SignUpActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
            }
        });

        binding.loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}