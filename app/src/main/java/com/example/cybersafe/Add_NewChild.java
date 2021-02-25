package com.example.cybersafe;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Add_NewChild extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener  {

    private FirebaseAuth mAuth;
    private EditText firstName;
    private EditText lastName;

    Spinner city;
    Spinner grade;
    Spinner gender;
    TextView Submit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_child);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}