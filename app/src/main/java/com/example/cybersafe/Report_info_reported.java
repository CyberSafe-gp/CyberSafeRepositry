package com.example.cybersafe;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                                    if (ch.getChild_id().equals(childID)){
                                        childName = ch.getFirstName() +" "+ ch.getLastName();
                                        TextView WriteChildName = (TextView)findViewById(R.id.WriteChildName);
                                        WriteChildName.setText(childName);

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










    }
}