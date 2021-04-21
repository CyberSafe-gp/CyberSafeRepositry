package com.example.cybersafe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParentLogin extends AppCompatActivity {

    private Button log;
   private TextView forgetPass;
    private FirebaseAuth Auth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    public static String LoggedIn_User_Email;
    public String userTypee;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        Bundle b = getIntent().getExtras();// add these lines of code to get data from notification
        String notificationData = b.getString("data");

        userTypee=getIntent().getStringExtra("userType");
        System.out.println("########################notificationData "+b);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get the current loged-in user
        if (user != null) {
           String userID = user.getUid();
          System.out.println("EEEEEEXXXXIIISST "+userID);
            Intent intent = new Intent(ParentLogin.this, ParentHome_New.class);
            intent.putExtra("userType", "Parent");
            startActivity(intent);

        } else {
            System.out.println("NOOOOOOO");
        }

     /*   if ( !(b.equals("Bundle[{userType=Parent}]")) &&!(b.equals("Bundle[{userType=SchoolManager}]"))){
            *//*Bundle b = getIntent().getExtras();// add these lines of code to get data from notification
            String notificationData = b.getString("userTypeNotification");*//*
            System.out.println("notificationData "+notificationData);
            if(notificationData.equals("Parent")){

                Intent intent = new Intent(ParentLogin.this, ParentHome_New.class);
                intent.putExtra("userType", "Parent");
            }else {
                Intent intent = new Intent(ParentLogin.this, SchoolHome_new.class);
                intent.putExtra("userType", "SchoolManager");
            }


        }*/

        //get and store the type of user loged-in
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference("SchoolManagers");
        Auth = FirebaseAuth.getInstance();
        log = (Button) findViewById(R.id.button9);
        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        forgetPass  = (TextView) findViewById(R.id.buttonForget);
        forgetPass.setOnClickListener(v -> {
            Intent intent =new Intent(ParentLogin.this,Forgetpassword.class);
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
//the log-in function and validate the input
    private void login(){
        final String Email = editTextEmail.getText().toString().trim();
         LoggedIn_User_Email = Email;
        OneSignal.sendTag("User_ID",LoggedIn_User_Email);
        String password = editTextPassword.getText().toString().trim();

        if(Email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            editTextPassword.setError("The password must be at least 8 characters.");
            editTextPassword.requestFocus();
            return;
        }


        Auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    if (userTypee.equals("Parent")) {
                        System.out.println("userTypee Parent"+userTypee);
                        Intent intent = new Intent(ParentLogin.this, ParentHome_New.class);
                        intent.putExtra("userType", userTypee);

                        //Start the service every one hour
                        //startService(new Intent(ParentLogin.this, MyService.class));

                    /*    Intent serviceIntent = new Intent(ParentLogin.this, MyService.class);
                        startService(serviceIntent);*/
                        //Start the service every one hour

                        /*startService(new Intent(ParentLogin.this, MyService.class));
                        Calendar cal = Calendar.getInstance();
                        Intent intent2 = new Intent(ParentLogin.this, MyService.class);
                        PendingIntent pintent = PendingIntent
                                .getService(ParentLogin.this, 0, intent2, 0);

                        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        // Start service every hour
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                100000, pintent);*/

                        UpdateToken("Parents");
                        startActivity(intent);
                        finish();
                        editTextEmail.getText().clear();
                        editTextPassword.getText().clear();
                        finish();


                    } else{
                        System.out.println("userTypee SM "+userTypee);
                        Intent intent = new Intent(ParentLogin.this, SchoolHome_new.class);
                        intent.putExtra("userType", userTypee);
                        UpdateToken("SchoolManagers");
                        startActivity(intent);
                        finish();
                        editTextEmail.getText().clear();
                        editTextPassword.getText().clear();
                        finish();

                }




                }
                else
                    Toast.makeText(ParentLogin.this, "The email or password entered is invalid.Pleas try again", Toast.LENGTH_LONG).show();
                }
        });
    }

    private void UpdateToken(String userTypee){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        //sendNotification(refreshToken);
       // showNotification(userTypee);

      //  commentsRef.child(comID).child("flag").setValue(true);
        FirebaseDatabase.getInstance().getReference(userTypee).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(refreshToken);
    }

    public void sendNotification(String token){
        String SERVER_KEY = "AAAAj3DgA1s:APA91bEmIDGuXxzcBT40HIf5oPYk6YDXrsCK2GBuHH_g74G9w30v-famyQwSFuQ9U2qi_iEC8jXcKv83zJUcDhqPmqa7uWwV8d38jvy1AsPRTV_ZNz79FCPOZnUNmR-xyIMqy5nmNZZe";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            JSONObject message = new JSONObject();
            message.put("to", token);
            message.put("priority", "high");

            JSONObject notification = new JSONObject();
            notification.put("title", "Notification");
            notification.put("body", "New report");
            notification.put("click_action", "OPEN_ACTIVITY_1");

            message.put("notification", notification);

  /*          JSONObject data = new JSONObject();
            notification.put("userType", "Notification");
            notification.put("userTypeNotification", userTypee);
            message.put("data", data);
*/
            JsonObjectRequest getRequest1 = new JsonObjectRequest( Request.Method.POST
                    , "https://fcm.googleapis.com/fcm/send", message, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println( "RRREEESSSPPPOOONNN" );

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            System.out.println( "errrrroorrrrrrrrrrrr" );
                            Log.d( "ERROR", "error => " + error.toString() );
                        }
                    }
            ) {
                @Override
                //we nee extra headers for our api url
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put( "Content-type", "application/json" );
                    params.put( "Authorization", "key="+ SERVER_KEY );
                    return params;
                }
            };


            requestQueue.add( getRequest1 );


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void showNotification(String userTypee) {
        String title="Bully comment detected!!";
        String message="your child receive a bully comment";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Cyber Safe Notification");
            channel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.logo) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(getApplicationContext(), ParentHome_New.class);
        intent.putExtra("userType", userTypee);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());


    }


}