package com.example.g2_nirav_sridheepan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.g2_nirav_sridheepan.databinding.ActivityTaskersBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TaskersActivity extends AppCompatActivity {
    private ActivityTaskersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setup the bottom navigation menu
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = binding.bottomNavView;
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}