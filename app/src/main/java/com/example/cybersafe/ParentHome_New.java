package com.example.cybersafe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentHome_New extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_parent_home__new);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, new HomeFragment()).commit();

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment(); //Done
                break;
            case R.id.navigation_Incoming:
                fragment = new IncomingFragment(); //Done
                break;
            case R.id.navigation_List:
                fragment = new ViewReportedBullyingFragment(); //Done
                break;
            case R.id.navigation_Edit:
                fragment = new Edit_Parent_ProfileFragment(); //Done
                break;
            case R.id.navigation_Keywords:
                fragment = new Add_Detection_KeywordFragment(); //Done
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).commit();
        return true;
    };
}

