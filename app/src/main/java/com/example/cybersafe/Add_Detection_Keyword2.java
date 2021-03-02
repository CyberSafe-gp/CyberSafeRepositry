package com.example.cybersafe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Keyword;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Add_Detection_Keyword2 extends AppCompatActivity {
    private TextInputEditText keywords;
    public Button add;
    FirebaseDatabase mDatabase =FirebaseDatabase.getInstance();
    DatabaseReference mDbRef = mDatabase.getReference("Keywords");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__detection__keyword2);

        keywords = findViewById(R.id.keyword);
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
        }

        String[] KEYWORDS = keyWords.split(",");

       // Pattern p = Pattern.compile("[a-z]+|[A-Z]+|[0-9]+|\\s+");
        Pattern p = Pattern.compile("^[\\s\\w\\d\\?><;,\\{\\}\\[\\]\\-_\\+=!@\\#\\$%^&\\*\\|\\']*$");
        Pattern p2 = Pattern.compile("[\\u0600-\\u06ff]|[\\u0750-\\u077f]|[\\ufb50-\\ufc3f]|[\\ufe70-\\ufefc]\n");


        int len = KEYWORDS.length;



        for (int i = 0; i < len; i++) {
            Matcher m = p.matcher(KEYWORDS[i]);
            Matcher m2 = p2.matcher(KEYWORDS[i]);

           String id =" oN6Md1uGmBaWi6lyKBiTexo7x263";


            //English-Keywords
            if (m.matches()) {

               // String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "English",id);
                String keyId=mDbRef.push().getKey();
                FirebaseDatabase.getInstance().getReference("Keywords").child(keyId).setValue(k);

            }
            //Arabic-Keywords
            else if (m2.matches()) {
               // String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "Arabic",id);
                String keyId=mDbRef.push().getKey();
                FirebaseDatabase.getInstance().getReference("Keywords").child(keyId).setValue(k);

            }

            //Other-Keywords
           else{
             //   String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "Other",id);
                String keyId=mDbRef.push().getKey();
                FirebaseDatabase.getInstance().getReference("Keywords").child(keyId).setValue(k);

            }


            }




        }

        }






