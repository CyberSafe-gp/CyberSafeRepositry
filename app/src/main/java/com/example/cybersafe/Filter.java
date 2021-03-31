package com.example.cybersafe;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Keyword;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Filter {

    DatabaseReference keywordsRef, keywordRef;
    ArrayList<Keyword> keywordArrayList= new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {

        keywordsRef = FirebaseDatabase.getInstance().getReference().child("Keywords");

    }


}
