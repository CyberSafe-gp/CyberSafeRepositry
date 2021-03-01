package com.example.cybersafe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class resetpassword extends AppCompatActivity {
String userTypee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        userTypee=getIntent().getStringExtra("userType");

    }
}