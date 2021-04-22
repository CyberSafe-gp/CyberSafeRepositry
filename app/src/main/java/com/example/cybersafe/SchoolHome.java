package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.School;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  SchoolHome extends AppCompatActivity {


    public Button btn11;
    public Button btn12;
    private Button logO2;
    DatabaseReference schoolManagerRef, schoolRef;
    String userID;
    String firstName, lastName, schoolID, schoolName,userType1;
    TextView SN;
    TextView SID;

//
    //make instance of DatabaseReference for the data base so we get access and read data:)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home);
        //bring the current user loged-in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            userType1=getIntent().getStringExtra("userType");
            //get current user type

        } else {

            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(SchoolHome.this, Interface.class);
            //to force the user log in
            startActivity(in);
        }



        btn11 =  findViewById(R.id.btn1);
        //edit button
        btn12 = findViewById(R.id.button7);
        //incoming reports button
        logO2= findViewById(R.id.button10);
        // log-out button
        SN=(TextView)findViewById(R.id.textView24);

        SID=(TextView)findViewById(R.id.textView16);




        schoolManagerRef = FirebaseDatabase.getInstance().getReference().child("SchoolManagers");
        schoolManagerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//loop over the school manager database to find the current loged in school manager
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                    SchoolManager sch = messageSnapshot.getValue(SchoolManager.class);

                    if (sch.getSchoolManager_id().equals(userID)){

                        // the info that we can reach from the school manager database
                        firstName = sch.getFirstName();
                        lastName = sch.getLastName();
                        schoolID=sch.getSchool_id();


                        //Write the school manager name on the text view
                        TextView schoolManagerName = (TextView)findViewById(R.id.textView24);
                        schoolManagerName.setText(firstName+" "+lastName);

                        schoolRef = FirebaseDatabase.getInstance().getReference().child("Schools");
                        schoolRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                                    School sch = messageSnapshot.getValue(School.class);

                                    if (sch.getSchool_id().equals(schoolID)){


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



            //edit button
        btn11 = (Button) findViewById(R.id.btn1);
            //if clicked transfer to edit page
        btn11.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolHome.this,EditSchoolFragment.class);
            //send intent to every page with user type
            intent.putExtra("userType",userType1);
            startActivity(intent);
        });
     //transfer to incoming report page
        btn12.setOnClickListener(v -> {
            Intent intent = new Intent(SchoolHome.this,IncomingReportsMain.class);
            intent.putExtra("userType",userType1);
            startActivity(intent);

        });

        //log-out
        logO2.setOnClickListener(new View.OnClickListener() {
          @Override
         public void onClick(View v) {

              startActivities();
          }
      //  if loged-out transfer to the interface
           private void startActivities() {

                Intent intent = new Intent(SchoolHome.this,Interface.class);
                 intent.putExtra("IntentName", "hi");
                startActivity(intent);
           }
           });



    }
}




