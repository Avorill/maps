package com.example.myapplication;

import static android.content.ContentValues.TAG;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
   // DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity( intent );

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
    //    databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signupProgressBar.setVisibility(View.VISIBLE);
                String email = binding.signupEmail.getText().toString().trim();
                String password = binding.signupPassword.getText().toString().trim();
                String confirmPassword = binding.signupConfirm.getText().toString().trim();
                if(email.equals("") || password.equals("") || confirmPassword.equals("")){
                    Toast.makeText(SignUpActivity.this,"All fields are mandatory",
                            Toast.LENGTH_SHORT).show();
                } else if (password.equals(confirmPassword)) {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    binding.signupProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            Log.w(TAG, "FirebaseAuthUserCollisionException");

                                        } catch (FirebaseNetworkException e) {
                                            Log.w(TAG, "FirebaseNetworkException");
                                        } catch (Exception e) {
                                            Log.w(TAG, "Unknown exception");
                                        }
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(SignUpActivity.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Registration failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }


                            });



//                    Boolean checkUserEmail = databaseHelper.checkEmail(email);
//
//                    if(checkUserEmail == false){
//                        Boolean insert = databaseHelper.insertData(email,password);
//
//                        if(insert == true){
//                            Toast.makeText(SignUpActivity.this,"SignUp successfully",Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            startActivity(intent);
//
//                        } else {
//                            Toast.makeText(SignUpActivity.this,"SignUp Failed",Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(SignUpActivity.this,"User already exists, please login",Toast.LENGTH_SHORT).show();
//                    }
                    
                } else {
                    Toast.makeText(SignUpActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}