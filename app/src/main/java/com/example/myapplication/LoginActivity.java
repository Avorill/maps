package com.example.myapplication;

import static android.content.ContentValues.TAG;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    //DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
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
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        //databaseHelper = new DatabaseHelper(this);
        MyApp myApp = (MyApp) getApplicationContext();
        binding.loginButton.setOnClickListener(v -> {
            binding.loginProgressBar.setVisibility(View.VISIBLE);

            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if(email.equals("") || password.equals("")){
                Toast.makeText(LoginActivity.this,"All fields are mandatory",
                        Toast.LENGTH_SHORT).show();

            }
            else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            binding.loginProgressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Login successful",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                myApp.onCreate();
                                startActivity( intent );
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        binding.signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class );
            startActivity( intent );
            finish();
        });
    }


}