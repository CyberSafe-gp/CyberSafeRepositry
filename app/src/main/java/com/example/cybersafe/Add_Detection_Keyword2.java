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

//shahad
public class Add_Detection_Keyword2 extends AppCompatActivity {
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
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(Add_Detection_Keyword2.this, ParentLogin.class);
            startActivity(in);
        }



        add = findViewById(R.id.adD);



        add.setOnClickListener((View v) -> {

                addDetectionKeywords();

        });




    }

    private void addDetectionKeywords() {

        String keyWords = keywords.getText().toString().trim();
        if (keyWords.isEmpty()) {
            keywords.setError("Field can not be empty");
            keywords.requestFocus();
            return;
        } else
            keywords.getText().clear();

        String[] KEYWORDS = keyWords.split(",");

       // Pattern p = Pattern.compile("[a-z]+|[A-Z]+|[0-9]+|\\s+");
        Pattern p = Pattern.compile("^[\\s\\w\\d\\?><;,\\{\\}\\[\\]\\-_\\+=!@\\#\\$%^&\\*\\|\\']*$");
        Pattern p2 = Pattern.compile("[\\u0600-\\u06ff]|[\\u0750-\\u077f]|[\\ufb50-\\ufc3f]|[\\ufe70-\\ufefc]\n");


        int len = KEYWORDS.length;


        for (int i = 0; i < len; i++) {
            Matcher m = p.matcher(KEYWORDS[i]);
            Matcher m2 = p2.matcher(KEYWORDS[i]);

            //English-Keywords
            if (m.matches()) {

               // String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "English",id);
                String keyId=mDbRef.push().getKey();
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
            //Arabic-Keywords
            else if (m2.matches()) {
               // String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "Arabic",id);
                String keyId=mDbRef.push().getKey();
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

            //Other-Keywords
           else{
             //   String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "Other",id);
                String keyId=mDbRef.push().getKey();
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






