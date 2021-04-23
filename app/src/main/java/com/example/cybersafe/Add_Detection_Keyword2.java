package com.example.cybersafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Keyword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Add_Detection_Keyword2 extends AppCompatActivity {

    //Modifiers
    private TextInputEditText keywords;
    public Button add;
    String id, userType;


    FirebaseDatabase mDatabase =FirebaseDatabase.getInstance();
    DatabaseReference mDbRef = mDatabase.getReference("Keywords");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__detection__keyword2);

        keywords = findViewById(R.id.keyword);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            id = user.getUid();
            userType = getIntent().getStringExtra("userType");

        } else {
            System.out.println("userID out");
            Intent in = new Intent(Add_Detection_Keyword2.this, Interface.class);
            startActivity(in);
        }


        //Add Keywords Button.
        add = findViewById(R.id.adD);
        add.setOnClickListener((View v) -> {

                addDetectionKeywords();

        });

    }

    private void addDetectionKeywords() {

        //Get the keywords if the field is not empty.
        String keyWords = keywords.getText().toString().trim();
        if (keyWords.isEmpty()) {
            keywords.setError("Field can not be empty");
            keywords.requestFocus();
            return;
        } else
            keywords.getText().clear();

        //Split the keywords and store them in the array.
        String[] KEYWORDS = keyWords.split("،|,");

       //Regular Expression to detect the Arabic language.
        Pattern ArabicPattern = Pattern.compile("^[\\u0621-\\u064A]+$");

        //KEYWORDS array length.
        int len = KEYWORDS.length;

        //Detect the language for each keyword in the array By using For Loop.
        for (int i = 0; i < len; i++) {


            //Check if the keyword language match of Arabic language.
            Matcher m2 = ArabicPattern.matcher(KEYWORDS[i]);


            //Arabic language - Keywords.
             if (m2.matches()) {

                 String keyId=mDbRef.push().getKey();

                 //Create the object of Keyword.
                Keyword k= new Keyword(keyId,KEYWORDS[i], "Arabic",id);

                //The keywords added successfully to the list
                String successfully = KEYWORDS[i]+ " added successfully";
                String notSuccessfully = KEYWORDS[i]+ " not added successfully";

                mDbRef.child(keyId).setValue(k).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Add_Detection_Keyword2.this, successfully, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(Add_Detection_Keyword2.this, notSuccessfully, Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }


            //Other languages - Keywords.
            else  {

                String keyId=mDbRef.push().getKey();
                Keyword k= new Keyword(keyId,KEYWORDS[i], "Not Arabic",id);
                String successfully = KEYWORDS[i]+ " added successfully";
                String notSuccessfully = KEYWORDS[i]+ " not added successfully";
                FirebaseDatabase.getInstance().getReference("Keywords").child(keyId).setValue(k).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Add_Detection_Keyword2.this, successfully, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(Add_Detection_Keyword2.this, notSuccessfully, Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }


            }

        }

        }






