package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.School;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;


public class SchoolManagerRegister extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    DatabaseReference schoolManagerRef2;
    private FirebaseAuth mAuth;
    private EditText firstName;
    private EditText lastName;
    private EditText password ,confirmPass;
    private EditText email;
    private String schoolname;
    private Spinner spinnerschoolname,citySpinner;
    private Button Submit;
    private TextView upi;
    private DatabaseReference schoolRef;
    private String  school_id;
    private String city[],userCity;
    public boolean find=false;
    ImageView setManager;
    Boolean checkSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_manager_register);

        mAuth = FirebaseAuth.getInstance();
        firstName = (EditText) findViewById((R.id.firstname));
        lastName = (EditText) findViewById((R.id.lastname));
        citySpinner = findViewById(R.id.City);
        email = (EditText) findViewById((R.id.email));
        password = (EditText) findViewById((R.id.password));
        schoolRef = FirebaseDatabase.getInstance().getReference().child("Schools");
        confirmPass= (EditText) findViewById((R.id.confirmpass));
        upi= (TextView) findViewById((R.id.upi));
        Submit=findViewById((R.id.submit_area));


        //City dropdown menu
        citySpinner = (Spinner) findViewById(R.id.City);
        city = new String[]{"Select", "Riyadh", "Jeddah", "Dammam"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(SchoolManagerRegister.this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(cityAdapter);


        //Get the user input for the city and set the School dropdown menu
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id) {
                int loc;
                loc = pos;

                switch (loc) {
                    case 0:
                        userCity = "Select";
                        break;

                    case 1:
                        userCity = "Riyadh";
                        break;

                    case 2:
                        userCity = "Jeddah";

                        break;
                    case 3:
                        userCity = "Dammam";
                        break;
                }


                //Set school dropdown menu
                spinnerschoolname = findViewById(R.id.spinnerschoolname);
                ArrayList<String> schoolList = new ArrayList<>();
                if (userCity.equals("Select")) {
                    schoolList.add("Select city first");
                }
                final ArrayAdapter schooladapter2 = new ArrayAdapter<String>(SchoolManagerRegister.this, android.R.layout.simple_spinner_item, schoolList);
                spinnerschoolname.setAdapter(schooladapter2);

                schoolRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            //Check if user select city
                            if (!userCity.equals("Select")) {
                                schoolList.clear();
                                schoolList.add("Select");
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    School findSchool = postSnapshot.getValue(School.class);
                                    if (findSchool.getCity().equals(userCity))
                                        schoolList.add(findSchool.getSchoolName());
                                }
                            }
                            schooladapter2.notifyDataSetChanged();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                //Get the user select for School
                spinnerschoolname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        schoolname = parent.getItemAtPosition(position).toString();
                        schoolRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    School chapterObj = postSnapshot.getValue(School.class);

                                    String x = chapterObj.getSchoolName();
                                    if (x.equalsIgnoreCase(schoolname)) {
                                        school_id = chapterObj.getSchool_id();
                                        setManager =(ImageView) findViewById(R.id.setManager);

                                        //Check if the school manager register
                                        schoolManagerRef2= FirebaseDatabase.getInstance().getReference().child("SchoolManagers");

                                        schoolManagerRef2.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                find = false;
                                                upi.setText(" ");
                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    SchoolManager findSM = postSnapshot.getValue(SchoolManager.class);

                                                    String findschoolid = findSM.getSchool_id();
                                                    if (school_id.equalsIgnoreCase(findschoolid)) {
                                                        find = true;
                                                        break;
                                                    }
                                                }
                                                if (find == true) {
                                                    setManager.setVisibility(View.INVISIBLE);
                                                    upi.setText("This School is already registered");
                                                    checkSchool=true;
                                                } else {
                                                    setManager.setVisibility(View.VISIBLE);
                                                    upi.setText(" ");
                                                    checkSchool=false;
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        break;
                                    }


                                }
                                schooladapter2.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg1) {

            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.submit_area:
                      registration();
                        break;


                }
            }

        });
    }


    private void registration() {
        final String firstname1 = firstName.getText().toString().trim();
        final String lastname1 = lastName.getText().toString().trim();
        //final String City;
        final String email1 = email.getText().toString().trim();
        final String password1 = password.getText().toString().trim();
        final String confirmPass1= confirmPass.getText().toString().trim();


        if(checkSchool){
            Toast.makeText(SchoolManagerRegister.this, "This School is already registered", Toast.LENGTH_LONG).show();
            return;
        }

        if (firstname1.isEmpty()) {
            firstName.setError("First name is required");
            firstName.requestFocus();
            return;
        }
        if (lastname1.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return;
        }
        if (email1.isEmpty()) {
            email.setError(" Email is required");
            email.requestFocus();
            return;
        }
        // check city is not empty
        if (userCity.equals("Select") ) {
            Toast.makeText(SchoolManagerRegister.this, "Please select city", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }
        if (password1.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (password1.length() < 8) {
            password.setError("Min password length should be 8 characters!");
            password.requestFocus();
            return;
        }

        if (!passwordValidation(password1)) {
            password.setError("Password should contains at least one capital letter, one small letter and one number");
            password.requestFocus();
            return;
        }
        if (confirmPass1.isEmpty()){
            confirmPass.setError("Confirm Password is required");
            confirmPass.requestFocus();
            return;
        }
        if (!confirmPass1.isEmpty()){
            if (!confirmPass1.equals(password1)) {
                confirmPass.setError("Password do not match");
                confirmPass.requestFocus();
                return;
            }}


        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if (task.isSuccessful()) {
                                                   String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                   String token = FirebaseInstanceId.getInstance().getToken();
                                                   SchoolManager USER = new SchoolManager(id,school_id,userCity,firstname1,lastname1,email1,"Not confirm",token);
                                                   FirebaseDatabase.getInstance().getReference("SchoolManagers")
                                                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                           .setValue(USER).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               Toast.makeText(SchoolManagerRegister.this, "SchoolManager registered Successfully ", Toast.LENGTH_LONG).show();
                                                               Intent intent = new Intent(SchoolManagerRegister.this, Admin_School.class);
                                                               intent.putExtra("userType", "SchoolManager");
                                                               startActivity(intent);


                                                           } else {
                                                               Toast.makeText(SchoolManagerRegister.this, "Registration failed ", Toast.LENGTH_LONG).show();

                                                           }
                                                       }
                                                   });
                                               } else {
                                                   Toast.makeText(SchoolManagerRegister.this, "this email is Already taken", Toast.LENGTH_LONG).show();

                                               }

                                           }
                                       }
                );
    }

    private boolean checkSchool(){
        final boolean[] flag = {false};
        schoolManagerRef2.orderByChild("school_id").equalTo(school_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    flag[0] = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flag[0];
    }

    private boolean passwordValidation(String password) {
        boolean CH = false;
        boolean ch = false;
        boolean num = false;

        for(int i = 0 ; i < password.length() ; i++ ){

            if(Character.isUpperCase(password.charAt(i))){
                CH = true;
            }
            if(Character.isLowerCase(password.charAt(i))){
                ch = true;
            }
            if(Character.isDigit(password.charAt(i))){
                num = true;
            }
        }
        return CH&&ch&&num;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {

    }

}
