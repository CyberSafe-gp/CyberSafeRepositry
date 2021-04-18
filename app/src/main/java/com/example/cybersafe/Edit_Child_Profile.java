package com.example.cybersafe;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

public class Edit_Child_Profile extends AppCompatActivity {



    DatabaseReference schoolRef , ChildRef , SMARef,schoolManagerRef;
    private Spinner genderSpinner, citySpinner,schoolSpinner,gradeSpinner,Applications;
    private String gender[],city[],Grade[];

    private String userCity, userGender,userGrade,userschool,parentid,school_id,date, schoolName ;
    private String apps;
    private Button date_picker;
    private DatePickerDialog datePickerDialog;
    private FirebaseAuth mAuth;

    //start for social media
//    private EditText username;
//    private EditText password ;
    private FirebaseUser user;
    TextView setSchoolManager;
    LocalDate birthDate;
    boolean find=false;
    final SMAccountCredentials SMAobj=new SMAccountCredentials();

    //shahad
    private EditText firstnameCH,lastnameCH;
    private Button add,deletB ;
    private String userIdC,userIdP,userIdS,Fname,lName,DOB,Cityy,Genderr,childID;
    private String DOB1;
    private  int Gradee;
    private FirebaseUser Cuser;
    private static final String TAG = Edit_Child_Profile.class.getSimpleName();
    String usernameD,passwordD, SMAId;
    ImageView setSchoolManager1;

    private DatabaseReference g =  FirebaseDatabase.getInstance().getReference("Children");

//    public Add_NewChild() {}
//current user id
    // فالديت للعمر والقريد


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__child__profile);
        initDatePicker();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            parentid = user.getUid().trim();
            childID = getIntent().getStringExtra("Child_id");
        } else {

            // No user is signed in
        }


        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        schoolManagerRef= FirebaseDatabase.getInstance().getReference().child("SchoolManagers");

        //SHAHAD
        firstnameCH = (EditText) findViewById((R.id.firstnameCH1));
        lastnameCH = (EditText) findViewById((R.id.lastnameCH1));

       // username = (EditText) findViewById((R.id.username1));
       // password = (EditText) findViewById((R.id.password1));
       // setSchoolManager = (TextView) findViewById((R.id.setSchoolManager)); انا لينه حذفتها لانه كانت تسبب لي ايرور
        setSchoolManager1 =findViewById((R.id.setSchoolManager1));
        date_picker = findViewById(R.id.date_picker1);

        //delete
        deletB=  findViewById(R.id.buttonx);


  deletB.setOnClickListener(new View.OnClickListener() {
            @Override
            //code for deleteng the child
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Edit_Child_Profile.this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Delete Child");
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage("Are you sure you want to Delete this child?");
                //Confirm the delete
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DatabaseReference mFirebaseInstance = FirebaseDatabase.getInstance().getReference().child("Children");
                        DatabaseReference mFirebaseInstance2 = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
                        mFirebaseInstance2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childrf : snapshot.getChildren()) {
                                    SMAccountCredentials findSMA = childrf.getValue(SMAccountCredentials.class);
                                    if(findSMA.getChild_id().equals(childID)){
                                        Toast.makeText(Edit_Child_Profile.this, "Deleted successfully", Toast.LENGTH_LONG).show();
                                        String SMAId=findSMA.getId();
                                        mFirebaseInstance2.child(SMAId).removeValue();
                                        mFirebaseInstance.child(childID).removeValue();
                                        finish();
                                    }


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                // not confirm
                alertDialogBuilder.setNegativeButton("Cancel", null).show();

            }
        });

        


        Cuser = FirebaseAuth.getInstance().getCurrentUser();
        g.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.cybersafe.Objects.Child us = postSnapshot.getValue(Child.class);

                    if (us.getChild_id().equals(childID)) {
                        //userIdC = us.getChild_id();

                        userIdP = us.getParent_id();
                        userIdS = us.getSchool_id();
                        Fname=us.getFirstName();
                        lName = us.getLastName();
                        DOB=us.getDate_of_birth();
                        Cityy=us.getCity();
                        Genderr=us.getGender();
                        Gradee=us.getGrade();
                        firstnameCH.setText(Fname);
                        lastnameCH .setText(lName);


//System.out.println(DOB);
                       String[] ddatte = DOB.split("/");

                        date_picker.setText(makeDateString(getMonthFormat(ddatte[0]),Integer.parseInt(ddatte[1]),Integer.parseInt(ddatte[2])));

                        gradeSpinner = (Spinner)findViewById(R.id.Grade1);
                        Grade = new String[] {"1", "2","3", "4","5", "6","7", "8","9"};

                        // grade dropdown menu
                        ArrayAdapter<String> GradeAdapter = new ArrayAdapter<String>(Edit_Child_Profile.this, android.R.layout.simple_spinner_item, Grade);
                        GradeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        gradeSpinner.setAdapter(GradeAdapter);

                        //Set child grade
                        for (int i = 0; i < Grade.length; i++) {
                            String x = Gradee+"";
                            if (Grade[i].equals(x)) {
                                gradeSpinner.setSelection(i);
                            }
                        }

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

                        //        //Gender dropdown menu
                        genderSpinner = (Spinner)findViewById(R.id.gender1);
                        gender = new String[] {"Male", "Female"};

                        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(Edit_Child_Profile.this, android.R.layout.simple_spinner_item, gender);
                        genderAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        genderSpinner.setAdapter(genderAdapter);
                        genderSpinner.setPrompt(Genderr);

                        //Set child gender
                        for (int i = 0; i < gender.length; i++) {
                            if (gender[i].equals(Genderr)) {
                                genderSpinner.setSelection(i);
                            }
                        }

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


                        //City dropdown menu
                        citySpinner = (Spinner)findViewById(R.id.City1);
                        city = new String[] {"Select","Riyadh", "Jeddah", "Dammam"};

                        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(Edit_Child_Profile.this, android.R.layout.simple_spinner_item, city);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        citySpinner.setAdapter(cityAdapter);

                        //Set child city
                        for (int i = 0; i < city.length; i++) {
                            if (city[i].equals(Cityy)) {
                                citySpinner.setSelection(i);
                            }
                        }

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
                                schoolSpinner= (Spinner)findViewById(R.id.School1);
                                ArrayList<String> schoolList = new ArrayList<>();

                                final ArrayAdapter schooladapter = new ArrayAdapter<String>( Edit_Child_Profile.this, android.R.layout.simple_spinner_item, schoolList);
                                schoolSpinner.setAdapter(schooladapter);



                                schoolRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.exists()) {
                                            //Check if user select city
                                            if(!userCity.equals("Select")){
                                                schoolList.clear();
//                                                schoolList.add("Select");
//
//                                                schoolList.clear();
//                                                //schoolList.add("Select");
                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                    School findSchool = postSnapshot.getValue(School.class);
                                                    if (findSchool.getCity().equals(userCity))
                                                        schoolList.add(findSchool.getSchoolName());
                                                    if(findSchool.getSchool_id().equals(userIdS))
                                                        schoolName=findSchool.getSchoolName();

                                                }}
                                            schooladapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                //Set child School
                                for (int i = 0; i < schoolList.size(); i++) {
                                    if (schoolList.get(i).equals(schoolName)) {
                                        citySpinner.setSelection(i);
                                    }
                                }


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

                                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                                    find = false;
                                                                    SchoolManager findSM = postSnapshot.getValue(SchoolManager.class);

                                                                    String findschoolid = findSM.getSchool_id();
                                                                    if (school_id.equalsIgnoreCase(findschoolid)) {
                                                                        find = true;
                                                                        break;
                                                                    }
                                                                }
                                                                if (find == true) {
//                                                                    setSchoolManager.setTextColor(Color.GREEN);
//                                                                    setSchoolManager.setText("School Manager is registered");
                                                                    setSchoolManager1.setVisibility(View.VISIBLE);

                                                                }else{
//                                                                    setSchoolManager.setTextColor(Color.RED);
//                                                                    setSchoolManager.setText("School Manager is not registered");
                                                                    setSchoolManager1.setVisibility(View.INVISIBLE);
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




                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Social media account credentials
        Applications = findViewById(R.id.Applications1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.application, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Applications.setAdapter(adapter);

        Applications.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apps = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials smAccountCredentials = postSnapshot.getValue(SMAccountCredentials.class);
                    if (smAccountCredentials.getChild_id().equals(childID)) {
//                        usernameD =smAccountCredentials.getAccount();
//                        passwordD=smAccountCredentials.getPassword();
                        SMAId=smAccountCredentials.getId();
//                        username.setText(usernameD);
//                        password.setText(passwordD);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        add = (Button) findViewById(R.id.add11);

        add.setOnClickListener(new View.OnClickListener() {

            //                    final String username1= username.getText().toString().trim();
//                    final String password2= password.getText().toString().trim();
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                savechild();
            }
        });
     }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void savechild() {
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("childID "+childID);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Children").child(childID);

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

        // check data of birth is not empty
        if (DOB == null) {
            Toast.makeText(Edit_Child_Profile.this, "Please select Date of birth", Toast.LENGTH_LONG).show();
            return;
        }
        // check gender is not empty
        if (userGender.equals("Select") ) {
            Toast.makeText(Edit_Child_Profile.this, "Please select gender", Toast.LENGTH_LONG).show();
            return;
        }

        // check city is not empty
        if (userCity.equals("Select") ) {
            Toast.makeText(Edit_Child_Profile.this, "Please select city", Toast.LENGTH_LONG).show();
            return;
        }
        // check grade is not empty
        if ( userGrade.equals("Select")) {
            Toast.makeText(Edit_Child_Profile.this, "Please select grade", Toast.LENGTH_LONG).show();
            return;
        }
        // check school is not empty
        if (userschool.equals("Select")) {
            Toast.makeText(Edit_Child_Profile.this, "Please select school", Toast.LENGTH_LONG).show();
            return;
        }



        //Edit
        String firstName=firstnameCH.getText().toString().trim();
        String lastName=lastnameCH.getText().toString().trim();


        Child Childobj=new Child(childID, userIdP, userIdS, firstName,lastName, DOB, userCity,userGender,Integer.parseInt(userGrade) );
        //userRef.setValue(Childobj);


        //Edit child to database
        userRef.setValue(Childobj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

//                    usernameD =username.getText().toString();
//                    passwordD =password.getText().toString();
/*                    SMAccountCredentials SMAobj=new SMAccountCredentials(SMAId,childID,apps,passwordD,usernameD);

                    SMARef.child(SMAId).setValue(SMAobj);

                    Toast.makeText(Edit_Child_Profile.this, "Child updated successfully", Toast.LENGTH_LONG).show();*/
                    // startActivity(new Intent(creatnotepopup.this, ExplorerNote.class));
                    /*Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    */
                    finish();
                } else {
                   // Toast.makeText(Edit_Child_Profile.this, "Child doesn't updated", Toast.LENGTH_LONG).show();
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
        if(month == 0)
            return "";

        //default should never happen
        return "JAN";
    }

    private int getMonthFormat(String month) {
        if(month.equals("JAN"))
            return  1 ;
        if(month.equals("FEB"))
            return 2;
        if(month.equals("MAR"))
            return 3;
        if(month.equals("APR"))
            return 4;
        if(month.equals("MAY"))
            return 5;
        if(month.equals("JUN"))
            return 6;
        if(month.equals("JUL"))
            return 7;
        if(month.equals("AUG"))
            return 8;
        if(month.equals("SEP"))
            return 9;
        if(month.equals("OCT"))
            return 10;
        if(month.equals("NOV"))
            return 11;
        if(month.equals("DEC"))
            return 12;
        if(month.equals(""))
            return 0;

        //default should never happen
        return 1;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()

        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                DOB = makeDateString(day, month, year);
                birthDate = LocalDate.of(year, month, day);
                date_picker.setText(DOB);
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