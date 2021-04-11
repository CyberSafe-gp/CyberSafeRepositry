package com.example.cybersafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

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

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    List<String> jsonCommentList = new ArrayList<>();
int commentCounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        RequestQueue queue = Volley.newRequestQueue( this );
       // URL to retrieve comment list  from child tiktok post
        //media id
        //authour id
        //author username
        String url = "https://api.tikapi.io/comment/list?media_id=6924734270825188610&cursor="+commentCounter+"&count=30&author_id=6878402755733390337&author_username=luuluuomar";
        System.out.println(url);
        //GET request is creating the RequestQueue. The RequestQueue is what deals with all the requests passed into it and automatically handles all the backend work such as creating worker threads, reading from/writing to the cache and parsing responses.
        JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET
                , url, null,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           //all the comments from the child account until it reaches the count that is specfid in url
                            JSONArray jsonArray = response.getJSONArray("comments");
                            for(int i = 0; i < jsonArray.length(); i++){
                                // specify what type of response we want from the URL. we are making a JsonObjectRequest
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String textComment= jsonObject.getString("text");
                               // System.out.println(textComment);
                                JSONObject userObj= jsonObject.getJSONObject("user");
                                String senderName= userObj.getString("unique_id");
                                    //System.out.println(senderName);
                                    commentCounter++;
                                    jsonCommentList.add(textComment);

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
                        System.out.println("errrrroorrrrrrrrrrrr");
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


    }
}