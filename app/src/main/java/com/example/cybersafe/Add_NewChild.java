package com.example.cybersafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.School;
import com.example.cybersafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Add_NewChild extends AppCompatActivity {
    ArrayList<School> schoolList = new ArrayList<>();
    DatabaseReference schoolRef;
    private Spinner genderSpinner, citySpinner;
    private String gender[],city[];

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
        //Log.i("AAA","spinner0");



        //City dropdown menu
        citySpinner = (Spinner)findViewById(R.id.City);
        city = new String[] {"Riyadh", "Jeddah", "Dammam"};

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(Add_NewChild.this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(cityAdapter);
        //Log.i("AAA","spinner0");



/*        schoolRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    schoolList.add(postSnapshot.getValue(School.class));
                }
                loadDataInSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/




    }
}