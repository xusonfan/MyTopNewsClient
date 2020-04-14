package com.dyt.mytopnews;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
        //设置透明状态栏
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.color));

        setContentView(R.layout.activity_main);
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController mController = Navigation.findNavController(this, R.id.nav_host);
        AppBarConfiguration mConfiguration = new AppBarConfiguration.Builder(mBottomNavigationView.getMenu()).build();
//        NavigationUI.setupActionBarWithNavController(this,mController,mConfiguration);
        NavigationUI.setupWithNavController(mBottomNavigationView, mController);
    }


}
