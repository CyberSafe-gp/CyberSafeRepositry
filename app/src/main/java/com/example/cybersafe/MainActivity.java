package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.School;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class MainActivity  extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        School USER = new School(id, "OOP", "LLL");
        FirebaseDatabase.getInstance().getReference("Schools")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(USER);






    }
}
