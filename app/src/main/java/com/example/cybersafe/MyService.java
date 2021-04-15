package com.example.cybersafe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Keyword;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

// نحط بال XML
//اتوقع نناديها باللوق ان واللوق اوت
public class MyService extends Service {

    DatabaseReference keywordsRef, keywordRef,commentRef, commentsRef, SMARef, ChildRef;
    ArrayList<Keyword> keywordArrayList= new ArrayList();
    ArrayList<Comment> commentList = new ArrayList();
   // private String userID, childID;
    String SMAccountCredentialID;

    //create an ArrayList to store our retrieved data in.
    int commentCounter;

    //ArrayList parentChildren=new ArrayList();
    ArrayList<String> parentChildren = new ArrayList();
    ArrayList<SMAccountCredentials> ChildrenSMA = new ArrayList();
    String accessToken, author_id, account,media_id,SMA_ID,Parent_ID;
    float video_Count,numberOfVideoRequest,numberOfCommentRequest;

    //in each request the is the minmmum number of videos and comments
    float video=20;
    float comment=30;
    boolean commentExist=false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("OONNN SSTTAARRT");

        //make references and bring information we need it in calling API
        ChildRef = FirebaseDatabase.getInstance().getReference().child("Children");
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        //store requests
        RequestQueue queue1= Volley.newRequestQueue( this );
        RequestQueue queue2 = Volley.newRequestQueue( this );
        RequestQueue queue = Volley.newRequestQueue( this );

        FirebaseUser currentParent=FirebaseAuth.getInstance().getCurrentUser();
        if (currentParent!=null){
            Parent_ID=currentParent.getUid();

        }

        // URL to retrieve comment list  from child TikTok post
        //first we make a API request to bring the number of child's video

        //Get parent children
        System.out.println("111111");
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
        System.out.println("222222");
        //        //Get the Social Media Account Credentials and the comment and add it to the database
System.out.println("parentChildren ########## "+parentChildren.isEmpty());

if(!(parentChildren.isEmpty())) {
    for (int i = 0; parentChildren.size() >= 0; i++) {
//
        String child_id = parentChildren.get(i);
        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        SMAccountCredentials checkSMA = messageSnapshot.getValue(SMAccountCredentials.class);

                        if (checkSMA.getChild_id().equals(child_id)) {
                            //Get the Social Media Account Credentials information we need
                            SMA_ID = checkSMA.getId();
                            accessToken = checkSMA.getAccess_token();
                            author_id = checkSMA.getAuthor_id();
                            account = checkSMA.getAccount();
                            commentCounter = checkSMA.getCommentCounter();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
        System.out.println("33333");


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET
                , "https://api.tikapi.io/user/info", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //the number of  child videos ?
                    JSONObject jsonObj3 = response.getJSONObject("userInfo");
                    JSONObject jsonArray3 = jsonObj3.getJSONObject("stats");
                    video_Count = jsonArray3.getInt("videoCount");
                    System.out.println(video_Count);


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
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            //we nee extra headers for our api url
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("X-ACCOUNT-KEY", accessToken); //but accessToken insted
                params.put("X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS");//always the same
                params.put("Accept", "application/json");
                return params;
            }
        };
        //store the requests
        queue.add(getRequest);
        System.out.println("444444");
        //make a request for the media id but first we make sure that we request for all the vedios the child post because each request bring only 20 vedios
        numberOfVideoRequest = (float) video_Count / video;

        System.out.println(numberOfVideoRequest);

        float x = 0;

        for (x = 0; x <= numberOfVideoRequest; x++) {

            //then loop and bring all the child videos id to use it in get comment list request


            JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET
                    , "https://api.tikapi.io/user/feed?count=20&cursor=0", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //all the comments from the child account until it reaches the count that is specfid in url
                        JSONArray jsonArray = response.getJSONArray("itemList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // specify what type of response we want from the URL. we are making a JsonObjectRequest
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            media_id = jsonObject.getString("id");
                            System.out.println("Media id:" + media_id);
                            JSONObject userObj = jsonObject.getJSONObject("stats");
                            int commentCount = userObj.getInt("commentCount");
                            System.out.println("total comment recived for this post is " + commentCount);
                            numberOfCommentRequest = commentCount / comment;
                            System.out.println(numberOfCommentRequest);
                            System.out.println("55555");

//bring for this media id"vedio"the comment list
                            for (int s = 0; s < numberOfCommentRequest; s++) {
                                String url1 = "https://api.tikapi.io/comment/list?media_id=" + media_id + "&cursor=0&count=30&author_id=" + author_id + "&author_username=" + account;

                                //GET request is creating the RequestQueue. The RequestQueue is what deals with all the requests passed into it and automatically handles all the backend work such as creating worker threads, reading from/writing to the cache and parsing responses.
                                JsonObjectRequest getRequest2 = new JsonObjectRequest(Request.Method.GET
                                        , url1, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            //all the comments from the child account until it reaches the count that is specfid in url

                                            JSONArray jsonArray = response.getJSONArray("comments");
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                                //get the comment body
                                                String comment = jsonObject.getString("text");
                                                System.out.println("comment:" + comment);
                                                //get comment id
                                                String commentID = jsonObject.getString("cid");
                                                System.out.println("commentID:" + commentID);
                                                //Get the sender name
                                                JSONObject userObj = jsonObject.getJSONObject("user");
                                                String senderName = userObj.getString("unique_id");
                                                System.out.println("the sender is " + senderName);
                                                //chick if the comment already exist


                                                commentRef.addValueEventListener(new ValueEventListener() {


                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                                                            Comment com = messageSnapshot.getValue(Comment.class);

                                                            if (com.getSMAccountCredentials_id().equals(SMA_ID)) {

                                                                if (com.getC_ID().equals(commentID)) {
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

                                                if (!commentExist) {
                                                    System.out.println("hello from if statment");

                                                    boolean filter = filter(comment, child_id);
                                                    boolean ourModel = ourModel(comment);
                                                    boolean sentimentAnalysisAPI = sentimentAnalysisAPI(comment);

                                                    String Comment_ID = commentRef.push().getKey();
                                                    Comment commentObj;
                                                    boolean bully;

                                                    if (filter || ourModel || sentimentAnalysisAPI) {
                                                        //create comment object to store it in database
                                                        commentObj = new Comment(Comment_ID, SMA_ID, senderName, comment, true, commentID);
                                                        bully = true;
                                                    } else {
                                                        //create comment object to store it in database
                                                        commentObj = new Comment(Comment_ID, SMA_ID, senderName, comment, false, commentID);
                                                        bully = false;
                                                    }


                                                    //Add the comment to the database
                                                    commentRef.child(Comment_ID).setValue(commentObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            // بس لتجريب بعدين نحذفها اذا ضبط
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(MyService.this, "Comment added successfully", Toast.LENGTH_LONG).show();
                                                                if (bully) {
                                                                    addNotification(child_id);
                                                                }

                                                            }
                                                        }
                                                    });

                                                    System.out.println(commentCounter);
//
                                                }
                                            }
                                        } catch (JSONException e) {
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
                                                Log.d("ERROR", "error => " + error.toString());
                                            }
                                        }
                                ) {
                                    @Override
                                    //we nee extra headers for our api url
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("X-ACCOUNT-KEY", accessToken);
                                        params.put("X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS");
                                        params.put("Accept", "application/json");
                                        return params;
                                    }
                                };
                                //store the requests
                                queue2.add(getRequest2);

                            }
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
                            Log.d("ERROR", "error => " + error.toString());
                        }
                    }
            ) {
                @Override
                //we nee extra headers for our api url
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-ACCOUNT-KEY", accessToken);
                    params.put("X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS");
                    params.put("Accept", "application/json");
                    return params;
                }
            };

            //store the requests
            queue1.add(getRequest1);
        }
    }

}
/*

        Toast.makeText(this, "Service Started",Toast.LENGTH_LONG).show();
        for(int i=0; i<3;i++)
            Toast.makeText(this, "Counting"+i,Toast.LENGTH_LONG).show();
*/



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    //detect if the comment contain any keyword the parent enter
    public boolean filter(String comment, String child_id){



        keywordsRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef.keepSynced(true);

        // SMAccountCredentialRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef.keepSynced(true);



        //Get the Keyword List
        keywordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keywordArrayList.clear();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Keyword key = messageSnapshot.getValue(Keyword.class);
                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                        if (key.getParent_id().equals(Parent_ID)){
                            keywordArrayList.add(key);
                        }
                    }
                } else {
                    Log.d("===", "No Data Was Found");

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get Comment
        //Get the SMAccountCredential ID for the Child to get the comments
       /* SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials smAccountCredentials = messageSnapshot.getValue(SMAccountCredentials.class);
                    //The comment not bully and same as the child email
                    if (smAccountCredentials.getChild_id().equals(child_id)){
                        SMAccountCredentialID=smAccountCredentials.getId();

                        //Now we get the comments with flag = false (not bully)
                        commentRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    commentList.clear();

                                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                        Comment com = messageSnapshot.getValue(Comment.class);
                                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                                        if (com.getFlag().equals(false) && com.getSMAccountCredentials_id().equals(SMAccountCredentialID)){
                                            commentList.add(com);

                                        }
                                    }
                                } else {
                                    Log.d("===", "No Data Was Found");

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        //Filter
        for(Keyword keyword : keywordArrayList){
            String word=keyword.getKeyword().toLowerCase();

            if ( comment.contains(word) ){
                //update the Flag and send notification
                return true;
            }

            }

        return false;
    }

    //Our trained model
    public boolean ourModel(String comment){
        final boolean[] bully = {false};

        RequestBody com = RequestBody.create(MediaType.parse("multipart/form-data"), comment);

        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.generateClient();
        Call<String> call =  retrofitClient.getApi().process(
                com
        );

        call.enqueue(new retrofit2.Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                System.out.println("onResponse onResponse");
                if (response.isSuccessful()){

                    System.out.println("###### "+response.body());
                    if (response.body().contains("Not a Bully")){
                        bully[0] =true;
                    }else{

                        bully[0] = false;
                    }
                }else{

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

        return  bully[0];
    }

    //sentiment Analysis API from google cloud
    public boolean sentimentAnalysisAPI(String comment){
        boolean sentimentPNN = false;
        float sentimentScore;

        try {
            // NOTE: The line below uses an embedded credential (res/raw/credential.json).
            //       You should not package a credential with real application.
            //       Instead, you should get a credential securely from a server.
            LanguageServiceClient language = LanguageServiceClient.create(
                    LanguageServiceSettings.newBuilder()
                            .setCredentialsProvider(() ->
                                    GoogleCredentials.fromStream(getApplicationContext()
                                            .getResources()
                                            .openRawResource(R.raw.credentials)))
                            .build());

            // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
            Document doc = Document.newBuilder().setContent(comment).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment == null) {
                return false;
            } else {
//                System.out.printf("Sentiment magnitude: %.3f\n", sentiment.getMagnitude());
//                System.out.printf("Sentiment score: %.3f\n", sentiment.getScore());
                sentimentScore= sentiment.getScore();

            }

           // System.out.println("SSS");
        } catch (IOException e) {
           // System.out.println("####### catch");
            throw new IllegalStateException("Unable to create a language client", e);
        }

        if(1.0 >= sentimentScore && sentimentScore >= 0.25){
            sentimentPNN=false;
        } else if(0.25 > sentimentScore && sentimentScore >= (-0.25)){
            sentimentPNN=false;
        }else if((-0.25) > sentimentScore && sentimentScore >= (-1.0)){
            sentimentPNN=true;
        }
       // System.out.println("sentimentPNN "+sentimentPNN);

return sentimentPNN;
    }

    private void addNotification(String child_id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Bully comment detected!!")
                        .setContentText("your child receive a bully comment");

        Intent notificationIntent = new Intent(this, BullyCommentMain.class);
        notificationIntent.putExtra("Child_id", child_id);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}