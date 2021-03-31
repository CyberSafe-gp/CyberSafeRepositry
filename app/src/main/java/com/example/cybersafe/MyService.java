package com.example.cybersafe;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Keyword;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// نحط بال XML
//اتوقع نناديها باللوق ان واللوق اوت
public class MyService extends Service {

    DatabaseReference keywordsRef, keywordRef,commentRef, commentsRef, SMAccountCredentialRef;
    ArrayList<Keyword> keywordArrayList= new ArrayList();
    ArrayList<Comment> commentList = new ArrayList();
    private String userID, childID;
    String SMAccountCredentialID;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service Started",Toast.LENGTH_LONG).show();
        for(int i=0; i<3;i++)
            Toast.makeText(this, "Counting"+i,Toast.LENGTH_LONG).show();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            childID = intent.getStringExtra("Child_id");
        } else {
            System.out.println("userID out");
            Intent in = new Intent(MyService.this, ParentLogin.class);
            startActivity(in);
        }


        keywordsRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef = FirebaseDatabase.getInstance().getReference().child("Keywords");
        keywordRef.keepSynced(true);

        SMAccountCredentialRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
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
                        if (key.getParent_id().equals(userID)){
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
        SMAccountCredentialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials smAccountCredentials = messageSnapshot.getValue(SMAccountCredentials.class);
                    //The comment not bully and same as the child email
                    if (smAccountCredentials.getChild_id().equals(childID)){
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
        });


        //Filter
        for(Keyword keyword : keywordArrayList){
            String word=keyword.getKeyword().toLowerCase();
            for(int i = 0; i<= commentList.size() - 1; i++) {
                Comment comment=commentList.get(i);
                String body=comment.getBody().toLowerCase();
                String comID=comment.getComment_id();

                if ( body.contains(word) ){
                //update the Flag and send notification
                    commentsRef.child(comID).child("flag").setValue(true);
                }


            }

        }


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
    }
}