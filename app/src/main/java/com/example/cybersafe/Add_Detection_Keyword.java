package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Keyword;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Add_Detection_Keyword extends AppCompatActivity {
     public Button dK;
    private String userID;
    DatabaseReference keywordsRef, keywordRef;
    RecyclerView recyclerView;
    ArrayList<Keyword> keywordArrayList= new ArrayList();
    Add_Detection_KeywordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__detection__keyword);

   /*     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();

        } else {
            System.out.println("userID out");
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(Add_Detection_Keyword.this, ParentLogin.class);
            startActivity(in);
        }*/

        userID="oN6Md1uGmBaWi6lyKBiTexo7x263oN6Md1uGmBaWi6lyKBiTexo7x263";

        TextView textView = (TextView) findViewById(R.id.noKeyword);

        keywordsRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerViewAddDe);
        adapter = new Add_Detection_KeywordAdapter(this, keywordArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Now we get the comments with flag = false (not bully)
        keywordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keywordArrayList.clear();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Keyword key = messageSnapshot.getValue(Keyword.class);
                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                        if (key.getParent_id().equals(userID)){
                            keywordArrayList.add(key);
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
        keywordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotrf) {
                for (DataSnapshot childrf : snapshotrf.getChildren()) {
                    Keyword findKey = childrf.getValue(Keyword.class);
                    //String acu = findCom.getSMAccountCredentials_Account();
                    if (findKey.getParent_id().equals(userID)) {
                        textView.setText("");
                        break;
                    } else {
                        textView.setText("no existing Keyword");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Add-Detection-Keyword-Button

        dK= findViewById(R.id.addDetectionK);

        dK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent inn = new Intent(Add_Detection_Keyword.this,Add_Detection_Keyword2.class);
                startActivity(inn);
            }
        });

    }
}