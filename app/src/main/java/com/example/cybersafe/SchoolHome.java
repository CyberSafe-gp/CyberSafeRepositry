package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolHome extends AppCompatActivity {

    private FirebaseAuth fauth;
    public Button btn11;
    public Button btn12;
    public Button btn13;
    String userID;
    String sID;
    TextView SN;
    TextView SID;

//
    //make instance of DatabaseReference for the data base so we get access and read data:)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home);

        btn11 =  findViewById(R.id.btn1);
        //edit
        btn12 = findViewById(R.id.button7);
        //incoming reports
        btn13= findViewById(R.id.button10);
        // log-out
        SN=(TextView)findViewById(R.id.textView24);
        SID=(TextView)findViewById(R.id.textView16);

        fauth=FirebaseAuth.getInstance();
        userID=fauth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("School Manager").child(userID);

        myRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   sID=snapshot.child("school_id").getValue().toString();
                   SID.setText(sID);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database1.getReference().child("School").child(sID);
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("schoolName:").getValue().toString();
                SN.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn11 = (Button) findViewById(R.id.btn1);

        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(SchoolHome.this, EditSchool.class);
                intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
        });
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(SchoolHome.this, IncomingReportsMain.class);
                intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
        });
        //log-out
        //btn13.setOnClickListener(new View.OnClickListener() {
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



    }
}




