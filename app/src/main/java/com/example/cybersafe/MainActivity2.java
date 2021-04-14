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

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity2 extends AppCompatActivity {
    //create an ArrayList to store our retrieved data in.
    int commentCounter;
    String Parent_ID;
    //ArrayList parentChildren=new ArrayList();
    ArrayList<String> parentChildren = new ArrayList();
    ArrayList<SMAccountCredentials> ChildrenSMA = new ArrayList();
    String accessToken, author_id, account,media_id,child_id;
    float video_Count,numberOfVideoRequest,numberOfCommentRequest;
    //in each request the is the minmmum number of videos and comments
    float video=20;
    float comment=30;
    boolean commentExist=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //make references and bring information we need it in calling API

        DatabaseReference ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        DatabaseReference SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        DatabaseReference commentRef=FirebaseDatabase.getInstance().getReference().child("Comments");

        //store requests
        RequestQueue queue1= Volley.newRequestQueue( this );
        RequestQueue queue2 = Volley.newRequestQueue( this );
        RequestQueue queue = Volley.newRequestQueue( this );


        // URL to retrieve comment list  from child tiktok post
        //media id WE NEED IT
        //first we make a API request to bring the number of child's vedio


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

//        //Get the Social Media Account Credentials and the comment and add it to the database
//
//        for(int i =0; parentChildren.size()>=0;i++){
//
//            String child_id = parentChildren.get(i);
//            SMARef.addValueEventListener(new ValueEventListener() {
//
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//
//                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
//                            SMAccountCredentials checkSMA = messageSnapshot.getValue( SMAccountCredentials.class );
//
//                            if (checkSMA.getChild_id().equals( child_id )) {
//                                //Get the Social Media Account Credentials information we need
//                                child_id = checkSMA.getChild_id();
//                                accessToken = checkSMA.getAccess_token();
//                                author_id = checkSMA.getAuthor_id();
//                                account = checkSMA.getAccount();
//                                commentCounter = checkSMA.getCommentCounter();
//                            }
//                        }}}
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }

                JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET
                , "https://api.tikapi.io/user/info", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //the number of  child videos ?
                    JSONObject jsonObj3 = response.getJSONObject( "userInfo" );
                    JSONObject jsonArray3 = jsonObj3.getJSONObject( "stats" );
                    video_Count = jsonArray3.getInt( "videoCount" );
                    System.out.println( video_Count );


                } catch (JSONException e) {
                    e.printStackTrace();
                }


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

                params.put( "X-ACCOUNT-KEY", "2OehchZsl76sXtMI5ceqpMVATtSv1Uaq" ); //but accessToken insted
                params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );//always the same
                params.put( "Accept", "application/json" );
                return params;
            }
        };
        //store the requests
       queue.add(getRequest);

        //make a request for the media id but first we make sure that we request for all the vedios the child post because each request bring only 20 vedios
        numberOfVideoRequest=(float) video_Count/video;

        System.out.println(numberOfVideoRequest);

    float x=0;

for(x=0;x<=numberOfVideoRequest;x++){

       //then loop and bring all the child videos id to use it in get comment list request


               JsonObjectRequest getRequest1 = new JsonObjectRequest( Request.Method.GET
                       , "https://api.tikapi.io/user/feed?count=20&cursor=0", null, new Response.Listener<JSONObject>() {
                       @Override
                         public void onResponse(JSONObject response) {
                           try {
                              //all the comments from the child account until it reaches the count that is specfid in url
                                   JSONArray jsonArray = response.getJSONArray( "itemList" );
                                   for (int i = 0; i < jsonArray.length(); i++) {
                                        // specify what type of response we want from the URL. we are making a JsonObjectRequest
                                        JSONObject jsonObject = jsonArray.getJSONObject( i );
                                        media_id = jsonObject.getString( "id" );
                                        System.out.println( "Media id:" + media_id );
                                        JSONObject userObj = jsonObject.getJSONObject( "stats" );
                                      int  commentCount = userObj.getInt( "commentCount" );
                                        System.out.println( "total comment recived for this post is " + commentCount );
                                        numberOfCommentRequest = commentCount /comment;
                                        System.out.println(numberOfCommentRequest );


//bring for this media id"vedio"the comment list
                                 for (int s = 0; s < numberOfCommentRequest; s++) {
                                      String url1 = "https://api.tikapi.io/comment/list?media_id=" + media_id + "&cursor=0&count=30&author_id=6878402755733390337&author_username=luuluuomar";

                                      //GET request is creating the RequestQueue. The RequestQueue is what deals with all the requests passed into it and automatically handles all the backend work such as creating worker threads, reading from/writing to the cache and parsing responses.
                                        JsonObjectRequest getRequest2 = new JsonObjectRequest( Request.Method.GET
                                             , url1, null, new Response.Listener<JSONObject>() {
                                           @Override
                                        public void onResponse(JSONObject response) {
                                                  try {
                                                    //all the comments from the child account until it reaches the count that is specfid in url

                                         JSONArray jsonArray = response.getJSONArray( "comments" );
                                         for (int j = 0; j < jsonArray.length(); j++) {
                                           JSONObject jsonObject = jsonArray.getJSONObject( j );
                                       //get the comment body
                                           String comment = jsonObject.getString( "text" );
                                           System.out.println( "comment:" + comment );
                                         //get comment id
                                            String commentID = jsonObject.getString( "cid" );
                                            System.out.println( "commentID:" + commentID );
                                                            //Get the sender name
                                            JSONObject userObj = jsonObject.getJSONObject( "user" );
                                            String senderName = userObj.getString( "unique_id" );
                                           System.out.println( "the sender is " + senderName );
                                           //chick if the comment already exist


                                             commentRef.addValueEventListener(new ValueEventListener() {


                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                     for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                                                         Comment com = messageSnapshot.getValue( Comment.class );

                                                         if (com.getComment_id().equals( child_id )) {

                                                             if (com.getC_ID().equals( commentID )) {
                                                                 commentExist = true;
                                                                 break;
                                                             }
                                                         }

                                                     }//make sure about this code with leenah
                                                 }
                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });

                                           if(!commentExist) {
                                               System.out.println( "hello from if statment" );

                                                  String SMA_id = SMARef.push().getKey();
                                                            //create comment object to store it in database

                                                             Comment commentObj= new Comment(child_id,SMA_id,senderName,comment,false,commentID);
                                                            //Add the comment to the database
                                                             commentRef.child(SMA_id).setValue(commentObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        // بس لتجريب بعدين نحذفها اذا ضبط
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MainActivity2.this, "Comment added successfully", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                       });

                                                        System.out.println( commentCounter );
//
                                              }
                                                        } } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
//
//
    },
                                                new Response.ErrorListener() {
                                                     @Override
                                                     public void onErrorResponse(VolleyError error) {
////                                                            // TODO Auto-generated method stub
////
                                                           Log.d( "ERROR", "error => " + error.toString() );
                                                       }
                                                  }
                                          ) {
                                              @Override
                                           //we nee extra headers for our api url
                                           public Map<String, String> getHeaders() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                  params.put( "X-ACCOUNT-KEY", "2OehchZsl76sXtMI5ceqpMVATtSv1Uaq" );
                                                   params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                                                  params.put( "Accept", "application/json" );
                                                   return params;
                                            }
                                            };
                                          //store the requests
                                          queue2.add( getRequest2 );

                                       }
                                   }
                              } catch (JSONException e){
                                        e.printStackTrace();
                                    }

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
                                params.put( "X-ACCOUNT-KEY", "2OehchZsl76sXtMI5ceqpMVATtSv1Uaq" );
                                params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                                params.put( "Accept", "application/json" );
                                return params;
                            }
                        };

                        //store the requests
                        queue1.add( getRequest1 );
                    }}}


