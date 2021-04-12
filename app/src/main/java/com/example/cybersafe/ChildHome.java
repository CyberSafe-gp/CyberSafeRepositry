package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChildHome extends AppCompatActivity {
     public Button btn1,btn4,btn5,btn6;
//btn1 for buuly coment page transfer button
// btn4 for flag page button transfer button
// btn5 for edit page transfer button
// btn6 for delete child button



    String ChildID,userType1,userID;
    //child id is  for store the current child id
    //userType1 is for store what type of user the loged-in user is
    //userID is for storing the curren loged-in user id
   // Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get the current loged-in user
        if (user != null) {

            userID = user.getUid();
            //bring user id
            ChildID = getIntent().getStringExtra("Child_id");
            //take from the parent home page the selected child id
            userType1=getIntent().getStringExtra("userType");
            //store what type of loged-in user is
        } else {
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(ChildHome.this, ParentLogin.class);
            startActivity(in);
            //to go back for the parent home
        }
//
//        //Toolbar
//        ImageView back = (ImageView) findViewById(R.id.arrowIncomP3);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//
//            }
//        });






        //bundle=getIntent().getStringExtras();
        //if(bundle.getString("Child_id")!=null)
        //{ChildID=bundle.getString("Child_id");
    //}


        btn1 = findViewById(R.id.button8);
        //bully comment



        btn4=  findViewById(R.id.button16);
        //flag

        btn5=  findViewById(R.id.button12);
        //edit

//        btn6=  findViewById(R.id.button22);
//        //delete

        btn1.setOnClickListener(v -> {
            Intent intent =new Intent(ChildHome.this, BullyCommentMain.class);
            intent.putExtra("Child_id",ChildID);
            startActivity(intent);
        });





        btn4.setOnClickListener(v -> {
            Intent intent = new Intent(ChildHome.this,FlagMain.class);
            intent.putExtra("Child_id",ChildID);
            startActivity(intent);

        });


        btn5.setOnClickListener(v -> {
            Intent intent = new Intent(ChildHome.this,Edit_Child_Profile.class);
            intent.putExtra("Child_id",ChildID);
            startActivity(intent);

        });




//        btn6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            //code for deleteng the child
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChildHome.this);
//                // Setting Alert Dialog Title
//                alertDialogBuilder.setTitle("Delete Child");
//                // Setting Alert Dialog Message
//                alertDialogBuilder.setMessage("Are you sure you want to Delete this child?");
//                //Confirm the delete
//                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        DatabaseReference mFirebaseInstance = FirebaseDatabase.getInstance().getReference().child("Children");
//                        DatabaseReference mFirebaseInstance2 = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
//                        mFirebaseInstance2.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot childrf : snapshot.getChildren()) {
//                                    SMAccountCredentials findSMA = childrf.getValue(SMAccountCredentials.class);
//                                    if(findSMA.getChild_id().equals(ChildID)){
//                                        Toast.makeText(ChildHome.this, "Deleted successfully", Toast.LENGTH_LONG).show();
//                                        String SMAId=findSMA.getId();
//                                        mFirebaseInstance2.child(SMAId).removeValue();
//                                        mFirebaseInstance.child(ChildID).removeValue();
//                                        finish();
//                                    }
//
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                });
//                // not confirm
//                alertDialogBuilder.setNegativeButton("Cancel", null).show();
//
//            }
//        });
//
//
//
//
//    }
}}