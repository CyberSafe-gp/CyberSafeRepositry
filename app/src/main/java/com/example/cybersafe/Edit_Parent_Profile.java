package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Parent;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class Edit_Parent_Profile extends AppCompatActivity {

    private String userId;



    private FirebaseUser Cuser;

    private static final String TAG = Edit_Parent_Profile.class.getSimpleName();
    private Button btEdit , changeP;
    private DatabaseReference g =  FirebaseDatabase.getInstance().getReference("Parents");

    private EditText editTextfName;
    private EditText editTextlName;
    private EditText editTextEmail;
    String userEmail,Fname,lName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__parent__profile);



        editTextfName = findViewById(R.id.firstNameB);
        editTextlName = findViewById(R.id.lastNameB);
        editTextEmail = findViewById(R.id.EditEmailAddressB);
        btEdit=(Button)findViewById(R.id.editButton);
        changeP=(Button)findViewById(R.id.changePass);


        //changePass-button
        changeP.setOnClickListener(v -> {
            Intent intentt = new Intent(Edit_Parent_Profile.this,resetpassword.class);
            //to go for the reset passowrd page
            startActivity(intentt);
        });


        //bring current user
        Cuser = FirebaseAuth.getInstance().getCurrentUser();

//if
        g.addValueEventListener(new ValueEventListener() {
            @Override
            //bringing all the current user information in the data base and view it in the edit page
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.cybersafe.Objects.Parent us = postSnapshot.getValue(Parent.class);
                    if (us.getParent_id().equals(Cuser.getUid())) {
                        userEmail = us.getEmail();
                        userId = us.getParent_id();
                        Fname=us.getFirstName();
                        lName = us.getLastName();
                        userId = us.getParent_id();
                        //write the parent information in the edit page
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
    //here we are changing the user information and valdate it
            private void updateInfo(View view) {


                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Parents").child(userId);
//the user input
                final String fname = editTextfName.getText().toString().trim();
                final String lastname = editTextlName.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();

//input validation
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
//store it in the database for the current user
                String token = FirebaseInstanceId.getInstance().getToken();
                Parent p = new Parent(fname, lastname, email, userId,token);
                userRef.setValue(p);
                Snackbar.make(view, "Information has been updated successfully", Snackbar.LENGTH_LONG).setDuration(30000).show();


            }
}





