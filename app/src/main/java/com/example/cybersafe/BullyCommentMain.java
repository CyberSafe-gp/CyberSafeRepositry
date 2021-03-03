package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.example.cybersafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BullyCommentMain extends AppCompatActivity {
    String childID;
    ImageView back, home;
    DatabaseReference commentRef, commentsRef, SMAccountCredentialRef;
    private TextView textView;
    RecyclerView recyclerView;
    ArrayList<Comment> commentList = new ArrayList();
    BullyCommentsAdapter adapter;
    String SMAccountCredentialID, ChildID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullycomments);
        ChildID = getIntent().getStringExtra("Child_id");
        System.out.println("ChildID BCM "+ChildID);


        //Toolbar
        back = (ImageView) findViewById(R.id.arrowIncomP);
        home = (ImageView) findViewById(R.id.homeIncomP);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent in = getIntent();
        childID = getIntent().getStringExtra("Child_id");

        //Toolbar
        back = (ImageView) findViewById(R.id.arrowIncomP);
        home = (ImageView) findViewById(R.id.homeIncomP);

        textView = (TextView) findViewById(R.id.noComments);
        SMAccountCredentialRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef.keepSynced(true);
        adapter = new BullyCommentsAdapter(this, commentList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Comment com = commentList.get(pos);
                reportInfo(com);

            }
        });

        System.out.println("adapter 222222 ");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        System.out.println("adapter 333333");

        //Get the SMAccountCredential ID for the Child to get the comments
        SMAccountCredentialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials smAccountCredentials = messageSnapshot.getValue(SMAccountCredentials.class);
                    //The comment not bully and same as the child email
                    if (smAccountCredentials.getChild_id().equals(childID)){
                        SMAccountCredentialID=smAccountCredentials.getId();

                        //Now we get the comments with flag = true (bully)
                        commentRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    commentList.clear();

                                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                        Comment com = messageSnapshot.getValue(Comment.class);
                                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                                        if (com.getFlag().equals(true) && com.getSMAccountCredentials_id().equals(SMAccountCredentialID)){
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
                                    if (findCom.getFlag().equals(true) && findCom.getSMAccountCredentials_id().equals(SMAccountCredentialID)) {
                                        textView.setText("");
                                        break;
                                    } else {
                                        textView.setText("no existing bully comments");

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

    public void reportInfo(Comment comment){

        String comment_id=comment.getComment_id();
        String SMA_id = comment.getSMAccountCredentials_id();
        String sender = comment.getSender();
        String body = comment.getBody();

        Intent in = new Intent(BullyCommentMain.this, ReportTo.class);
        in.putExtra("Comment_id", comment_id);
        in.putExtra("SMA_id", SMA_id);
        in.putExtra("sender", sender);
        in.putExtra("body", body);
        in.putExtra("Child_id", ChildID);


        startActivity(in);


    }
}