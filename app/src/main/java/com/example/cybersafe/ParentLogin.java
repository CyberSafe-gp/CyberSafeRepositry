package com.example.cybersafe;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.util.Calendar;

public class ParentLogin extends AppCompatActivity {

    private Button log;
   private TextView forgetPass;
    private FirebaseAuth Auth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    public static String LoggedIn_User_Email;
    public String userTypee;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);


        userTypee=getIntent().getStringExtra("userType");
        //get and store the type of user loged-in
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference("SchoolManagers");
        Auth = FirebaseAuth.getInstance();
        log = (Button) findViewById(R.id.button9);
        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        forgetPass  = (TextView) findViewById(R.id.buttonForget);
        forgetPass.setOnClickListener(v -> {
            Intent intent =new Intent(ParentLogin.this,Forgetpassword.class);
            intent.putExtra("userType",userTypee);
            startActivity(intent);

        });



        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
    }
//the log-in function and validate the input
    private void login(){
        final String Email = editTextEmail.getText().toString().trim();
         LoggedIn_User_Email = Email;
        OneSignal.sendTag("User_ID",LoggedIn_User_Email);
        String password = editTextPassword.getText().toString().trim();

        if(Email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            editTextPassword.setError("Min password length should be 8 characters");
            editTextPassword.requestFocus();
            return;
        }


        Auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    if (userTypee.equals("Parent")) {
                        System.out.println("userTypee Parent"+userTypee);
                        Intent intent = new Intent(ParentLogin.this, ParentHome_New.class);
                        intent.putExtra("userType", userTypee);

                        //Start the service every one hour
                        //startService(new Intent(ParentLogin.this, MyService.class));

                    /*    Intent serviceIntent = new Intent(ParentLogin.this, MyService.class);
                        startService(serviceIntent);*/
                        //Start the service every one hour

                        startService(new Intent(ParentLogin.this, MyService.class));
                        Calendar cal = Calendar.getInstance();
                        Intent intent2 = new Intent(ParentLogin.this, MyService.class);
                        PendingIntent pintent = PendingIntent
                                .getService(ParentLogin.this, 0, intent2, 0);

                        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        // Start service every hour
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                300000, pintent);


                        startActivity(intent);
                        editTextEmail.getText().clear();
                        editTextPassword.getText().clear();

                    } else{
                        System.out.println("userTypee SM "+userTypee);
                        Intent intent = new Intent(ParentLogin.this, SchoolHome_new.class);
                        intent.putExtra("userType", userTypee);
                        startActivity(intent);
                        editTextEmail.getText().clear();
                        editTextPassword.getText().clear();
                }




                }
                else
                    Toast.makeText(ParentLogin.this, "failed to login", Toast.LENGTH_LONG).show();
                }
        });
    }


}