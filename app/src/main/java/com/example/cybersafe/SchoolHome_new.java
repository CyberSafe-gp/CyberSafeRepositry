package com.example.cybersafe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cybersafe.Objects.Report;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolHome_new extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String userID;

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
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_incoming2);

        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout2, new SchoolHomeFragment()).commit();

        }

        FirebaseUser currentParent = FirebaseAuth.getInstance().getCurrentUser();
        if (currentParent != null) {
            userID = currentParent.getUid();

        }

        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");

        //To get the list of the incoming report
        reportRef.addValueEventListener(new ValueEventListener() {
            final boolean[] exist = {false};

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //If there new incoming report notify the school manager
                        if (rep.getReceiver_id().equals(userID) && !(rep.getStatus().equals("Confirm"))){
                            exist[0] = true;
                        }
                    }

                }
                //if there is a new incoming report and not confirm the exist[0] = true and show badge
                badge.setVisible(exist[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
