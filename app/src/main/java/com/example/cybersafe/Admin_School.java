package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_School extends AppCompatActivity {
    DatabaseReference SchoolM = FirebaseDatabase.getInstance().getReference().child("SchoolManager");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userID = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__school);

SchoolM.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot childrf : snapshot.getChildren()) {
            SchoolManager SchoolM = childrf.getValue(SchoolManager.class);
            String user_id = SchoolM.getSchoolManager_id();
            if (user_id.equals(userID)) {
                if(SchoolM.getAdmin().equals("Confirm")){
                    Toast.makeText(Admin_School.this, "School Manager registered has been confirmed Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Admin_School.this,SchoolHomeFragment.class);
                    startActivity(intent);}
                    else{

                    Toast.makeText(Admin_School.this, "The school Manager registered has not confirmed successfully", Toast.LENGTH_LONG).show();
                }
                }
            else {

                ///
            }
        }


    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});




    }




}