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
    Button next;
    String access_token;
    String Author_id;
    String childAccount;
    Uri data;



    String url = "https://api.tikapi.io/user/info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_social_media_credintals );

      /*  //Get the child information to add it to the data base

        String parent_id = getIntent().getStringExtra("parentid");
        String school_id = getIntent().getStringExtra("school_id");
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String date = getIntent().getStringExtra("date");
        String userCity = getIntent().getStringExtra("userCity");
        String userGender = getIntent().getStringExtra("userGender");
        String userGrade = getIntent().getStringExtra("userGrade");
*/
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");

        RequestQueue queue = Volley.newRequestQueue( addSocialMediaCredintals.this );

        Intent intent = getIntent();
         data = intent.getData();

        //To get the access token for the login TikTok
        if(data != null){
             access_token = data.getQueryParameter("access_token");
            Toast.makeText(addSocialMediaCredintals.this, "access_token  "+access_token, Toast.LENGTH_LONG).show();


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
                    params.put( "X-ACCOUNT-KEY",access_token );// access
                    params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                    params.put( "Accept", "application/json" );
                    return params;
                }
            };
            //store the requests
            queue.add( getRequest );




        }
        else {
            Toast.makeText(addSocialMediaCredintals.this, "log in your child TikTok", Toast.LENGTH_LONG).show();

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

        next =  findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(data != null && Author_id != null && childAccount != null ){
                    String SMA_id = SMARef.push().getKey();

                    Intent in = new Intent(addSocialMediaCredintals.this, Add_NewChild.class);

                    in.putExtra("Author_id",Author_id);
                    in.putExtra("childAccount",childAccount);
                    in.putExtra("SMA_id",SMA_id);
                    in.putExtra("access_token",access_token);
                    startActivity(in);
                }else{
                    Toast.makeText(addSocialMediaCredintals.this, "Error oucord", Toast.LENGTH_LONG).show();

                }




            }
        });













    }

}