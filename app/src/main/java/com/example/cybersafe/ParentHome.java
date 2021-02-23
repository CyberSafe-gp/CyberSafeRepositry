package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParentHome extends AppCompatActivity {


    public Button  btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);



        btn1= findViewById(R.id.button3);
        //add new child
        btn2=findViewById(R.id.button4);
        //detection keyword
        btn3=findViewById(R.id.button14);
        //edit
        btn4=findViewById(R.id.button5);
        //log-out

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(ParentHome.this,Add_NewChild.class);
                intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(ParentHome.this,Add_Detection_Keyword.class);
                intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
        });





        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(ParentHome.this, EditSchool.class);
                intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
        });
        //log-out
        //btn4.setOnClickListener(new View.OnClickListener() {
        //@Override
        // public void onClick(View v) {

        //startActivities();
        //}

        //private void startActivities() {

        //Intent intent = new Intent(SchoolHome.this, .class);
        //intent.putExtra("IntentName", "hi");
        //startActivity(intent);
        //}
        // });

    }
}
