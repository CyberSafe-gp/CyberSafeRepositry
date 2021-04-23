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

public class Forgetpassword extends AppCompatActivity {
/// for the button, //for the email written
    private EditText emailEditText;
    private Button resetPasswordB;
    FirebaseAuth Auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        //bring the id's of the button,edit text
        emailEditText = (EditText) findViewById(R.id.editTextTextEmailAddress2);

        //bring the id's of the button
        resetPasswordB =(Button) findViewById(R.id.button10);

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
            emailEditText.setError("Enter a valid email");
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
                    Toast.makeText(Forgetpassword.this, "go to your email to reset your password",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(Forgetpassword.this,"the email is not regesterd",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
