package com.example.cybersafe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cybersafe.Objects.Parent;
import com.example.cybersafe.Objects.SchoolManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.Calendar;

public class Login extends AppCompatActivity {

    private Button log;
    private TextView forgetPass;
    private FirebaseAuth Auth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    public static String LoggedIn_User_Email;
    public String userTypee;
    public View textUserType;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ParentRef,SchoolMangerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ParentRef=FirebaseDatabase.getInstance().getReference().child( "Parents" );
        SchoolMangerRef=FirebaseDatabase.getInstance().getReference().child( "SchoolManagers");
        userTypee=getIntent().getStringExtra("userType");
        TextView setUserType =   findViewById(R.id.userType);
        //userType
        if(userTypee.equals("Parent")){
            setUserType.setText("Parent Log-in");
        }else {
            setUserType.setText("School Manager Log-in");
        }
        //get and store the type of user loged-in
        Auth = FirebaseAuth.getInstance();
        log = (Button) findViewById(R.id.log);
        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        forgetPass  = (TextView) findViewById(R.id.buttonForget);
        forgetPass.setOnClickListener(v -> {
            Intent intent =new Intent(Login.this,Forgetpassword.class);
            intent.putExtra("userType",userTypee);
            startActivity(intent);

        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
    }
    //the log-in function and to validate the input if it match the patterns such as email pattern
    private void login(){
        //bring the email that the user wrote
        final String Email = editTextEmail.getText().toString().trim();
        //to send the tag with the email and store it in User id variable
        LoggedIn_User_Email = Email;
        OneSignal.sendTag("User_ID",LoggedIn_User_Email);
        //bring user password
        String password = editTextPassword.getText().toString().trim();
        //validation
        if(Email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        //validation
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Please enter your email address in format: yourname@example.com");
            editTextEmail.requestFocus();
            return;
        }
        // validation
        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
//        //validation
//        if(password.length() < 8){
//            editTextPassword.setError(" The password must be at least 8 characters.");
//            editTextPassword.requestFocus();
//            return;
//        }


        //log in with auth so we can bring the current user from any page
        Auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final boolean[] isParent = {false};
                if(task.isSuccessful()) {
                    //chick if the user has entered an email belong to an parent user and if not tell him to change the user type
                    if(userTypee.equalsIgnoreCase("Parent")) {
                        ParentRef.addValueEventListener( new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                        Parent parent = messageSnapshot.getValue( Parent.class );
                                        //Add children id that belong to the parent
                                        if (parent.getEmail().equalsIgnoreCase( Email )) {

                                            isParent[0] = true;
                                            continue;
                                        }
                                    }
                                }
                                //in here if the isParent true then the parent clicked the right user type so we will transfer him to his page and begin the background service that will chick the bully comments for the parents user if exist
                                if (isParent[0]) {
                                    Intent intent = new Intent(Login.this,ParentHome_New.class);
                                    intent.putExtra("userType", userTypee);
                                   // start the service for ever hour
                                        startService(new Intent(Login.this, MyService.class));
                                            Calendar cal = Calendar.getInstance();
                                            Intent intent2 = new Intent(Login.this, MyService.class);
                                            PendingIntent pintent = PendingIntent
                                                    .getService(Login.this, 0, intent2, 0);

                                            AlarmManager alarm = (AlarmManager) getSystemService( Context.ALARM_SERVICE);
                                            // Start service every hour
                                            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                                    200000, pintent);

                                    //transfer the parent to the his/her home
                                    startActivity(intent);
                                    //empty the log-in page text
                                    editTextEmail.getText().clear();
                                    editTextPassword.getText().clear();
                                }
                                else{
                                    // in here the email and password is exist in our database but the user type is wrong
                                    Toast.makeText(Login.this, "Please make sure you choose the correct user type", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, Login.class);
                                    intent.putExtra("userType", userTypee);

                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        } );





                    } else{
                        //in here the user type chosen  is school manger but we will make sure that he is a school manger

                        SchoolMangerRef.addValueEventListener( new ValueEventListener() {

                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                       String admin = "Not confirm";
                                                                       final boolean[] isSchoolManager = {false};
                                                                       if (snapshot.exists()) {
                                                                           for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                                                               SchoolManager schoolManager = messageSnapshot.getValue( SchoolManager.class );
                                                                               //Add children id that belong to the parent
                                                                               if (schoolManager.getEmail().equalsIgnoreCase( Email )) {
                                                                                   isSchoolManager[0] = true;
                                                                                   admin = schoolManager.getAdmin();
                                                                                   break;

                                                                               }
                                                                           }
                                                                           if (isSchoolManager[0]){


                                                                               if(admin.equals("Confirm")){
                                                                                   //transfer to the school manger home
                                                                                   Intent intent = new Intent(Login.this, SchoolHome_new.class);
                                                                                   intent.putExtra("userType", userTypee);
                                                                                   startService(new Intent(Login.this, ServiceSM.class));
                                                                                   startActivity(intent);
                                                                                   //empty the log in page
                                                                                   editTextEmail.getText().clear();
                                                                                   editTextPassword.getText().clear();
                                                                               }else{
                                                                                   //transfer to the school manger home
                                                                                   Intent intent = new Intent(Login.this, Admin_School.class);
                                                                                   intent.putExtra("userType", userTypee);
                                                                                   startActivity(intent);
                                                                                   //empty the log in page
                                                                                   editTextEmail.getText().clear();
                                                                                   editTextPassword.getText().clear();
                                                                               }


                                                                           }else {
                                                                               // in here the email and password is exist in our database but the user type is wrong
                                                                               Toast.makeText(Login.this, "Please make sure you choose the correct user type", Toast.LENGTH_LONG).show();
                                                                               Intent intent = new Intent(Login.this, Login.class);
                                                                               intent.putExtra("userType", userTypee);
                                                                               startActivity(intent);
                                                                               editTextEmail.getText().clear();
                                                                               editTextPassword.getText().clear();
                                                                           }
                                                                       }
                                                                       }


                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError error) {

                                                                   }
                                                               });
                                    //if the email is exist in school mangers database.





                    }}
                else//the email is not exist in our system or the user entered a wrong input
                {
                    Toast.makeText( Login.this, "The email or password entered is invalid.Pleas try again", Toast.LENGTH_LONG ).show();
                }
            }
        });
    }


}
