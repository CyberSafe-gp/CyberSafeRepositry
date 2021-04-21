package com.example.cybersafe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login_Register extends AppCompatActivity {

    public Button log;
    public Button reg;
    public ImageButton bk;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__register);
        String user = getIntent().getStringExtra("userType");


        TextView setUserType = findViewById(R.id.userType);
        //userType
        if(user.equals("Parent")){
            setUserType.setText("Parent");
        }else if(user.equals("SchoolManager")){
            setUserType.setText("School Manager");
        }


     //Login-Button -- !!

      log=findViewById(R.id.loginButton);

      log.setOnClickListener((View v) -> {
          Intent intent = new Intent(Login_Register.this, Login.class);

          if (user.equals("Parent"))
          {
              intent.putExtra("userType", "Parent");
              startActivity(intent);
          }
          else {
              intent.putExtra("userType", "Schoolmanager");

              startActivity(intent);
          }

      });

        //Register-Button  -- !!

        reg=findViewById(R.id.registerButton);

        reg.setOnClickListener((View v) -> {


            if (user.equals("Parent")) {
                Intent intent2 = new Intent(Login_Register.this, ParentRegister.class);
                intent2.putExtra("userType", "Parent");

                startActivity(intent2);
                finish();
        }
            else {

                Intent intent3 = new Intent(Login_Register.this, SchoolManagerRegister.class);
                intent3.putExtra("userType", "SchoolManager");
                startActivity(intent3);
                finish();
            }
        });

        //Back-Button -image
//
//       bk =(ImageButton)findViewById(R.id.imageButton);
//       bk.setOnClickListener(v -> {
//           Intent intentt = new Intent(Login_Register.this,Interface.class);
//           startActivity(intentt);
//       });









    }
}