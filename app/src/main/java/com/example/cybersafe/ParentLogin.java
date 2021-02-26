package com.example.cybersafe;


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

public class ParentLogin extends AppCompatActivity {

    private Button log;
    private FirebaseAuth Auth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    public static String LoggedIn_User_Email;
    private TextView forgetPass;
    public String userType;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        userType=getIntent().getStringExtra("userType");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference("SchoolManagers");
        Auth = FirebaseAuth.getInstance();
        forgetPass  = (TextView) findViewById(R.id.textView13);
        log = (Button) findViewById(R.id.button9);
        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParentLogin.this,Forgetpassword.class));
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
    }

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

                if(task.isSuccessful())
                    /*if (userType.equals("Parent")){
                   intent.putExtra("userType",userType);
                   startActivity(new Intent(ParentLogin.this,ParentHome.class));
                     */
                /*else
                intent.putExtra("userType",userType);
                startActivity(new Intent(ParentLogin.this,SchoolHome.class));
                 */
                    startActivity(new Intent(ParentLogin.this,ParentHome.class));


                else
                    Toast.makeText(ParentLogin.this, "failed to login", Toast.LENGTH_LONG).show();
                }
        });
    }


}