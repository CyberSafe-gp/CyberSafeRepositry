package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class addSocialMediaCredintals extends AppCompatActivity {
    DatabaseReference ChildRef, SMARef;
    ImageView loginTikTok;
    String access_token;
    String Author_id;
    String childAccount;
    RequestQueue queue = Volley.newRequestQueue( this );
    String url = "https://api.tikapi.io/user/info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_social_media_credintals );

        //Get the child information to add it to the data base

        String parent_id = getIntent().getStringExtra("parentid");
        String school_id = getIntent().getStringExtra("school_id");
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String date = getIntent().getStringExtra("date");
        String userCity = getIntent().getStringExtra("userCity");
        String userGender = getIntent().getStringExtra("userGender");
        String userGrade = getIntent().getStringExtra("userGrade");

        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");



        Intent intent = getIntent();
        Uri data = intent.getData();

        //To get the access token for the login TikTok
        if(data != null){
             access_token = data.getQueryParameter("access_token");

            //Get Author id and child Account

            //GET request is creating the RequestQueue. The RequestQueue is what deals with all the requests passed into it and automatically handles all the backend work such as creating worker threads, reading from/writing to the cache and parsing responses.
            JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET
                    , url, null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        //all the comments from the child account until it reaches the count that is specfid in url

                        //To get childAccount
                        JSONObject jsonObj3 = response.getJSONObject("userInfo");
                        JSONObject jsonArray3= jsonObj3.getJSONObject("user");
                        Author_id=jsonArray3.getString( "id" );
                        childAccount=jsonArray3.getString("uniqueId");

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }


            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d( "ERROR", "error => " + error.toString() );
                        }
                    }
            ) {
                @Override
                //we nee extra headers for our api url
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put( "X-ACCOUNT-KEY","2OehchZsl76sXtMI5ceqpMVATtSv1Uaq" );// access
                    params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                    params.put( "Accept", "application/json" );
                    return params;
                }
            };
            //store the requests
            queue.add( getRequest );


            String child_id = ChildRef.push().getKey();

            Child ChildObj=new Child(child_id, parent_id, school_id, firstName,lastName, date, userCity,userGender,Integer.parseInt(userGrade) );

            //Add child to database
            ChildRef.child(child_id).setValue(ChildObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        //social
                        String SMAid = SMARef.push().getKey();


                        SMAccountCredentials SMAObj=new SMAccountCredentials(SMAid,child_id,"TikTok",childAccount,access_token,Author_id,0);



                        SMARef.child(SMAid).setValue(SMAObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(addSocialMediaCredintals.this, "Child added successfully", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(addSocialMediaCredintals.this, "Child doesn't added", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                   /*     Toast.makeText(Add_NewChild.this, "Child added successfully", Toast.LENGTH_LONG).show();
                        // startActivity(new Intent(creatnotepopup.this, ExplorerNote.class));
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);

                        finish();*/
                    } else {
                       // Toast.makeText(Add_NewChild.this, "Child doesn't added", Toast.LENGTH_LONG).show();
                    }

                }
            });



        }

        loginTikTok =  findViewById(R.id.addSocial);
        loginTikTok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://tikapi.io/account/authorize?client_id=c_8PGS0I4EY9&redirect_uri=http://www.cybersafe.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
















    }

}