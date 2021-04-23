package com.example.cybersafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Parent;
import com.example.cybersafe.Objects.SMAccountCredentials;
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
public class Edit_Parent_ProfileFragment extends Fragment {

    private String userId;
ImageView dots;
    private Button logO;

    private FirebaseUser Cuser;
    private Button btEdit , changeP;
    private DatabaseReference g =  FirebaseDatabase.getInstance().getReference("Parents");

    private EditText editTextfName;
    private EditText editTextlName;
    private EditText editTextEmail;
    String userEmail,Fname,lName,parent_id;



    public Edit_Parent_ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit__parent__profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTextfName = getActivity().findViewById(R.id.firstNameB);
        editTextlName = getActivity().findViewById(R.id.lastNameB);
        editTextEmail = getActivity().findViewById(R.id.EditEmailAddressB);
        btEdit=(Button)getActivity().findViewById(R.id.editButton);
        changeP=(Button)getActivity().findViewById(R.id.changePass);
        dots=getActivity().findViewById( R.id.dotEdit );
        parent_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dotEdit:

                        PopupMenu popup = new PopupMenu(getActivity(),v);
                        popup.getMenuInflater().inflate(R.menu.editmenue,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.logg:
                                        //log-out from the user account+transfer him/her to the interface page+stop my service from working in the background
                                        Intent intent = new Intent(getActivity(),Interface.class);
                                        intent.putExtra("IntentName", "hi");
                                        FirebaseAuth.getInstance().signOut();
                                        getActivity().stopService(new Intent(getActivity(), MyService.class));


                                        startActivity(intent);

                                        break;
                                    case R.id.delete:
                                        //Show confirm message

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                        // Setting Alert Dialog Title
                                        alertDialogBuilder.setTitle("Delete account");
                                        // Setting Alert Dialog Message
                                        alertDialogBuilder.setMessage("Are you sure you want to delete your account?");
                                        //Confirm the delete
                                        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DatabaseReference ParentReference = FirebaseDatabase.getInstance().getReference().child( "Parents" );
                                                DatabaseReference SMAReference = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );
                                                DatabaseReference childrenReference = FirebaseDatabase.getInstance().getReference().child( "Children" );
                                                ParentReference.addValueEventListener( new ValueEventListener() {


                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot Parentrf : snapshot.getChildren()) {
                                                            Parent parentOBJ = Parentrf.getValue( Parent.class );
                                                            if (parentOBJ.getParent_id().equals( parent_id )) {
                                                                childrenReference.addValueEventListener( new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        for (DataSnapshot childRef : snapshot.getChildren()) {
                                                                            Child childObj = childRef.getValue( Child.class );
                                                                            if (childObj.getParent_id().equals( parent_id )) {
                                                                                String childObjID = childObj.getChild_id();
                                                                                SMAReference.addValueEventListener( new ValueEventListener() {

                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        for (DataSnapshot SMARef : snapshot.getChildren()) {
                                                                                            SMAccountCredentials SMAChild = SMARef.getValue( SMAccountCredentials.class );
                                                                                            for (DataSnapshot childrf : snapshot.getChildren()) {
                                                                                                SMAccountCredentials findSMA = childrf.getValue( SMAccountCredentials.class );
                                                                                                if (findSMA.getChild_id().equals( childObjID )) {
                                                                                                    String SMAId = findSMA.getId();
                                                                                                    //Delete the social media credential for this child
                                                                                                    SMAReference.child( SMAId ).removeValue();
                                                                                                    //Delete the child
                                                                                                    childrenReference.child( childObjID ).removeValue();


                                                                                                }

                                                                                            }
                                                                                        }}

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                } );
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                } );

                                                            }
                                                            ParentReference.child( parent_id ).removeValue();
                                                            FirebaseAuth.getInstance().getCurrentUser().delete();

                                                            Toast.makeText( getContext(), "Deleted successfully", Toast.LENGTH_LONG ).show();
                                                            Intent intent=new Intent(getActivity(),Interface.class);
                                                            startActivity(intent);
                                                            break;
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                } );
                                            }});
                                        // not confirm
                                        alertDialogBuilder.setNegativeButton("Cancel", null).show();



                                        break;

                                    default:
                                        break;
                                }

                                return true;
                            }
                        });

                        break;

                    default:
                        break;
                }
            }
        });



        //changePass-button
        changeP.setOnClickListener(v -> {
            Intent intentt = new Intent(getActivity(),resetpassword.class);
            startActivity(intentt);
        });

        Cuser = FirebaseAuth.getInstance().getCurrentUser();

        g.addValueEventListener(new ValueEventListener() {

            //bringing all the current user information in the data base and view it in the edit page
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.cybersafe.Objects.Parent us = postSnapshot.getValue(Parent.class);
                    if (us.getParent_id().equals(Cuser.getUid())) {
                        userEmail = us.getEmail();
                        userId = us.getParent_id();
                        Fname=us.getFirstName();
                        lName = us.getLastName();
                        userId = us.getParent_id();
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

            public void onClick(View view) {
                updateInfo(view);
            }
        });
    }

    //here we are changing the user information and validate it
    private void updateInfo(View view) {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Parents").child(userId);

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

        Cuser.updateEmail(email);

        //store it in the database for the current user
        Parent p = new Parent(fname, lastname, email, userId);
        userRef.setValue(p);
        Snackbar.make(view, "Information has been updated successfully", Snackbar.LENGTH_LONG).setDuration(20000).show();


    }
}

