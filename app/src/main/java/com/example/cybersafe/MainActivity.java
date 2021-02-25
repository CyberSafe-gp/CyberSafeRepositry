package com.example.cybersafe;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity  extends AppCompatActivity {
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
    DatabaseReference mDbRef= mDatabase.getReference("Parents");

    public static final String TAG = "TAG";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        mAuth.createUserWithEmailAndPassword("ses@gmail.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser userID = mAuth.getCurrentUser();
                            Parent user = new Parent("shahad","ali","ses@gmail.com",userID.getUid());
                            mDbRef.child(userID.getUid()).setValue(user);
                            Log.d(TAG, "createUserWithEmail:success");


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }

                        // ...
                    }
                });

    }
}
