package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report_info_reported extends AppCompatActivity {
    String Comment_id, Report_id, sender_id, Status,receiver_id, childID, userType;
    DatabaseReference commentRef, SMARef, childRef;
    String childAccount, childName, application, commentText, bullyAccount,SMAccountCredentials_id;
    ImageView back, home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_info_reported);


        Comment_id =getIntent().getStringExtra("Comment_id");
        Report_id =getIntent().getStringExtra("Report_id");
        sender_id =getIntent().getStringExtra("sender_id");
        receiver_id =getIntent().getStringExtra("receiver_id");
        Status =getIntent().getStringExtra("Status");
        userType =getIntent().getStringExtra("userType");

        //Toolbar
/*
        back = (ImageView) findViewById(R.id.arrowIncomP);
        home = (ImageView) findViewById(R.id.homeIncomP);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                */
/*Intent mIntent = new Intent(FlagMain.this, ChildHome.class);
                mIntent.putExtra("Child_id", childID);
                startActivity(mIntent);*//*


            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(Report_info_reported.this, ChildHome.class);
                mIntent.putExtra("userType", userType);
                startActivity(mIntent);

            }
        });
*/


        // get the comment to retrieve the info
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                        Comment com = messageSnapshot.getValue(Comment.class);

                        if (com.getComment_id().equals(Comment_id)){

                            // the info that we can reach from the comment
                            commentText = com.getBody();
                            bullyAccount = com.getSender();
                            SMAccountCredentials_id = com.getSMAccountCredentials_id();

                            //Write the bully comment and the bully(sender) account and child account on the text view
                            TextView BullyCommentText = (TextView)findViewById(R.id.BullyCommentText);
                            BullyCommentText.setText(commentText);

                            TextView WriteBullyAccount = (TextView)findViewById(R.id.WriteBullyAccount);
                            WriteBullyAccount.setText(bullyAccount);


                            break;}
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // the info that we can reach from the comment
/*        System.out.println("Report comment.getBody()"+comment.getBody());
        commentText = comment.getBody();
        bullyAccount = comment.getSender();
        SMAccountCredentials_id = comment.getSMAccountCredentials_id();*/



        // get the smAccountCredentials to retrieve the info
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials sma = messageSnapshot.getValue(SMAccountCredentials.class);

                    if (sma.getId().equals(SMAccountCredentials_id)){

                        // the info that we can reach from the SMAccountCredentials

                        application = sma.getSocialMediaPlatform();
                        childAccount=sma.getAccount();
                        childID= sma.getChild_id();
                        System.out.println("childID "+childID);

                        //Write the platform and child account
                        TextView WriteApplication = (TextView)findViewById(R.id.WriteApplication);
                        WriteApplication.setText(application);

                        TextView WriteChildAccount = (TextView)findViewById(R.id.WriteChildAccount);
                        WriteChildAccount.setText(childAccount);

                        // get the smAccountCredentials to retrieve the info
                        childRef = FirebaseDatabase.getInstance().getReference().child("Children");
                        childRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                    Child ch = messageSnapshot.getValue(Child.class);
                                    //The comment not bully and same as the child email
                                    System.out.println("ch.getChild_id() "+ch.getChild_id());
                                    if (ch.getChild_id().equals(childID)){
                                        childName = ch.getFirstName() +" "+ ch.getLastName();
                                        System.out.println("childName "+childName);
                                        System.out.println("ch.getFirstName() "+ch.getFirstName());
                                        System.out.println("ch.getLastName() "+ch.getLastName());
                                        TextView WriteChildName = (TextView)findViewById(R.id.WriteChildName);
                                        WriteChildName.setText(childName);

                                        break;}
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        home.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent mIntent = new Intent(Report_info_reported.this, ChildHome.class);
                                mIntent.putExtra("userType", userType);
                                mIntent.putExtra("Child_id", childID);
                                startActivity(mIntent);

                            }
                        });


                        break;}

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










    }
}