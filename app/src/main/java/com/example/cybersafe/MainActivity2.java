package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;



import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {
    //create an ArrayList to store our retrieved data in.
    int commentCounter;
    String Parent_ID;
    //ArrayList parentChildren=new ArrayList();
    ArrayList<String> parentChildren = new ArrayList();
    ArrayList<SMAccountCredentials> ChildrenSMA = new ArrayList();
    String accessToken, author_id, account;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        RequestQueue queue = Volley.newRequestQueue( this );
        // URL to retrieve comment list  from child tiktok post
        //media id WE NEED IT


        FirebaseUser currentParent=FirebaseAuth.getInstance().getCurrentUser();
        if (currentParent!=null){
            Parent_ID=currentParent.getUid();

        }
        else{
            Intent intent=new Intent(MainActivity2.this,Interface.class);
            startActivity(  intent);
        }
        DatabaseReference ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        DatabaseReference SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        DatabaseReference CommentRef = FirebaseDatabase.getInstance().getReference().child("Comments");


        //Get parent children
        ChildRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    parentChildren.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Child checkChild = messageSnapshot.getValue(Child.class);
                        //The income report
                        if (checkChild.getParent_id().equals(Parent_ID)) {
                            parentChildren.add(checkChild.getChild_id());
                        }
                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get the Social Media Account Credentials and the comment and add it to the database
        for(int i =0; parentChildren.size()>=0;i++){

            String child_id = parentChildren.get(i);
            SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        SMAccountCredentials checkSMA = messageSnapshot.getValue(SMAccountCredentials.class);

                        if (checkSMA.getChild_id().equals(child_id)) {
                            //Get the Social Media Account Credentials information we need
                           accessToken = checkSMA.getAccess_token();
                           author_id = checkSMA.getAuthor_id();
                           account = checkSMA.getAccount();
                           commentCounter = checkSMA.getCommentCounter();


                            String url = "https://api.tikapi.io/comment/list?media_id=6924734270825188610&cursor="+commentCounter+"&count=30&author_id="+author_id+"&author_username="+account;

                            //GET request is creating the RequestQueue. The RequestQueue is what deals with all the requests passed into it and automatically handles all the backend work such as creating worker threads, reading from/writing to the cache and parsing responses.
                            JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET
                                    , url, null,new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //all the comments from the child account until it reaches the count that is specfid in url
                                        JSONArray jsonArray = response.getJSONArray("comments");
                                        for(int i = 0; i < jsonArray.length(); i++){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            //get the comment body
                                            String textComment= jsonObject.getString("text");

                                            //Get the sender name
                                            JSONObject userObj= jsonObject.getJSONObject("user");
                                            String senderName= userObj.getString("unique_id");

                                            commentCounter++;
                                            String SMA_id = SMARef.push().getKey();
                                            //create comment object
                                            Comment commentObj= new Comment(child_id,SMA_id,senderName,textComment,false);
                                            //Add the comment to the database
                                            CommentRef.child(SMA_id).setValue(commentObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    // بس لتجريب بعدين نحذفها اذا ضبط
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(MainActivity2.this, "Comment added successfully", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });


                                        }
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
                                    params.put( "X-ACCOUNT-KEY","2OehchZsl76sXtMI5ceqpMVATtSv1Uaq" );
                                    params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                                    params.put( "Accept", "application/json" );
                                    return params;
                                }
                            };
                            //store the requests
                            queue.add( getRequest );


                           break;


                        }
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        }


    }
}