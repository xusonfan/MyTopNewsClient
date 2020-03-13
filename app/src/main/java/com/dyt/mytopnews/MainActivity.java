package com.dyt.mytopnews;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController mController = Navigation.findNavController(this, R.id.nav_host);
        AppBarConfiguration mConfiguration = new AppBarConfiguration.Builder(mBottomNavigationView.getMenu()).build();
//        NavigationUI.setupActionBarWithNavController(this,mController,mConfiguration);
        NavigationUI.setupWithNavController(mBottomNavigationView, mController);
    }


}
