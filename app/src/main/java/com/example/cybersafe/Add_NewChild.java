package com.example.cybersafe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.example.cybersafe.Objects.School;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;

public class Add_NewChild extends AppCompatActivity {



    DatabaseReference schoolRef , ChildRef , SMARef,schoolManagerRef;
    private Spinner genderSpinner, citySpinner,schoolSpinner,gradeSpinner;
    private String gender[],city[],Grade[];
    private String userCity, userGender,userGrade,userschool,parentid,school_id,date ;
    private TextView oom;
    private Button date_picker;
    private DatePickerDialog datePickerDialog;
    //start for social media
    private EditText firstnameCH,lastnameCH;
    private FirebaseUser user;
    private Button add ;
    //TextView setSchoolManager;
    ImageView setSchoolManager;
    LocalDate birthDate;
    public boolean find=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_child);
        initDatePicker();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            parentid = user.getUid().trim();
        } else {

            // No user is signed in
        }
        oom= (TextView) findViewById(R.id.oo);
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        schoolManagerRef= FirebaseDatabase.getInstance().getReference().child("SchoolManagers");
        firstnameCH = (EditText) findViewById((R.id.firstnameCH));
        lastnameCH = (EditText) findViewById((R.id.lastnameCH));

        setSchoolManager =findViewById((R.id.setSchoolManager));

        date_picker = findViewById(R.id.date_picker);
        date_picker.setText(makeDateString(0,0,0));


       // grade dropdown menu
        gradeSpinner = (Spinner)findViewById(R.id.Grade);
        Grade = new String[] {"Select","1", "2","3", "4","5", "6","7", "8","9"};

        ArrayAdapter<String> GradeAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, Grade);
        GradeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gradeSpinner.setAdapter(GradeAdapter);


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
                        userGrade="Select";

                        break;
                    case 1:
                        userGrade="1";
                        break;

                    case 2:
                        userGrade="2";

                        break;
                    case 3:
                        userGrade="3";

                        break;
                    case 4:
                        userGrade="4";

                        break;
                    case 5:
                        userGrade="5";

                        break;
                    case 6:
                        userGrade="6";

                        break;
                    case 7:
                        userGrade="7";

                        break;
                    case 8:
                        userGrade="8";

                        break;
                    case 9:
                        userGrade="9";

                        break;



                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });



//        //Gender dropdown menu
        genderSpinner = (Spinner)findViewById(R.id.gender);
        gender = new String[] {"Select","Male", "Female"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(genderAdapter);



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
                        userGender="Select";
                        break;

                    case 1:
                        userGender="Male";
                        break;

                    case 2:
                        userGender="Female";

                        break;


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });


        //City dropdown menu
        citySpinner = (Spinner)findViewById(R.id.City);
        city = new String[] {"Select","Riyadh", "Jeddah", "Dammam"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(cityAdapter);

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
                schoolSpinner= (Spinner)findViewById(R.id.School);
                ArrayList<String> schoolList = new ArrayList<>();
                if(userCity.equals("Select")) {
                    schoolList.add("Select city first");
                }
                final ArrayAdapter schooladapter = new ArrayAdapter<String>( Add_NewChild.this, android.R.layout.simple_spinner_item, schoolList);
                schoolSpinner.setAdapter(schooladapter);



                    schoolRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.exists()) {
                                //Check if user select city
                                if(!userCity.equals("Select")){
                                schoolList.clear();
                                schoolList.add("Select");
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    School findSchool = postSnapshot.getValue(School.class);
                                    if (findSchool.getCity().equals(userCity))
                                        schoolList.add(findSchool.getSchoolName());
                                }}
                                schooladapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    //Get the user select for School
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
                                        if (x.equalsIgnoreCase(userschool)) {
                                            school_id = chapterObj.getSchool_id();

                                            //Check if the school manager register
                                            schoolManagerRef.addValueEventListener(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                           find=false;
                                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                        SchoolManager findSM = postSnapshot.getValue(SchoolManager.class);

                                                        String findschoolid = findSM.getSchool_id();
                                                        if (school_id.equalsIgnoreCase(findschoolid)) {
                                                            find = true;
                                                            break;
                                                        }
                                                    }
                                                    if (find == true) {

                                                        //
                                                        setSchoolManager.setVisibility(View.VISIBLE);

                                                    }else{

                                                        setSchoolManager.setVisibility(View.INVISIBLE);
                                                        oom.setText("School Manager is not registered");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            break;
                                        }


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

                        }
                    });



            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });





             add = (Button) findViewById(R.id.add1);

             add.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

            saveChild();
            }
        });



    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveChild() {
        //FirebaseUser currentUser = mAuth.getCurrentUser();


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

        // check data of birth is not empty
        if (date == null) {
            Toast.makeText(Add_NewChild.this, "Please select Date of birth", Toast.LENGTH_LONG).show();
            return;
        }
        // check gender is not empty
        if (userGender.equals("Select") ) {
            Toast.makeText(Add_NewChild.this, "Please select gender", Toast.LENGTH_LONG).show();
            return;
        }

        // check city is not empty
        if (userCity.equals("Select") ) {
            Toast.makeText(Add_NewChild.this, "Please select city", Toast.LENGTH_LONG).show();
            return;
        }
        // check grade is not empty
        if ( userGrade.equals("Select")) {
            Toast.makeText(Add_NewChild.this, "Please select grade", Toast.LENGTH_LONG).show();
            return;
        }
        // check school is not empty
        if (userschool.equals("Select")) {
            Toast.makeText(Add_NewChild.this, "Please select school", Toast.LENGTH_LONG).show();
            return;
        }


        int grade = Integer.parseInt(userGrade);



        //Go to social media credential


        String firstName=firstnameCH.getText().toString();
        String lastName=lastnameCH.getText().toString();



          //Get the social media credintals information to add it to the data base

        String Author_id = getIntent().getStringExtra("Author_id");
        String childAccount = getIntent().getStringExtra("childAccount");
        String SMA_id = getIntent().getStringExtra("SMA_id");
        String access_token = getIntent().getStringExtra("access_token");

        //}
       String child_id = ChildRef.push().getKey();


         Child Childobj=new Child(child_id, parentid, school_id, firstName,lastName, date, userCity,userGender,Integer.parseInt(userGrade) );





        //Add child to database
        ChildRef.child(child_id).setValue(Childobj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {



                    SMAccountCredentials SMAobj=new SMAccountCredentials(SMA_id ,child_id,"TikTok",childAccount,access_token,Author_id);



                    SMARef.child(SMA_id).setValue(SMAobj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //SMAccountCredentials added successfully
                            } else {
                                //SMAccountCredentials doesn't added
                            }

                        }
                    });

                    Toast.makeText(Add_NewChild.this, "Child added successfully", Toast.LENGTH_LONG).show();

                    System.out.println("#######");
/*
                    Fragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("userType", "Parent");
                    fragment.setArguments(bundle);
                    System.out.println("111111");
                    fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).commit();
*/

                    Intent intent = new Intent(Add_NewChild.this,ParentHome_New.class);
                    intent.putExtra( "open", "Home");
                    intent.putExtra("userType","Parent");
                    System.out.println("111111");
                    startActivity(intent);
                } else {
                    Toast.makeText(Add_NewChild.this, "Child doesn't added", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return LocalDate.of(year, month, month);
    }

    private String makeDateString(int day, int month, int year) {
        if (day == 0 && month == 0 && year == 0 )
        return getMonthFormat(0) + "" + "" ;

        return getMonthFormat(month) + "/" + day + "/" + year;

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
        if(month == 0)
            return "";

        //default should never happen
        return "JAN";
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()

        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                date = makeDateString(day, month, year);
                birthDate = LocalDate.of(year, month, day);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }


}