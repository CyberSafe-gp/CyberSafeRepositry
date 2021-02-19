package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ParentRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FirebaseAuth mAuth;
    private EditText firstname,lastname,email,password;
    private String number;
    Spinner spinner1;
    //Button Next;
    TextView Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        mAuth = FirebaseAuth.getInstance();
        firstname = (EditText) findViewById((R.id.firstname));
        lastname = (EditText) findViewById((R.id.lastname));
        email = (EditText) findViewById((R.id.email));
        password = (EditText) findViewById((R.id.password));
        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Children, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        Next = findViewById(R.id.Next);
        Next.setOnClickListener((View.OnClickListener) this);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                number = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Next:
                registration();
                break;
        }
    }

    private void registration() {
        final String firstname1= firstname.getText().toString().trim();
        final String lastname1= lastname.getText().toString().trim();
        final String email1= email.getText().toString().trim();
        final String password1= password.getText().toString().trim();
        final String number1= number.trim();
        
        System.out.print(firstname1);

        if(firstname1.isEmpty()){
            firstname.setError("First name is required");
            firstname.requestFocus();
            return;
        }
        if(lastname1.isEmpty()){
            lastname.setError("Last name is required");
            lastname.requestFocus();
            return;
        }
        if(email1.isEmpty()) {
            email.setError(" Email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }
        if(password1.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(password1.length()<8){
            password.setError("Min password length should be 8 characters!");
            password.requestFocus();
            return;
        }

        if(!passwordValidation(password1)){
            password.setError("Password should contains at least one capital letter, one small letter and one number");
            password.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if(task.isSuccessful()){
                                                   String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                   Parent USER = new Parent(firstname1,lastname1,email1,id);
                                                   FirebaseDatabase.getInstance().getReference("Parents")
                                                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                           .setValue(USER).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               Toast.makeText(ParentRegister.this, "Parent registered Successfully ", Toast.LENGTH_LONG).show();
                                                               startActivity(new Intent(ParentRegister.this, ParentRegister2.class));

                                                           }
                                                           else {
                                                               Toast.makeText(ParentRegister.this, "Registration failed ", Toast.LENGTH_LONG).show();

                                                           }
                                                       }
                                                   }  );
                                               }
                                               else {
                                                   Toast.makeText(ParentRegister.this, "this email is Already taken", Toast.LENGTH_LONG).show();

                                               }

                                           }
                                       }
                );
    }

    private boolean passwordValidation(String password) {
        boolean CH = false;
        boolean ch = false;
        boolean num = false;

        for(int i = 0 ; i < password.length() ; i++ ){

            if(Character.isUpperCase(password.charAt(i))){
                CH = true;
            }
            if(Character.isLowerCase(password.charAt(i))){
                ch = true;
            }
            if(Character.isDigit(password.charAt(i))){
                num = true;
            }
        }
        return CH&&ch&&num;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}