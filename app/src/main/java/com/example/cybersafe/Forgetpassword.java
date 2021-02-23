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
import com.google.firebase.auth.FirebaseAuth;

public class Forgetpassword extends AppCompatActivity {
    private TextView cancelB;
    private EditText emailEditText;
    private Button resetpasswordB;
    FirebaseAuth Auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        //cancelB= (TextView) findViewById(R.id.);
/*        cancelB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(Forgetpassword.this, MainActivity.class));
            }
        });*/
        emailEditText = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        resetpasswordB =(Button) findViewById(R.id.button10);

        Auth= FirebaseAuth.getInstance();

        resetpasswordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });
    }
    private void resetpassword(){
        String email= emailEditText.getText().toString().trim();

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
