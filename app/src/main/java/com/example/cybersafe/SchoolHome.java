package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SchoolHome extends AppCompatActivity {
    public Button btn11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home);

        btn11 = (Button) findViewById(R.id.btn1);

        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivities();
            }

            private void startActivities() {

                Intent intent = new Intent(SchoolHome.this, EditSchool.class);
                intent.putExtra("IntentName", "hi");
                startActivity(intent);
            }
            });





    }
}



