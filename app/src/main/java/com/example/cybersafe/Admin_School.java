package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.SchoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_School extends AppCompatActivity {

    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__school);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
             userID = user.getUid();
             System.out.println("userID "+userID);
        }else {
            Intent intent = new Intent(Admin_School.this, Interface.class);
            startActivity(intent);

        }

        DatabaseReference SchoolM = FirebaseDatabase.getInstance().getReference().child("SchoolManagers");
        SchoolM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childrf : snapshot.getChildren()) {
                        SchoolManager SchoolM = childrf.getValue(SchoolManager.class);
                        String user_id = SchoolM.getSchoolManager_id();

                        System.out.println("1111111");
                        if (user_id.equals(userID)) {
                            System.out.println("22222222");
                            System.out.println("@@@@@ " + SchoolM.getAdmin());
                            if (SchoolM.getAdmin().equals("Confirm")) {
                                System.out.println("333333333");

                                Toast.makeText(Admin_School.this, "School Manager registered has been confirmed Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Admin_School.this, SchoolHome_new.class);
                                startService(new Intent(Admin_School.this, ServiceSM.class));
                                intent.putExtra("userType","SchoolManager");
                                startActivity(intent);
                            } else {
                            }
                        } else {

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }




}