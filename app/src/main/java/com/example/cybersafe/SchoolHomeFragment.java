package com.example.cybersafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cybersafe.Objects.School;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolHomeFragment extends Fragment {



    DatabaseReference schoolManagerRef, SchoolRef;
    TextView WritheSchoolManagerName,WritheSchoolManagerEmail, WriteSchoolName, WriteSchoolCity;
    private FirebaseUser user;
    String schoolManagerID, userEmail, Fname, lName,SchoolID;

    public SchoolHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.school_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            schoolManagerID = user.getUid().trim();
        } else {

            // No user is signed in
        }

        schoolManagerRef = FirebaseDatabase.getInstance().getReference().child("SchoolManagers");
        SchoolRef = FirebaseDatabase.getInstance().getReference().child("Schools");


        WritheSchoolManagerName =  getActivity().findViewById((R.id.WritheSchoolManagerName));
        WritheSchoolManagerEmail =  getActivity().findViewById((R.id.WritheSchoolManagerEmail));
        WriteSchoolName =  getActivity().findViewById((R.id.WriteSchoolName));
        WriteSchoolCity =  getActivity().findViewById((R.id.WriteSchoolCity));

        schoolManagerRef.addValueEventListener(new ValueEventListener() {
            @Override
            //bringing all the current School Manager information
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SchoolManager us = postSnapshot.getValue(SchoolManager.class);
                   /* System.out.println("us.getSchoolManager_id() "+us.getSchoolManager_id());
                    System.out.println("us.userID() "+userID);*/
                    if (us.getSchoolManager_id().equals(schoolManagerID)) {
                        userEmail = us.getEmail();
                       // userId = us.getSchoolManager_id();
                        Fname=us.getFirstName();
                        lName = us.getLastName();
                        SchoolID=us.getSchool_id();
                        String name=Fname+" "+lName;
                        WritheSchoolManagerName.setText(name);
                        WritheSchoolManagerEmail.setText(userEmail);

                        //Get the school info
                        SchoolRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot2 : snapshot.getChildren()){
                                    School school = postSnapshot2.getValue(School.class);
                                    if(school.getSchool_id().equals(SchoolID)){
                                        String city = school.getCity();
                                        String schoolName = school.getSchoolName();
                                        WriteSchoolName.setText(schoolName);
                                        WriteSchoolCity.setText(city);
                                        break;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
