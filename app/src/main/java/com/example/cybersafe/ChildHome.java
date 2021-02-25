package com.example.cybersafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChildHome extends AppCompatActivity {
     public Button btn1;
     public Button btn2;
     public Button btn3;
     public Button btn4;
     public Button btn5;
     public Button btn6;
    private FirebaseAuth fauth;
    String ChildID,userType1,userID;
   // Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            userID = user.getUid();
            ChildID = getIntent().getStringExtra("Child_id");
            userType1=getIntent().getStringExtra("userType");
        } else {
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(ChildHome.this, ParentLogin.class);
            startActivity(in);
        }





        //bundle=getIntent().getStringExtras();
        //if(bundle.getString("Child_id")!=null)
        //{ChildID=bundle.getString("Child_id");
    //}


        btn1 = findViewById(R.id.button8);
        //bully comment

        btn2 = findViewById(R.id.button17);
        //incoming reports

        btn3=  findViewById(R.id.button18);
        //view reported

        btn4=  findViewById(R.id.button19);
        //flag

        btn5=  findViewById(R.id.button12);
        //edit

        btn6=  findViewById(R.id.button22);
        //delete

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                startActivities();
            }


            private void startActivities() {
                Intent intent =new Intent(ChildHome.this,Bullycomments.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities2();
            }

            private void startActivities2() {

                Intent intent = new Intent(ChildHome.this,IncomingReportsMain.class);

                intent.putExtra("childId",ChildID);
                intent.putExtra("userType",userType1);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities1();
            }

            private void startActivities1() {

                Intent intent = new Intent(ChildHome.this,ViewReportedBullyingMain.class);
                intent.putExtra("childId",ChildID);
                startActivity(intent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities3();
            }

            private void startActivities3() {

                Intent intent = new Intent(ChildHome.this,FlagMain.class);
                intent.putExtra("childId",ChildID);
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities4();
            }

            private void startActivities4() {

                Intent intent = new Intent(ChildHome.this,EditSchool.class);
                intent.putExtra("childId",ChildID);
                startActivity(intent);
            }
        });


        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChildHome.this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Delete Child");
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage("Are you sure you want to Delete this child?");
                //Confirm the delete
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DatabaseReference mFirebaseInstance = FirebaseDatabase.getInstance().getReference().child("Children");;
                        mFirebaseInstance.child(ChildID).removeValue();
                    }
                });
                // not confirm
                alertDialogBuilder.setNegativeButton("Cancel", null).show();

            }
        });




    }
}