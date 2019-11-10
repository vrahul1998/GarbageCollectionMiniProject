package com.example.garbagecollection.Employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.garbagecollection.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployeeHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.empBottomNav);
        NavController navController= Navigation.findNavController(this,R.id.emp_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}
