package com.example.cybersafe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

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
import com.example.cybersafe.Objects.Report;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

// نحط بال XML
//اتوقع نناديها باللوق ان واللوق اوت
public class MyService extends Service {

    private final String SERVER_KEY = "AAAAj3DgA1s:APA91bEmIDGuXxzcBT40HIf5oPYk6YDXrsCK2GBuHH_g74G9w30v-famyQwSFuQ9U2qi_iEC8jXcKv83zJUcDhqPmqa7uWwV8d38jvy1AsPRTV_ZNz79FCPOZnUNmR-xyIMqy5nmNZZe";


    DatabaseReference keywordsRef, keywordRef, commentsRef, SMARef, ChildRef ,ReportRef, ParentRef;
    ArrayList<Keyword> keywordArrayList = new ArrayList();
    ArrayList<Comment> commentList = new ArrayList();
    // private String userID, childID;
    String SMAccountCredentialID;

    //create an ArrayList to store our retrieved data in.

    //ArrayList parentChildren=new ArrayList();
    ArrayList<String> parentChildren = new ArrayList();
    ArrayList<SMAccountCredentials> ChildrenSMA = new ArrayList();
    String accessToken, author_id, account, media_id, SMA_ID, Parent_ID;
    float video_Count, numberOfVideoRequest, numberOfCommentRequest;

    //in each request the is the minmmum number of videos and comments
    float video = 20;
    float comment = 30;


    //we need it in looping over the comment list if the child comment exceeds 30 because the limit for each request is 30 comment
    ArrayList<Integer> commentCursor = new ArrayList<>( Arrays.asList( 0, 30, 60, 90, 120, 150, 180, 210, 240, 270, 300 ) );
    //we need it in looping over the video media id  if the child videos  exceeds 20 because the limit for each request is 20 video
    ArrayList<Integer> videoCursor = new ArrayList<>( Arrays.asList( 0, 20, 40, 60, 80, 100, 120, 140, 160, 180, 200 ) );

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //make references and bring information we need it in calling API
        ChildRef = FirebaseDatabase.getInstance().getReference().child( "Children" );
        SMARef = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );
        commentsRef = FirebaseDatabase.getInstance().getReference().child( "Comments" );
        ReportRef = FirebaseDatabase.getInstance().getReference().child( "Reports" );
        ParentRef = FirebaseDatabase.getInstance().getReference().child( "Parents" );

        //store requests

        RequestQueue queue = Volley.newRequestQueue( this );

        FirebaseUser currentParent = FirebaseAuth.getInstance().getCurrentUser();
        if (currentParent != null) {
            Parent_ID = currentParent.getUid();


        }




          //Get the Social Media Account Credentials and the comment and add it to the database

        ChildRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    parentChildren.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Child checkChild = messageSnapshot.getValue( Child.class );
                        //Add children id that belong to the parent
                        if (checkChild.getParent_id().equals( Parent_ID )) {
                            parentChildren.add( checkChild.getChild_id() );
                            System.out.println( "childes added" );
                        }
                    }

                    if (!(parentChildren.isEmpty())) {
                        for (int i = 0; parentChildren.size() > i; i++) {

                            String child_id = parentChildren.get( i );


                            SMARef.addValueEventListener( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                            SMAccountCredentials checkSMA = messageSnapshot.getValue( SMAccountCredentials.class );

                                            if (checkSMA.getChild_id().equals( child_id )) {
                                                //Get the Social Media Account Credentials information we need
                                                SMA_ID = checkSMA.getId();
                                                accessToken = checkSMA.getAccess_token();
                                                author_id = checkSMA.getAuthor_id();
                                                account = checkSMA.getAccount();

                                                JsonObjectRequest getRequest = new JsonObjectRequest( Request.Method.GET
                                                        , "https://api.tikapi.io/user/info", null, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            //the number of  child videos ?
                                                            JSONObject jsonObj3 = response.getJSONObject( "userInfo" );
                                                            JSONObject jsonArray3 = jsonObj3.getJSONObject( "stats" );
                                                            video_Count = jsonArray3.getInt( "videoCount" );


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

                                                        params.put( "X-ACCOUNT-KEY", accessToken ); //but accessToken insted
                                                        params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );//always the same
                                                        params.put( "Accept", "application/json" );
                                                        return params;
                                                    }
                                                };
                                                //store the requests
                                                queue.add( getRequest );


                                                //make a request for the media id but first we make sure that we request for all the videos the child post because each request bring only 20 videos
                                                numberOfVideoRequest = (float) video_Count / video;


                                                getVideo( numberOfVideoRequest, child_id );

                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            } );

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        /////


        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");

        //To get the list of the incoming report
        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //If there new incoming report notify the school manager
                        if (rep.getReceiver_id().equals(Parent_ID) && rep.getNotification().equals("new")){
                            showNotificationReport();
                            reportRef.child(rep.getReport_id()).child("notification").setValue("old");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ////


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException( "Not yet implemented" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);

    }

    public void getVideo(float numberOfVideoRequest1, String child_id) {

        RequestQueue queue1 = Volley.newRequestQueue( this );

        for (int x = 0; x < numberOfVideoRequest1; x++) {

            //then loop and bring all the child videos id to use it in get comment list request
            JsonObjectRequest getRequest1 = new JsonObjectRequest( Request.Method.GET
                    , "https://api.tikapi.io/user/feed?count=20&cursor=" + videoCursor.get( x ), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //all the comments from the child account until it reaches the count that is specified in url
                        JSONArray jsonArray = response.getJSONArray( "itemList" );
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // specify what type of response we want from the URL. we are making a JsonObjectRequest
                            JSONObject jsonObject = jsonArray.getJSONObject( i );
                            media_id = jsonObject.getString( "id" );
                            JSONObject userObj = jsonObject.getJSONObject( "stats" );
                            int commentCount = userObj.getInt( "commentCount" );
                            numberOfCommentRequest = commentCount / comment;
                            getComments( numberOfCommentRequest, media_id, child_id );

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
                    params.put( "X-ACCOUNT-KEY", accessToken );
                    params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                    params.put( "Accept", "application/json" );
                    return params;
                }
            };

            //store the requests
            queue1.add( getRequest1 );
        }
    }

    public void getComments(float numberOfCommentRequest, String media_id, String child_id) {

        RequestQueue queue2 = Volley.newRequestQueue( this );


        //bring for this media id"video"the comment list
        for (int s = 0; s < numberOfCommentRequest; s++) {
            String url1 = "https://api.tikapi.io/comment/list?media_id=" + media_id + "&cursor=" + commentCursor.get( s ) + "&count=30&author_id=" + author_id + "&author_username=" + account;

            //GET request is creating the RequestQueue. The RequestQueue is what deals with all the requests passed into it and automatically handles all the backend work such as creating worker threads, reading from/writing to the cache and parsing responses.
            JsonObjectRequest getRequest2 = new JsonObjectRequest( Request.Method.GET
                    , url1, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    //all the comments from the child account until it reaches the count that is specfid in url


                    //chick if the comment already exist

                    commentsRef = FirebaseDatabase.getInstance().getReference().child( "Comments" );
                    commentsRef.addValueEventListener( new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {


                                JSONArray jsonArray = response.getJSONArray( "comments" );
                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject( j );
                                    //get the comment body
                                    String comment = jsonObject.getString( "text" );

                                    //get comment id
                                    String commentID = jsonObject.getString( "cid" );

                                    //Get the sender name
                                    JSONObject userObj = jsonObject.getJSONObject( "user" );
                                    String senderName = userObj.getString( "unique_id" );

                                    addComment( child_id,comment,senderName,commentID );



                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    } );


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
                    params.put( "X-ACCOUNT-KEY", accessToken );
                    params.put( "X-API-KEY", "CYR2NAj0JjJbE09iFoe8jzr3gH6rBymS" );
                    params.put( "Accept", "application/json" );
                    return params;
                }
            };
            //store the requests
            queue2.add( getRequest2 );

        }
    }

    //detect if the comment contain any keyword the parent enter
    public boolean filter(String comment, String child_id) {



        keywordsRef = FirebaseDatabase.getInstance().getReference().child( "Keywords" );
        keywordRef = FirebaseDatabase.getInstance().getReference().child( "Keywords" );
        keywordRef.keepSynced( true );

        //Get the Keyword List
        keywordRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    keywordArrayList.clear();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Keyword key = messageSnapshot.getValue( Keyword.class );
                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                        if (key.getParent_id().equals( Parent_ID )) {
                            keywordArrayList.add( key );
                        }
                    }
                } else {
                    Log.d( "===", "No Data Was Found" );

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );


        //Filter
        for (int i=0; keywordArrayList.size() > i ;i++) {
            String word = keywordArrayList.get(i).getKeyword().toLowerCase();


            if (comment.toLowerCase().contains( word )) {
                //update the Flag and send notification
                return true;
            }

        }

        return false;
    }

    //Our trained model
    public boolean ourModel(String comment) {
        final boolean[] bully = {false};

        RequestBody com = RequestBody.create( MediaType.parse( "multipart/form-data" ), comment );

        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.generateClient();
        Call<String> call = retrofitClient.getApi().process(
                com
        );

        call.enqueue( new retrofit2.Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {

                    if (response.body().contains( "Not a Bully" )) {
                        bully[0] = true;

                    } else {
                        bully[0] = false;
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


            }
        } );

        return bully[0];
    }

    //sentiment Analysis API from google cloud
    public boolean sentimentAnalysisAPI(String comment) {
        boolean sentimentPNN = false;
        float sentimentScore;

        try {
            // NOTE: The line below uses an embedded credential (res/raw/credential.json).
            //       You should not package a credential with real application.
            //       Instead, you should get a credential securely from a server.
            LanguageServiceClient language = LanguageServiceClient.create(
                    LanguageServiceSettings.newBuilder()
                            .setCredentialsProvider( () ->
                                    GoogleCredentials.fromStream( getApplicationContext()
                                            .getResources()
                                            .openRawResource( R.raw.credentials ) ) )
                            .build() );


            // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
            Document doc = Document.newBuilder().setContent( comment ).setType( Document.Type.PLAIN_TEXT ).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment( doc );
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment == null) {
                return false;
            } else {

                sentimentScore = sentiment.getScore();
                language.close();
            }

        } catch (IOException e) {

            throw new IllegalStateException( "Unable to create a language client", e );
        }

        if (1.0 >= sentimentScore && sentimentScore >= 0.25) {
            sentimentPNN = false;
        } else if (0.25 > sentimentScore && sentimentScore >= (-0.25)) {
            sentimentPNN = false;
        } else if ((-0.25) > sentimentScore && sentimentScore >= (-1.0)) {
            sentimentPNN = true;
        }


        return sentimentPNN;
    }


    private void chickIfCommentExist(String child_id,String comment,String senderName,String commentID) {


        commentsRef.orderByChild("c_ID").equalTo(commentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //bus number exists in Database
                } else {
                    //bus number doesn't exists.
                    addComment(child_id,comment,senderName,commentID) ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void addComment(String child_id,String comment,String senderName,String commentID){

        commentsRef.orderByChild("c_ID").equalTo(commentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
            //comment exist no need to store it again

                }
                else{
                    boolean filter = filter( comment, child_id );
                    boolean ourModel = ourModel( comment );
                    boolean sentimentAnalysisAPI = sentimentAnalysisAPI( comment );

                    String Comment_ID = commentsRef.push().getKey();
                    Comment commentObj;
                    boolean bully;

                    if (filter || ourModel || sentimentAnalysisAPI) {
                        //create comment object to store it in database
                        commentObj = new Comment( Comment_ID, SMA_ID, senderName, comment, commentID, true );
                        bully = true;
                    } else {
                        //create comment object to store it in database
                        commentObj = new Comment( Comment_ID, SMA_ID, senderName, comment, commentID, false );
                        bully = false;
                    }
                    //bus number doesn't exists.
                    //Add the comment to the database
                    commentsRef.child(Comment_ID).setValue(commentObj).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()) {


                           if (bully) {
                               showNotificationBullyComment( child_id );


                               //check if the sender exixt
                               SMARef.addValueEventListener( new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if (snapshot.exists()) {

                                           for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                               SMAccountCredentials SMA = messageSnapshot.getValue( SMAccountCredentials.class );
                                               //Add children id that belong to the parent
                                               if (SMA.getAccount().equals( senderName )) {
                                                   String childID = SMA.getChild_id();

                                                   ChildRef.addValueEventListener( new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                           if (snapshot.exists()) {
                                                               for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                                                   Child child = messageSnapshot.getValue( Child.class );
                                                                   //Add children id that belong to the parent
                                                                   if (child.getChild_id().equals( childID )) {
                                                                       String ParentID = child.getParent_id();





                                                                       ReportRef = FirebaseDatabase.getInstance().getReference().child( "Reports" );

                                                                       String Report_id = ReportRef.push().getKey();
                                                                       Report incomingReport = new Report( Report_id, "Admin", ParentID, Comment_ID, "Not confirm", getDateTime());
                                                                       ReportRef.child(Report_id).setValue(incomingReport).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                           @Override
                                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                               if (task.isSuccessful()){

                                                                               }
                                                                           }
                                                                       });

                                                                       break;
                                                                   }
                                                               }
                                                           }
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                       }
                                                   } );
                                                   break;
                                               }
                                           }
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               } );
                            }

                            }
                        }
                    } );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //get today date for the report day
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd" );
        Date date = new Date();
        return dateFormat.format( date );
    }


    //For the current parent if detected a bully comment for one of his/her children notify the parent
    void showNotificationBullyComment(String child_id) {
        String title="CyberSafe";
        String message="Bully comment detected! your child receive a bully comment";
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
                .setSmallIcon(R.drawable.ic_baseline_notifications_24) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click

        //When click notification open children page
        Intent intent = new Intent(getApplicationContext(), ParentHome_New.class);
        intent.putExtra( "openNotification", "BullyComment" );
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());


    }

    //For the current user if there a new incoming report notify the parent
    void showNotificationReport() {
        String title="Report";
        String message="New incoming report.";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNE",
                    "YOUR_CHANNE",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CyberSafe Notification");
            channel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNE")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click


        Intent intent = new Intent(getApplicationContext(), ParentHome_New.class);
        intent.putExtra( "openNotification", "IncomingReport" );
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());


    }

}