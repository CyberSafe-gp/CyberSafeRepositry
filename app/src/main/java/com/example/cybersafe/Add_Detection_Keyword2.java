package com.example.cybersafe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Keyword;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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
        Pattern p = Pattern.compile("[a-zA-Z0-9]");
        int len = KEYWORDS.length;


        for (int i = 0; i < len; i++) {
            Matcher m = p.matcher(KEYWORDS[i]);
            if (m.matches()) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Keyword k= new Keyword(KEYWORDS[i], "English",id);
                FirebaseDatabase.getInstance().getReference("Keywords").child(mDbRef.push().getKey()).setValue(k);
            }
            }

        }

        }






