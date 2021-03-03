package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cybersafe.Objects.School;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        School USER = new School(id, "najd", "najd@hotmail.com");
        FirebaseDatabase.getInstance().getReference("Schools")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(USER);






    }

}