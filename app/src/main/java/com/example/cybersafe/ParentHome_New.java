package com.example.cybersafe;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class
ParentHome_New extends AppCompatActivity {
    BadgeDrawable badge;
    BottomNavigationView bottomNavigationView;
    DatabaseReference commentsRef, ChildRef,SMARef;
    ArrayList<Child> parentChildren = new ArrayList();
    ArrayList<String> SMA_IDS = new ArrayList();
    String  Parent_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentParent = FirebaseAuth.getInstance().getCurrentUser();
        if (currentParent != null) {
            Parent_ID = currentParent.getUid();

        }

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_parent_home__new);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_home);
        badge.setVisible(false);


        commentsRef = FirebaseDatabase.getInstance().getReference().child( "Comments" );
        ChildRef = FirebaseDatabase.getInstance().getReference().child( "Children" );
        SMARef = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );





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



                commentsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        System.out.println("onChildAdded ");
                        Comment lastComment = snapshot.getValue(Comment.class);
                        String commentSMA =lastComment.getSMAccountCredentials_id();
                        System.out.println("commentSMA "+commentSMA);
                        if (Arrays.asList("-MYMubiuPwqiHBNddt4j").contains(commentSMA)){
                            System.out.println("contains");
                            if(lastComment.getFlag().equals(true)){
                                badge.setVisible(true);
                                System.out.println("onChildAdded222222 ");
                            }

                        }



                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



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
//                badge.setVisible(false);
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

