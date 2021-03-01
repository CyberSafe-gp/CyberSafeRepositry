package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Detection_Keyword extends AppCompatActivity {
     public Button dK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__detection__keyword);

        //Add-Detection-Keyword-Button

        dK= findViewById(R.id.addDetectionK);

        dK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent inn = new Intent(Add_Detection_Keyword.this,Add_Detection_Keyword2.class);
                startActivity(inn);
            }
        });

    }
}