package com.example.cybersafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Gethelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gethelp);
        TextView T=(TextView)findViewById(R.id.link);
        T.setMovementMethod(LinkMovementMethod.getInstance());
    }






}