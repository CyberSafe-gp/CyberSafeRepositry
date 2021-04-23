package com.example.cybersafe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ReportToFragment extends Fragment {

    String Comment_id,senderID, SMA_id, sender,ChildID, body, childFName, childLName, childName,childAccount,parentID, schoolMID,school_Token;
    ImageView back, home;
    Child child, senderObj;
    private FirebaseUser user;
    boolean findSchoolManager=false;
    Button gethelp;
    //For today date
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    String childSchoolId, senderSchoolId;

    public ReportToFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_report_to, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            parentID = user.getUid().trim();
        } else {

            // No user is signed in
        }

        Bundle bundle = this.getArguments();

        Comment_id = bundle.getString("Comment_id");
        SMA_id = bundle.getString("SMA_id");
        sender = bundle.getString("sender");
        body = bundle.getString("body");
        ChildID = bundle.getString("Child_id");

        gethelp= (Button) getActivity().findViewById(R.id.getthelp);

        gethelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(getActivity() ,Gethelp.class);

                startActivity(intent);
            }
        });


        TextView BullyCommentText = (TextView)getActivity().findViewById(R.id.BullyCommentText);
        BullyCommentText.setText(body);


        TextView WriteBullyAccount = (TextView)getActivity().findViewById(R.id.WriteBullyAccount);
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
                TextView WriteChildAccount = (TextView)getActivity().findViewById(R.id.WriteChildAccount);
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
                childSchoolId=child.getSchool_id();

                TextView WriteChildName = (TextView)getActivity().findViewById(R.id.WriteChildName);
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
                                        senderSchoolId=senderObj.getSchool_id();



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




        Button report = (Button)getActivity().findViewById(R.id.report);


        //check if School Manager exist and get the School Manager ID also enable or enable the button
        schoolRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SchoolManager SM = messageSnapshot.getValue(SchoolManager.class);
                    if(SM.getSchool_id().equals(childSchoolId)){//check if School Manager exist and get the School Manager ID
                        schoolMID=SM.getSchoolManager_id();
                        findSchoolManager=true;
                    }
                }

                //Enable or enable the button
                if (childSchoolId.equals(senderSchoolId) && !(ChildID.equals(senderID)) && findSchoolManager){

                    report.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                    report.setEnabled(true);

                } else {
                    report.setBackgroundColor(getResources().getColor(R.color.gray));
                    report.setEnabled(false);

                    System.out.println("##################");
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
                                                      Toast.makeText(getActivity(), "Report send successfully", Toast.LENGTH_LONG).show();


                                                  } else {
                                                      Toast.makeText(getActivity(), "Report doesn't send successfully", Toast.LENGTH_LONG).show();
                                                  }

                                              }
                                          });

                                      }
                                  }
        );






    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
