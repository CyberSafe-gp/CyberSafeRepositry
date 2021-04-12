package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ParentHome extends AppCompatActivity {
    private TextView textView;
    //
    private RecyclerView recyclerView;
    //for the adapter
    private ArrayList<Child> childrenList = new ArrayList();
    //to list all the current parent childs
    private ParentHomeAdapter adapter;
    DatabaseReference childRef, childrenRef;
    private String userID,userType1;
    //userType1 is for storing  what is the type of the current user
    public Button btn2,btn3,btn4,btn5,btn6;
    // btn2 is for detection keyword button
    //btn3 is for edit button
    //btn4 is for log-out button
    //btn5 is for incoming report button
    //btn6 is for view reported list button
    FloatingActionButton actionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get the current user
        if (user != null) {

            userID = user.getUid();
            //get and store current user id
            userType1 = getIntent().getStringExtra("userType");
            //get and store userType1 is for storing  what is the type of the current user
        } else {

            Intent in = new Intent(ParentHome.this, ParentLogin.class);
            startActivity(in);
        }
/*

        //buttons

        btn2 = findViewById(R.id.button4);
        //detection keyword
        btn3 = findViewById(R.id.button14);
        //edit
        btn4 = findViewById(R.id.button5);
        //log-out
        btn5 = findViewById(R.id.incomingrep);
        //incoming rep
        btn6 = findViewById(R.id.Viewrep);
        // view report




        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(ParentHome.this, Add_Detection_Keyword.class);
            intent.putExtra("userType", userType1);
             //to send for the next page the type of user
            startActivity(intent);
            //go to the add detecton page
        });


        btn3.setOnClickListener(v -> {


            Intent intent = new Intent(ParentHome.this, Edit_Parent_Profile.class);
            intent.putExtra("userType", userType1);
            startActivity(intent);

        });


        //log-out
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {
                FirebaseAuth.getInstance().signOut();
                //sign out from the auth firbase
                finish();
                Intent intent = new Intent(ParentHome.this, Interface.class);
                // intent.putExtra("userType", "Parent");
                startActivity(intent);
            }
        });


        btn5.setOnClickListener(v -> {

            Intent intent = new Intent(ParentHome.this, IncomingReportsMain.class);
            intent.putExtra("userType", userType1);
            startActivity(intent);
        });

        btn6.setOnClickListener(v -> {


            Intent intent = new Intent(ParentHome.this, ViewReportedBullyingMain.class);
            intent.putExtra("userType", userType1);
            startActivity(intent);
        });

        //adapter
        textView = (TextView) findViewById(R.id.noChild);
//adapter
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
                in.putExtra("Child_id", Child_id);
                in.putExtra("userType", userType1);
//send usertype and selected child id to the child home
                startActivity(in);
            }
        });

        recyclerView = findViewById(R.id.recyclerPH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //userID="rTTwi9dfa9W425lPa2A6MiU93yz1";// بدال هذا قيت اوث عشان الاي دي حقت البارنت
        //userID="rTTwi9dfa9MiU93yz1";

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    childrenList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Child ch = messageSnapshot.getValue(Child.class);
                        //The sent report
                        if (ch.getParent_id().equals(userID)) { //if (rep.getUser_id().equals(userID))
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

        actionButton.findViewById(R.id.floating_action_button);


         //floating_action_button for add child
       actionButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParentHome.this, Add_NewChild.class);
            startActivity(intent);
        });*/
//go to the add child page
        // Respond to FAB click

    }}

