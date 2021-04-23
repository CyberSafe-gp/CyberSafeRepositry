package com.example.cybersafe;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {
  //for the email written
    private EditText emailEditText;
    // for the button
    private Button resetPasswordB;
    //bring the current user loged-in
    FirebaseAuth Auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
     //bring the id's of the button,and the editText

        emailEditText = (EditText) findViewById(R.id.editTextTextEmailAddress22);
        resetPasswordB =(Button) findViewById(R.id.button15);

     //bring the current user loged-in
        Auth= FirebaseAuth.getInstance();

        // if the user clicked the button call the resetpassword method
        resetPasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });
    }

    private void resetpassword(){
        //bring the email that the user written
        String email= emailEditText.getText().toString().trim();
//chick pattern
        if(email.isEmpty()){
            emailEditText.setError(" Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }
        //in here we will send the user an email to reset his/her password
        Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(resetpassword.this, "go to your email to reset your password",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(resetpassword.this,"the email is not registered",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}