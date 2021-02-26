package com.example.cybersafe;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditSchool extends AppCompatActivity {
    private String userId;


    private FirebaseUser Cuser;

    private Button btEdit;
    private DatabaseReference g =  FirebaseDatabase.getInstance().getReference("Parents");

    private EditText editTextfName,editTextlName,editTextEmail;

    String userEmail,Fname,lName,Sid,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school);

            editTextfName = findViewById(R.id.firstname2);
            editTextlName = findViewById(R.id.lastname2);
        editTextEmail = findViewById(R.id.email2);
            btEdit=(Button)findViewById(R.id.editb);

            Cuser = FirebaseAuth.getInstance().getCurrentUser();
            System.out.println("############################################");
            System.out.println(Cuser.getEmail());

            g.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        com.example.cybersafe.Objects.SchoolManager us = postSnapshot.getValue(SchoolManager.class);
                        if (us.getSchoolManager_id().equals(Cuser.getUid())) {
                            userEmail = us.getEmail();
                            userId = us.getSchoolManager_id();
                            Fname=us.getFirstName();
                            lName = us.getLastName();
                            userId = us.getSchoolManager_id();
                            Sid=us.getSchool_id();
                            pass=us.getPassword();
                            editTextfName.setText(Fname);
                            editTextlName .setText(lName);
                            editTextEmail.setText(userEmail);

                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    updateInfo(view);
                }
            });
        }
        private void updateInfo(View view) {


            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("SchoolManagers").child(userId);

            final String fname = editTextfName.getText().toString().trim();
            final String lastname = editTextlName.getText().toString().trim();
            final String email = editTextEmail.getText().toString().trim();


            if (fname.isEmpty()) {
                editTextfName.setError("first name is required");
                editTextfName.requestFocus();

                return;
            }
            if (lastname.isEmpty()) {
                editTextlName.setError("last name name is required");
                editTextlName.requestFocus();
                return;
            }


            if (!fname.matches("[a-zA-Z]+")) {
                editTextfName.setError("first name must contains letter");
                editTextfName.requestFocus();

                return;
            }


            if (!lastname.matches("[a-zA-Z]+")) {
                editTextlName.setError("last name must contains letter");
                editTextlName.requestFocus();

                return;
            }

            if (email.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Please provide valid email");
                editTextEmail.requestFocus();
                return;
            }

            SchoolManager Smanger = new SchoolManager(userId,Sid,fname, lastname, pass,email);
            userRef.setValue(Smanger);
            Snackbar.make(view, "Information has been updated successfully", Snackbar.LENGTH_LONG).setDuration(30000).show();


        }
    }


