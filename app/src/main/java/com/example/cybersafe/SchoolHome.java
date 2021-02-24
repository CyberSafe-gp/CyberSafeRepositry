package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.School;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    DatabaseReference schoolManagerRef, schoolRef;
    String userID;
    String firstName, lastName, schoolID, schoolName,userType;
    TextView SN;
    TextView SID;

//
    //make instance of DatabaseReference for the data base so we get access and read data:)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home);
        userType=getIntent().getStringExtra("userType");

        btn11 =  findViewById(R.id.btn1);
        //edit
        btn12 = findViewById(R.id.button7);
        //incoming reports
        btn13= findViewById(R.id.button10);
        // log-out
        SN=(TextView)findViewById(R.id.textView24);
        SID=(TextView)findViewById(R.id.textView16);



        /*fauth=FirebaseAuth.getInstance();
        userID=fauth.getCurrentUser().getUid();*/
        userID="-MTz4FV6I8eik51jwoJ1";

        schoolManagerRef = FirebaseDatabase.getInstance().getReference().child("SchoolManagers");
        schoolManagerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                    SchoolManager sch = messageSnapshot.getValue(SchoolManager.class);

                    if (sch.getSchoolManager_id().equals(userID)){

                        // the info that we can reach from the school manager
                        firstName = sch.getFirstName();
                        lastName = sch.getLastName();
                        schoolID=sch.getSchool_id();
                        System.out.println("schoolID"+schoolID);

                        //Write the school manager name on the text view
                        TextView schoolManagerName = (TextView)findViewById(R.id.textView24);
                        schoolManagerName.setText(firstName+" "+lastName);

                        schoolRef = FirebaseDatabase.getInstance().getReference().child("Schools");
                        schoolRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                                    School sch = messageSnapshot.getValue(School.class);
                                    System.out.println("out"+schoolID);
                                    System.out.println("from"+sch.getSchool_id());

                                    if (sch.getSchool_id().equals(schoolID)){
                                        System.out.println("getSchool_id().equals(schoo");

                                        // the info that we can reach from the school
                                        schoolName = sch.getSchoolName();

                                        //Write the school  name on the text view
                                        TextView schoolNameTV = (TextView)findViewById(R.id.textView16);
                                        schoolNameTV.setText(schoolName);

                                        break;}
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        break;}
                }
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
                //intent.putExtra("IntentName", "hi");
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
                intent.putExtra("userType",userType);
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




