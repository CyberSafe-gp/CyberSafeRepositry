package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportTo extends AppCompatActivity {
    String Comment_id, SMA_id, sender,ChildID, body, childFName, childLName, childName,childAccount;
    ImageView back, home;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_to);

        Comment_id =getIntent().getStringExtra("Comment_id");
        SMA_id =getIntent().getStringExtra("SMA_id");
        sender =getIntent().getStringExtra("sender");
        body =getIntent().getStringExtra("body");
        ChildID = getIntent().getStringExtra("Child_id");


        TextView BullyCommentText = (TextView)findViewById(R.id.BullyCommentText);
        BullyCommentText.setText(body);


        TextView WriteBullyAccount = (TextView)findViewById(R.id.WriteBullyAccount);
        WriteBullyAccount.setText(sender);

        TextView WriteApplication = (TextView)findViewById(R.id.WriteApplication);
        WriteApplication.setText("TikTok");


        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Children");
        DatabaseReference smaRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");


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
                Child child= dataSnapshot.getValue(Child.class);
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




        //Toolbar
        back = (ImageView) findViewById(R.id.arrowIncomP);
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
        });






    }
}
