package com.example.cybersafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class Add_NewChild extends AppCompatActivity {
    ArrayList<String> schoolList = new ArrayList<>();
    DatabaseReference schoolRef;
    private Spinner genderSpinner, citySpinner,schoolSpinner;
    private String gender[],city[];
    private String userCity, userGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_child);


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
}