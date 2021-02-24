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

import com.example.cybersafe.Objects.Child;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentHome extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    private ArrayList<Child> childrenList = new ArrayList();
    private ParentHomeAdapter adapter;
    DatabaseReference childRef, childrenRef;
    private String userID,User;
    public Button btn1,btn2,btn3,btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);
        User=getIntent().getStringExtra("User");

        //buttons
        btn1= findViewById(R.id.button3);
        //add new child
        btn2=findViewById(R.id.button4);
        //detection keyword
        btn3=findViewById(R.id.button14);
        //edit
        btn4=findViewById(R.id.button5);
        //log-out

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(ParentHome.this,Add_NewChild.class);

                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(ParentHome.this,Add_Detection_Keyword.class);

                startActivity(intent);
            }
        });





        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(ParentHome.this, EditSchool.class);
                //intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
        });
        //log-out
        //btn4.setOnClickListener(new View.OnClickListener() {
        //@Override
        // public void onClick(View v) {

        //startActivities();
        //}

        //private void startActivities() {

        //Intent intent = new Intent(SchoolHome.this, .class);
        //intent.putExtra("IntentName", "hi");
        //startActivity(intent);
        //}
        // });





        //adapter
        textView = (TextView) findViewById(R.id.noChild);

        childrenRef = FirebaseDatabase.getInstance().getReference().child("Children");
        childRef = FirebaseDatabase.getInstance().getReference().child("Children");
        childRef.keepSynced(true);
        //click child
        adapter = new ParentHomeAdapter(this, childrenList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {

                Child lr = childrenList.get(pos);
                String Child_id = lr.getChild_id();
                Intent in = new Intent(ParentHome.this, ChildHome.class);
                in.putExtra("Child_id",Child_id);
                in.putExtra("User",User);

                startActivity(in);
            }
        });

        recyclerView = findViewById(R.id.recyclerPH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        userID="rTTwi9dfa9W425lPa2A6MiU93yz1";// بدال هذا قيت اوث عشان الاي دي حقت البارنت
        //userID="rTTwi9dfa9MiU93yz1";

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    childrenList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Child ch = messageSnapshot.getValue(Child.class);
                        //The sent report
                        if (ch.getParent_id().equals(userID)){ //if (rep.getUser_id().equals(userID))
                            childrenList.add(ch);
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



        childrenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childrf : snapshot.getChildren()) {
                    Child findCh = childrf.getValue(Child.class);
                    String user_id = findCh.getParent_id(); //String user_id = findRep.getUser_id();
                    if (user_id.equals(userID)) {
                        textView.setText("");
                        break;
                    } else {
                        textView.setText("no existing children");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
