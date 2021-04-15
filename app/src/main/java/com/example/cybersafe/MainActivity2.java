package com.example.cybersafe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {


    DatabaseReference SMARef, ChildRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //make references and bring information we need it in calling API
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        String ch_id = ChildRef.push().getKey();
        String SM_id = SMARef.push().getKey();
        Child child1=new Child(ch_id,"4tqUIcM9GfSgK1hskkDefXP61vu1","MTz4FV6I8eik51jwoJ1","lulu","omar","20/05/1999","riyadh","female",6);
        SMAccountCredentials SMA=new SMAccountCredentials(SM_id,ch_id,"TikTok","luuluuomar","OkRNB31DvwBeOeC9LAS0obpO1PHChhlB","6878402755733390337");
    }
    }


