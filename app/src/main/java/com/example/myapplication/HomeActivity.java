package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0) {

            super.onCreate(savedInstanceState);
            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            binding.bottomNavigationView.setVisibility(NavigationView.VISIBLE);
            replaceActivity(new MainActivity());
            NavigationView mNavigationView = findViewById(R.id.bottomNavigationView);
            if (mNavigationView != null) {
                mNavigationView.setNavigationItemSelectedListener(this);
            }

        }


    }
    private void replaceActivity (Activity activity){
        Intent intent = new Intent(getApplicationContext(), activity.getClass());
        startActivity(intent);


    }
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.person) {
            replaceActivity(new ProfileActivity());
        } else if (id == R.id.explore) {
            replaceActivity(new MainActivity());
        } else if (id == R.id.route) {
            replaceActivity(new MapsActivity());
        }
        return true;
    }
}