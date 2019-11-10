package com.example.garbagecollection.Company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.garbagecollection.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CompanyHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.compBottomNav);
        NavController navController= Navigation.findNavController(this,R.id.comp_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}
