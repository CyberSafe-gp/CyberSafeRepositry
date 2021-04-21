package com.example.cybersafe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class
ParentHome_New extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    DatabaseReference commentsRef, ChildRef,SMARef,reportRef;
    ArrayList<Child> parentChildren = new ArrayList();
    ArrayList<String> SMA_IDS = new ArrayList();
    String  Parent_ID, openNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentParent = FirebaseAuth.getInstance().getCurrentUser();
        if (currentParent != null) {
            Parent_ID = currentParent.getUid();
        }

        openNotification = getIntent().getStringExtra("openNotification");

        if (openNotification != null){

            if(openNotification.equals("BullyComment")){
                Fragment fragment  = new HomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).commit();


            } else if(openNotification.equals("IncomingReport")){
                Fragment fragment  = new IncomingFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).commit();
            }


        }

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_parent_home__new);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        BadgeDrawable badgeHome = bottomNavigationView.getOrCreateBadge(R.id.navigation_home);
        badgeHome.setVisible(false);
        BadgeDrawable badgeReport = bottomNavigationView.getOrCreateBadge(R.id.navigation_Incoming);
        badgeReport.setVisible(false);


        commentsRef = FirebaseDatabase.getInstance().getReference().child( "Comments" );
        ChildRef = FirebaseDatabase.getInstance().getReference().child( "Children" );
        SMARef = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");



////Notification
        final boolean[] existComment = {false};

        //For the bully comment badge
        //Get all parent children
        ChildRef.addValueEventListener(new ValueEventListener() {

                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               if (snapshot.exists()) {
                                                   parentChildren.clear();
                                                   System.out.println("there is children");
                                                   for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                                       Child checkChild = messageSnapshot.getValue(Child.class);
                                                       //Add children id that belong to the parent
                                                       if (checkChild.getParent_id().equals(Parent_ID)) {
                                                           parentChildren.add(checkChild);
                                                           System.out.println("childes added");
                                                       }
                                                   }
                                                   if (!(parentChildren.isEmpty())) {

                                                       for (int i = 0; parentChildren.size() > i; i++) {
                                                           String child_id = parentChildren.get( i ).getChild_id();

                                                           //Get the social media id for each parent's child
                                                           SMARef.addValueEventListener(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                   if (snapshot.exists()) {

                                                                       for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                                                           SMAccountCredentials checkSMA = messageSnapshot.getValue(SMAccountCredentials.class);

                                                                           if (checkSMA.getChild_id().equals(child_id)) {
                                                                               SMA_IDS.add(checkSMA.getId());
                                                                           }
                                                                       }
                                                                       //Check if the comment reference in database add new check if it belongs to one of the child and the comment is bully then show the notification
                                                                       commentsRef.addValueEventListener(new ValueEventListener() {
                                                                           @Override
                                                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                               if (snapshot.exists()){
                                                                                   existComment[0] = false;
                                                                                   for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                                                                       Comment lastComment = messageSnapshot.getValue(Comment.class);
                                                                                       String commentSMA =lastComment.getSMAccountCredentials_id();
                                                                                       String notification = lastComment.getNotification();
                                                                                       for (int i = 0; SMA_IDS.size() > i; i++){
                                                                                           if (SMA_IDS.get(i).equals(commentSMA)) {
                                                                                               if (lastComment.getFlag().equals(true) &&  notification.equals("new")) {
                                                                                                   //if there is a new comment and bully the exist[0] = true and show notification
                                                                                                   existComment[0] = true;

                                                                                               }
                                                                                           }

                                                                                       }

                                                                                   }
                                                                                   badgeHome.setVisible(existComment[0]);
                                                                               }
                                                                           }

                                                                           @Override
                                                                           public void onCancelled(@NonNull DatabaseError error) {

                                                                           }
                                                                       });

                                                                   }
                                                               }

                                                               @Override
                                                               public void onCancelled(@NonNull DatabaseError error) {

                                                               }
                                                           });
                                                       }
                                                   }
                                               }
                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });

        badgeHome.setVisible(existComment[0]);


        //For the Incoming report badge
        //To get the list of the incoming report
        final boolean[] existReport = {false};
        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    existReport[0]=false;
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //If there new incoming report notify the school manager
                        if (rep.getReceiver_id().equals(Parent_ID) && !(rep.getStatus().equals("Confirm"))){
                            System.out.println("RRPPPOOOTTYY");
                            existReport[0] = true;

                        }
                    }

                }
                //if there is a new incoming report and not confirm the exist[0] = true and show badge
                badgeReport.setVisible(existReport[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        badgeReport.setVisible(existReport[0]);





////

        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, new HomeFragment()).commit();

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =item -> {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();

                break;
            case R.id.navigation_Incoming:
                fragment = new IncomingFragment();
                break;
            case R.id.navigation_school:
                fragment = new ViewReportedBullyingFragment();
                break;
            case R.id.navigation_Edit:
                fragment = new Edit_Parent_ProfileFragment();
                break;
            case R.id.navigation_Keywords:
                fragment = new Add_Detection_KeywordFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).commit();
        return true;
    };
}

