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

import com.example.cybersafe.Objects.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class FlagMain extends AppCompatActivity {
    //private EditText editText, etd;
    //private ImageView flag;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    //private LinearLayoutManager linearLayoutManager;


    RecyclerView recyclerView;
    ArrayList<Comment> commentList = new ArrayList();
    flagBullyCommentAdapter adapter;
    DatabaseReference commentRef, commentsRef, usersRef;
    //OnItemClickListener listener;
    String childAccount, childId;
    private TextView textView;


    //private DatabaseReference g =  FirebaseDatabase.getInstance().getReference("Comment");// لازم اغير الباث


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_bully_comment);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        myToolbar.setTitle("AbhiAndroid");
//        setSupportActionBar(myToolbar);

        /*Intent iin= getIntent();
        Bundle bun = iin.getExtras();*/

/*        if(bun!=null)
        {
            childAccount =(String) bun.get("childAccount");
            childId =(String) bun.get("childId");
        }*/


        //childAccount = "Lenaah";
        //childId="1";

        //https://medium.com/android-grid/how-to-use-firebaserecycleradpater-with-latest-firebase-dependencies-in-android-aff7a33adb8b
/*        editText = findViewById(R.id.et);
        etd = findViewById(R.id.etd);
        button = findViewById(R.id.btn);*/

        textView = (TextView) findViewById(R.id.noComments);

        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef.keepSynced(true);
        //System.out.println("H2");
        //usersRef = FirebaseDatabase.getInstance().getReference().child("users");

 /*       adapter = new flagBullyCommentAdapter(this, commentList, new OnItemClickListener() {

            @Override
            public void onItemClick(int posistion) {

            }
        });*/



        recyclerView = findViewById(R.id.recyclerViewFlag);
        adapter = new flagBullyCommentAdapter(this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //System.out.println("H3");
/*        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int posistion) {
                String list = commentList.get(posistion).get;
                Intent in = new Intent(FlagMain.this, previewnote.class); // previewnote==report

            }

        });*/
        // add the comment
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("snapshot");
                if (snapshot.exists()) {
                    System.out.println("exists");
                    commentList.clear();
                    //commentList.clear();
                    //commentList.addAll(newList);
                    //adapter.notifyDataSetChanged();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Comment com = messageSnapshot.getValue(Comment.class);
                        System.out.println("H");
                        //The comment not bully and same as the child email
                        if (com.getFlag().equals(false) && com.getSMAccountCredentials_id().equals("-MTz69mBYxgQdUwUjR2b"))
                            commentList.add(com);
                        System.out.println("H1");
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
        System.out.println("H4");


        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotrf) {
                for (DataSnapshot childrf : snapshotrf.getChildren()) {
                    Comment findCom = childrf.getValue(Comment.class);
                    //String acu = findCom.getSMAccountCredentials_Account();
                    if (findCom.getFlag().equals(false) &&findCom.getSMAccountCredentials_id().equals("-MTz69mBYxgQdUwUjR2b")) {
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

// M
/*
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Notes");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Notes");
        commentRef.keepSynced(true);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        commentAdapter = new flagBullyCommentAdapter(this, commentList, new CustomItemClickListener() {
*/


       // }

            //
    }
}

