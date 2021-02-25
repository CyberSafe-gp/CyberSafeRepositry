package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cybersafe.Objects.Parent;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SchoolManagerRegister extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FirebaseAuth mAuth;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText email;
    private String schoolname;
    Spinner spinnerschoolname;
    TextView Submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        mAuth = FirebaseAuth.getInstance();
        firstName = (EditText) findViewById((R.id.firstname));
        lastName = (EditText) findViewById((R.id.lastname));
        email = (EditText) findViewById((R.id.email));
        password = (EditText) findViewById((R.id.password));
        spinnerschoolname = findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Children, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerschoolname.setAdapter(adapter);
        Submit = findViewById(R.id.submit_area);
        Submit.setOnClickListener((View.OnClickListener) this);

    }

    public void onClick (View v){
        switch (v.getId()) {
            case R.id.submit_area:
                registration();
                break;
        }
    }
    private void registration() {
        final String firstname1 = firstName.getText().toString().trim();
        final String lastname1 = lastName.getText().toString().trim();
        final String email1 = email.getText().toString().trim();
        final String password1 = password.getText().toString().trim();
        //final String schoolname = schoolname.trim();

        System.out.print(firstname1);

        if (firstname1.isEmpty()) {
            firstName.setError("First name is required");
            firstName.requestFocus();
            return;
        }
        if (lastname1.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return;
        }
        if (email1.isEmpty()) {
            email.setError(" Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        }
        if (password1.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (password1.length() < 8) {
            password.setError("Min password length should be 8 characters!");
            password.requestFocus();
            return;
        }

        if (!passwordValidation(password1)) {
            password.setError("Password should contains at least one capital letter, one small letter and one number");
            password.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if (task.isSuccessful()) {
                                                   String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                   SchoolManager USER = new SchoolManager(id,schoolname,firstname1,lastname1,password1,email1);
                                                   FirebaseDatabase.getInstance().getReference("SchoolManager")
                                                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                           .setValue(USER).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               Toast.makeText(SchoolManagerRegister.this, "SchoolManager registered Successfully ", Toast.LENGTH_LONG).show();
                                                               startActivity(new Intent(SchoolManagerRegister.this, SchoolHome.class));

                                                           } else {
                                                               Toast.makeText(SchoolManagerRegister.this, "Registration failed ", Toast.LENGTH_LONG).show();

                                                           }
                                                       }
                                                   });
                                               } else {
                                                   Toast.makeText(SchoolManagerRegister.this, "this email is Already taken", Toast.LENGTH_LONG).show();

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
