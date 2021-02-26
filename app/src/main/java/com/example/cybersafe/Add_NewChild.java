package com.example.cybersafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.School;
import com.example.cybersafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Add_NewChild extends AppCompatActivity {
    ArrayList<String> schoolList = new ArrayList<>();
    DatabaseReference schoolRef;
    private Spinner genderSpinner, citySpinner,schoolSpinner,gradeSpinner;
    private String gender[],city[],Grade[];
    private String userCity, userGender,userGrade;
    private Button date_picker;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_child);
        initDatePicker();
        date_picker = findViewById(R.id.date_picker);
        date_picker.setText(getTodaysDate());
        //grade dropdown menu
        gradeSpinner = (Spinner)findViewById(R.id.Grade);
        Grade = new String[] {"1", "2","3", "4","5", "6","7", "8","9"};

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




        //Gender dropdown menu
        genderSpinner = (Spinner)findViewById(R.id.gender);
        gender = new String[] {"Male", "Female"};

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
        citySpinner = (Spinner)findViewById(R.id.City);
        city = new String[] {"Riyadh", "Jeddah", "Dammam"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(cityAdapter);
        //Log.i("AAA","spinner0");

        schoolRef = FirebaseDatabase.getInstance().getReference().child("Schools");

        //Get the user input for the city and set the School dropdown menu
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view1, int pos, long id)
            {
                int loc;
                loc = pos;

                switch (loc)
                {
                    case 0:
                        userCity="Riyadh";
                        break;

                    case 1:
                        userCity="Jeddah";

                        break;
                    case 2:
                        userCity="Dammam";
                        break;
                }

                schoolRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            schoolList.clear();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                School findSchool = postSnapshot.getValue(School.class);
                                if (findSchool.getCity().equals(userCity))
                                    schoolList.add(findSchool.getSchoolName());//schoolList.add(postSnapshot.getValue(School.class));
                            }

                            //School dropdown menu
                            schoolSpinner = (Spinner) findViewById(R.id.School);
                            ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, schoolList);
                            schoolAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            //schoolAdapter.notifyDataSetChanged();
                            schoolSpinner.setAdapter(schoolAdapter);
                            //schoolAdapter.notifyDataSetChanged();

                        }

                    }















                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg1)
            {

            }
        });





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
                String date = makeDateString(day, month, year);
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