package com.example.cybersafe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportTo extends AppCompatActivity {
    String Comment_id,senderID, SMA_id, sender,ChildID, body, childFName, childLName, childName,childAccount,parentID, schoolMID;
    ImageView back, home;
    Child child, senderObj;
    private FirebaseUser user;
    boolean findSchoolManager=false;

    //For today date
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_to);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            parentID = user.getUid().trim();
        } else {

            // No user is signed in
        }

        Comment_id =getIntent().getStringExtra("Comment_id");
        SMA_id =getIntent().getStringExtra("SMA_id");
        sender =getIntent().getStringExtra("sender");
        body =getIntent().getStringExtra("body");
        ChildID = getIntent().getStringExtra("Child_id");


        TextView BullyCommentText = (TextView)findViewById(R.id.BullyCommentText);
        BullyCommentText.setText(body);


        TextView WriteBullyAccount = (TextView)findViewById(R.id.WriteBullyAccount);
        WriteBullyAccount.setText(sender);

//        TextView WriteApplication = (TextView)findViewById(R.id.WriteApplication);
//        WriteApplication.setText("TikTok");


        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Children");
        DatabaseReference smaRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        DatabaseReference schoolRef = FirebaseDatabase.getInstance().getReference().child("SchoolManagers");


        smaRef.child(SMA_id).child("account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childAccount = dataSnapshot.getValue(String.class);
                TextView WriteChildAccount = (TextView)findViewById(R.id.WriteChildAccount);
                WriteChildAccount.setText(childAccount);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        childRef.child(ChildID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                child= dataSnapshot.getValue(Child.class);
                childFName = child.getFirstName();
                childLName = child.getLastName();
                childName=childFName+" "+childLName;

                TextView WriteChildName = (TextView)findViewById(R.id.WriteChildName);
                WriteChildName.setText(childName);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Button Report to School Manager
        smaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials sma = messageSnapshot.getValue(SMAccountCredentials.class);

                    if (sma.getAccount().equals(sender)){

                        // the info that we can reach from the SMAccountCredentials
                        senderID= sma.getChild_id();

                        // get the smAccountCredentials to retrieve the info
                        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Children");
                        childRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                    Child ch = messageSnapshot.getValue(Child.class);
                                    if (ch.getChild_id().equals(senderID)){
                                      senderObj=ch;

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

        String childSchoolId=child.getSchool_id();
        String senderSchoolId=senderObj.getSchool_id();
        String senderID=senderObj.getChild_id();
        Button report = (Button)findViewById(R.id.report);


        //check if School Manager exist and get the School Manager ID also enable or enable the button
        schoolRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SchoolManager SM = messageSnapshot.getValue(SchoolManager.class);
                    if(SM.getSchool_id().equals(childSchoolId)){//check if School Manager exist and get the School Manager ID
                        schoolMID=SM.getSchool_id();
                        findSchoolManager=true;
                    }
                }

                //Enable or enable the button
                if (childSchoolId.equals(senderSchoolId) && !(ChildID.equals(senderID)) && findSchoolManager){
                    report.setEnabled(true);

                } else {
                    report.setEnabled(false);
                    report.setBackgroundColor(Color.parseColor("#F8F8F8"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        report.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String reportID = reportRef.push().getKey();

                                          Report reportObj=new Report(reportID,parentID,schoolMID,Comment_id,"Pending",getDateTime());

                                          //Add "send" Report to database
                                          reportRef.child(reportID).setValue(reportObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {

                                                  if (task.isSuccessful()) {
                                                      Toast.makeText(ReportTo.this, "Report send successfully", Toast.LENGTH_LONG).show();
                                                      finish();

                                                  } else {
                                                      Toast.makeText(ReportTo.this, "Report doesn't send successfully", Toast.LENGTH_LONG).show();
                                                  }

                                              }
                                          });

                                      }
                                  }
        );



        //Toolbar
/*        back = (ImageView) findViewById(R.id.arrowIncomP);
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

                Intent mIntent = new Intent(ReportTo.this, ChildHome.class);
                mIntent.putExtra("userType", "Parent");
                mIntent.putExtra("Child_id", ChildID);
                startActivity(mIntent);

            }
        });*/






    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
