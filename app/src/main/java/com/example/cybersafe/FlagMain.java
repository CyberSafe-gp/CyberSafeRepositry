package com.example.cybersafe;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toolbar;
//import android.support.v7.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class FlagMain extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Comment> commentList = new ArrayList();
    flagBullyCommentAdapter adapter;
    DatabaseReference commentRef, commentsRef, SMAccountCredentialRef;
    //OnItemClickListener listener;
    String childAccount, childId,SMAccountCredentialID;
    private TextView textView;
    String childID;
    ImageView back, home;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_bully_comment);
  //      Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        myToolbar.setTitle("AbhiAndroid");
//        setSupportActionBar(myToolbar);



        Intent in = getIntent();
        childID = getIntent().getStringExtra("Child_id");
        //childID="-MTz33wOa3jPoFQlixeP";

        //Toolbar
        back = (ImageView) findViewById(R.id.arrowFlag);
        home = (ImageView) findViewById(R.id.homeFlag);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent mIntent = new Intent(FlagMain.this, ChildHome.class);
                mIntent.putExtra("Child_id", childID);
                startActivity(mIntent);*/

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               /* Intent mIntent = new Intent(FlagMain.this, ChildHome.class);
                mIntent.putExtra("Child_id", childID);
                startActivity(mIntent);*/

            }
        });


        textView = (TextView) findViewById(R.id.noComments);
        SMAccountCredentialRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef.keepSynced(true);


        recyclerView = findViewById(R.id.recyclerViewFlag);
        adapter = new flagBullyCommentAdapter(this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Get the SMAccountCredential ID for the Child to get the comments
        SMAccountCredentialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        SMAccountCredentials smAccountCredentials = messageSnapshot.getValue(SMAccountCredentials.class);
                        //The comment not bully and same as the child email
                        if (smAccountCredentials.getChild_id().equals(childID)){
                            SMAccountCredentialID=smAccountCredentials.getId();

                            //Now we get the comments with flag = false (not bully)
                            commentRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        commentList.clear();

                                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                            Comment com = messageSnapshot.getValue(Comment.class);
                                            //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                                            if (com.getFlag().equals(false) && com.getSMAccountCredentials_id().equals(SMAccountCredentialID)){
                                                commentList.add(com);

                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("===", "No Data Was Found");

                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //If no comment found print no existing comments
                            commentsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotrf) {
                                    for (DataSnapshot childrf : snapshotrf.getChildren()) {
                                        Comment findCom = childrf.getValue(Comment.class);
                                        //String acu = findCom.getSMAccountCredentials_Account();
                                        if (findCom.getFlag().equals(false) && findCom.getSMAccountCredentials_id().equals(SMAccountCredentialID)) {
                                            textView.setText("");
                                            break;
                                        } else {
                                            textView.setText("no existing comments");

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

