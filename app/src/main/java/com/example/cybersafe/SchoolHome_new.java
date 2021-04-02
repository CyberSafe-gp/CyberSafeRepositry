package com.example.cybersafe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SchoolHome_new extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_school_home_new);
        bottomNavigationView = findViewById(R.id.bottom_nav2);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout2, new SchoolHomeFragment()).commit();

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_Edit2:
                fragment = new EditSchoolFragment();
                break;
            case R.id.navigation_incoming2:
                fragment = new IncomingFragment();
                break;
            case R.id.navigation_home2:
                fragment = new SchoolHomeFragment();
                break;


        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.addFragmentLayout2, fragment).commit();
        return true;
    };
}
