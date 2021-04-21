package com.example.cybersafe;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChildHomeFragment extends Fragment {

    LinearLayout bullyComments, flagComments;
    ImageView redDot;
    String ChildID,userType1,userID;
    DatabaseReference commentsRef,SMARef;
    String SMA_id;


    public ChildHomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_child_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = this.getArguments();
        //get the current loged-in user
        if (user != null) {
            userID = user.getUid();//bring user id
            ChildID = bundle.getString("Child_id");  //take from the parent home page the selected child id
            userType1= bundle.getString("userType");  //store what type of loged-in user is

        } else {
            Intent in = new Intent(getActivity(), Interface.class);
            startActivity(in);
            //to go back for the parent home
        }


        bullyComments = getActivity().findViewById(R.id.bullyComments);
        flagComments=  getActivity().findViewById(R.id.flagComment);
        redDot =getActivity().findViewById(R.id.redDot);

        bullyComments.setOnClickListener(v -> {

            //Go to Bully Comments page
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = new BullyCommentMainFragment();
            Bundle bundleBully= new Bundle();
            bundleBully.putString("Child_id", ChildID);
            fragment.setArguments(bundleBully);
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).addToBackStack(null).commit();


        });




        flagComments.setOnClickListener(v -> {

            //Go to Flag page
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = new FlagMainFragment();
            Bundle bundleFlag = new Bundle();
            bundleFlag.putString("Child_id", ChildID);
            fragment.setArguments(bundleFlag);
            fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).addToBackStack(null).commit();

        });


        ////Notification

        SMARef = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );
        commentsRef = FirebaseDatabase.getInstance().getReference().child( "Comments" );
        final boolean[] exist = {false};

        //Get the social media id for the child
        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        SMAccountCredentials checkSMA = messageSnapshot.getValue(SMAccountCredentials.class);

                        if (checkSMA.getChild_id().equals(ChildID)) {
                            SMA_id = checkSMA.getId();



                            //Check if the comment reference in database add new check if it belongs to  the child and the comment is bully then show the notification
                            commentsRef.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                            Comment lastComment = messageSnapshot.getValue(Comment.class);
                                            String commentSMA = lastComment.getSMAccountCredentials_id();
                                            String notification = lastComment.getNotification();

                                            if (SMA_id.equals(commentSMA)) {
                                                if (lastComment.getFlag().equals(true) && notification.equals("new")) {
                                                    exist[0] = true;
                                                }
                                            }
                                        }
                                        //if there is a new comment and bully show notification
                                        if (exist[0]){
                                            redDot.setVisibility(View.VISIBLE);
                                        }else{
                                            redDot.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



////







    }
}
