package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Interface extends AppCompatActivity {
    Button parentB;
    TextView schoolB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Parent-Button
        parentB= findViewById(R.id.parentButton);

        parentB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)  {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(Interface.this,Login_Register.class);
                intent.putExtra("userType", "Parent");
                startActivity(intent);
            }
        });


        //School-Button
       schoolB= (TextView) findViewById(R.id.schoolButton);


        schoolB.setOnClickListener(v -> {
            Intent intent =new Intent(Interface.this,Login_Register.class);
            intent.putExtra("userType","SchoolManager");
            startActivity(intent);

            finish();


        });

    }

}