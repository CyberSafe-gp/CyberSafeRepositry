package com.example.cybersafe;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Patterns;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.fragment.app.Fragment;

        import com.example.cybersafe.Objects.SchoolManager;
        import com.google.android.material.snackbar.Snackbar;
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
public class EditSchoolFragment extends Fragment {
    private String userId,userEmail,Fname,lName,Sid,pass,userType1;
    private FirebaseUser SchoolUser;
    private Button btEdit,btreset;
    String userID;
    private DatabaseReference SMRef =  FirebaseDatabase.getInstance().getReference("SchoolManagers");
    private EditText editTextfName,editTextlName,editTextEmail;

    public EditSchoolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit_school, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        getActivity().setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            userID = user.getUid();
        else {
            System.out.println("userID out");
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(getActivity(), ParentLogin.class);
            startActivity(in);
            //go back
        }

        editTextfName = getActivity().findViewById(R.id.firstname2);
        editTextlName = getActivity().findViewById(R.id.lastname2);
        editTextEmail = getActivity().findViewById(R.id.email2);
        btreset=(Button)getActivity().findViewById(R.id.button21);
        btEdit=(Button)getActivity().findViewById(R.id.editb);
        userType1= getActivity().getIntent().getStringExtra("userType");

        //to go for the reset passowrd pag
        btreset.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),resetpassword.class);
            intent.putExtra("userType",userType1);
            //bring the type of loged-in user
            startActivity(intent);
        });

        //


        SMRef.addValueEventListener(new ValueEventListener() {
            @Override
            //bringing all the current user information in the data base and view it in the edit page
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SchoolManager us = postSnapshot.getValue(SchoolManager.class);
                    System.out.println("us.getSchoolManager_id() "+us.getSchoolManager_id());
                    System.out.println("us.userID() "+userID);
                    if (us.getSchoolManager_id().equals(userID)) {
                        userEmail = us.getEmail();
                        userId = us.getSchoolManager_id();
                        Fname=us.getFirstName();
                        lName = us.getLastName();
                        userId = us.getSchoolManager_id();
                        Sid=us.getSchool_id();
                        pass=us.getPassword();
                        editTextfName.setText(Fname);
                        editTextlName .setText(lName);
                        editTextEmail.setText(userEmail);

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(v);
            }
        } );

    }

    ////here we are changing the user information and valdate it
    private void updateInfo(View view) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("SchoolManagers").child(userId);

        final String fname = editTextfName.getText().toString().trim();
        final String lastname = editTextlName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();


        if (fname.isEmpty()) {
            editTextfName.setError("first name is required");
            editTextfName.requestFocus();

            return;
        }
        if (lastname.isEmpty()) {
            editTextlName.setError("last name name is required");
            editTextlName.requestFocus();
            return;
        }


        if (!fname.matches("[a-zA-Z]+")) {
            editTextfName.setError("first name must contains letter");
            editTextfName.requestFocus();

            return;
        }


        if (!lastname.matches("[a-zA-Z]+")) {
            editTextlName.setError("last name must contains letter");
            editTextlName.requestFocus();

            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
//store it in the database for the current user
        SchoolManager Smanger = new SchoolManager(userId,Sid,fname, lastname, pass,email);
        userRef.setValue(Smanger);
        Snackbar.make(view, "Information has been updated successfully", Snackbar.LENGTH_LONG).setDuration(30000).show();


    }
}
