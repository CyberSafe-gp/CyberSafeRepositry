package com.example.cybersafe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EditSchool extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school);
        String intentName = getIntent().getStringExtra("IntentName");
    }
}