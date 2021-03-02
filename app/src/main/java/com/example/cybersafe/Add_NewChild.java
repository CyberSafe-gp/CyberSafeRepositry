package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.example.cybersafe.Objects.School;
import com.example.cybersafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Add_NewChild extends AppCompatActivity {



    DatabaseReference schoolRef , ChildRef , SMARef;
    private Spinner genderSpinner, citySpinner,schoolSpinner,gradeSpinner,Applications;
    private String gender[],city[],Grade[];
    private String userCity, userGender,userGrade,userschool,parentid,school_id,date ;
    private String apps;
    private Button date_picker;
    private DatePickerDialog datePickerDialog;
    private FirebaseAuth mAuth;
    //start for social media
    private EditText firstnameCH,lastnameCH;
    private EditText username;
    private EditText password;
    private FirebaseUser user;
    private Button add ;
    final SMAccountCredentials SMAobj=new SMAccountCredentials();
//    public Add_NewChild() {}
//current user id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_child);
        initDatePicker();
        System.out.print("onCreate 00000");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            parentid = user.getUid().trim();
        } else {
            System.out.println("onCreate no usserrr");
            // No user is signed in
        }
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        firstnameCH = (EditText) findViewById((R.id.firstnameCH));
        lastnameCH = (EditText) findViewById((R.id.lastnameCH));
        username = (EditText) findViewById((R.id.username));
        password = (EditText) findViewById((R.id.password));
        date_picker = findViewById(R.id.date_picker);
        date_picker.setText(getTodaysDate());
        System.out.println("onCreate 1111");

       // grade dropdown menu
        gradeSpinner = (Spinner)findViewById(R.id.Grade);
        Grade = new String[] {"1", "2","3", "4","5", "6","7", "8","9"};

        ArrayAdapter<String> GradeAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, Grade);
        GradeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gradeSpinner.setAdapter(GradeAdapter);
        System.out.println("onCreate 2222");

        //Get the user input for the gender
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id)
            {

                int loc;
                loc = pos;

                switch (loc)
                {
                    case 0:
                        userGrade="1";
                        break;

                    case 1:
                        userGrade="2";

                        break;
                    case 2:
                        userGrade="3";

                        break;
                    case 3:
                        userGrade="4";

                        break;
                    case 4:
                        userGrade="5";

                        break;
                    case 5:
                        userGrade="6";

                        break;
                    case 6:
                        userGrade="7";

                        break;
                    case 7:
                        userGrade="8";

                        break;
                    case 8:
                        userGrade="9";

                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });

        System.out.println("onCreate 3333");

//        //Gender dropdown menu
        genderSpinner = (Spinner)findViewById(R.id.gender);
        gender = new String[] {"Male", "Female"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(genderAdapter);
        System.out.println("onCreate 4444");

        //Get the user input for the gender
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id)
            {
                Log.i("AAA","OnItemSelected");
                int loc;
                loc = pos;

                switch (loc)
                {
                    case 0:
                        userGender="Male";
                        break;

                    case 1:
                        userGender="Female";

                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });
        System.out.println("onCreate 5555");

        //City dropdown menu
        citySpinner = (Spinner)findViewById(R.id.City);
        city = new String[] {"Riyadh", "Jeddah", "Dammam"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(cityAdapter);
        System.out.println("onCreate 66666");
//        //Log.i("AAA","spinner0");
//
        schoolRef = FirebaseDatabase.getInstance().getReference().child("Schools");

        //Get the user input for the city and set the School dropdown menu
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id) {
                int loc;
                loc = pos;

                switch (loc) {
                    case 0:
                        userCity = "Riyadh";
                        break;

                    case 1:
                        userCity = "Jeddah";

                        break;
                    case 2:
                        userCity = "Dammam";
                        break;
                }
                //School dropdown menu
                schoolSpinner= (Spinner)findViewById(R.id.School);
                ArrayList<String> schoolList = new ArrayList<>();
                final ArrayAdapter schooladapter = new ArrayAdapter<String>( Add_NewChild.this, android.R.layout.simple_spinner_item, schoolList);
                schoolSpinner.setAdapter(schooladapter);
////////////
                schoolRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            schoolList.clear();
                            schoolList.add("select");
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                School findSchool = postSnapshot.getValue(School.class);
                                 if (findSchool.getCity().equals(userCity))
                                schoolList.add(findSchool.getSchoolName());
                            }
                            schooladapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        userschool = parent.getItemAtPosition(position).toString();
                        schoolRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    School chapterObj = postSnapshot.getValue(School.class);

                                    String x = chapterObj.getSchoolName();
                                    if (x.equalsIgnoreCase(userschool))
                                        school_id = chapterObj.getSchool_id();
                                }
                                schooladapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }         });
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });
        System.out.println("onCreate 77777");


//   // from here for social media credentials
//        username = (EditText) findViewById((R.id.username));
//        password = (EditText) findViewById((R.id.password));
        System.out.println("onCreate 8888");
        Applications = findViewById(R.id.Applications);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.application, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Applications.setAdapter(adapter);
        System.out.println("onCreate 99999");
        Applications.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apps = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
       });
        System.out.println("onCreate 1010101");

             add = (Button) findViewById(R.id.add1);
        System.out.println("onCreate 11111111111");
             add.setOnClickListener(new View.OnClickListener() {

//                    final String username1= username.getText().toString().trim();
//                    final String password2= password.getText().toString().trim();
            @Override
            public void onClick(View v) {
            System.out.println("add onClick");
            savechild();
            }
        });

    }

    private void savechild() {
        //FirebaseUser currentUser = mAuth.getCurrentUser();

        System.out.println("add savechild");

        // check First name not empty
        if(firstnameCH.getText().toString().isEmpty()){
            firstnameCH.setError("First name is required");
            firstnameCH.requestFocus();
            return;
        }

        // check Last name not empty
        if(lastnameCH.getText().toString().isEmpty()){
            lastnameCH.setError("Last name is required");
            lastnameCH.requestFocus();
            return;
       }
//
//
        // check it is not select
        if (date_picker.equals("") || userGender.equals("") || userCity.equals("") || userGrade.equals("") || userschool.equals("") ) {
            Toast.makeText(Add_NewChild.this, "can't be added , please select date, gender,grade,city and school ", Toast.LENGTH_LONG).show();
        }
        // else if (currentUser != null) {


        //}
        String id2 = ChildRef.push().getKey();
        String firstName=firstnameCH.getText().toString();
        String lastName=lastnameCH.getText().toString();
        System.out.println("add 111");

         Child Childobj=new Child(id2, parentid, school_id, firstName,lastName, date, userCity,userGender,userGrade );

        System.out.println("add 222");

       /* Childobj.setDate_of_birth(date );
        Childobj.setGender(userGender);
        Childobj.setCity(userCity);
        Childobj.setGrade(userGrade);
        Childobj.setSchool_id(school_id);
        Childobj.setParent_id(parentid);
        Childobj.setFirstName(firstnameCH.getText().toString());
        Childobj.setLastName(lastnameCH.getText().toString());
        Childobj.setChild_id(id2);*/

        //Add child to database
        ChildRef.child(id2).setValue(Childobj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("onComplete 111");
                if (task.isSuccessful()) {
                    System.out.println("onComplete 222");

                    //social
                    String id = SMARef.push().getKey();
                    String usernameD =username.getText().toString();
                    String passwordD =password.getText().toString();
                    System.out.println("onComplete 333");

                    SMAccountCredentials SMAobj=new SMAccountCredentials(id,id2,apps,passwordD,usernameD);
                    System.out.println("onComplete 444");

                    /*SMAobj.setId(id);
                    SMAobj.setAccount(username.getText().toString());
                    SMAobj.setPassword(password.getText().toString());
                    SMAobj.setSocialMediaPlatform(apps);
                    SMAobj.setChild_id("");*/

                    SMARef.child(id).setValue(SMAobj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Add_NewChild.this, "SMAccountCredentials added successfully", Toast.LENGTH_LONG).show();
                                //             startActivity(new Intent(creatnotepopup.this, ExplorerNote.class));
                            /*    Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();*/
                            } else {
                                Toast.makeText(Add_NewChild.this, "SMAccountCredentials doesn't added", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    Toast.makeText(Add_NewChild.this, "Child added successfully", Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(creatnotepopup.this, ExplorerNote.class));
                    /*Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();*/
                } else {
                    Toast.makeText(Add_NewChild.this, "Child doesn't added", Toast.LENGTH_LONG).show();
                }

            }
        });
        System.out.println("add 333");

    }

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;

    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()

        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                date = makeDateString(day, month, year);
                date_picker.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }




}