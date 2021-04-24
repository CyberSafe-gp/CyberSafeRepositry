package com.example.cybersafe;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BullyCommentMainFragment extends Fragment {

    String childID;
    DatabaseReference commentRef, commentsRef, SMAccountCredentialRef;
    private TextView textView;
    RecyclerView recyclerView;
    ArrayList<Comment> commentList = new ArrayList();
    BullyCommentsAdapter adapter;
    String SMAccountCredentialID;


    public BullyCommentMainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_bullycomments, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        childID = bundle.getString("Child_id");



        textView = (TextView) getActivity().findViewById(R.id.noComments);
        textView.setTextColor(Color.parseColor("#D7DBDD"));

        SMAccountCredentialRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef.keepSynced(true);
        adapter = new BullyCommentsAdapter(getActivity(), commentList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Comment com = commentList.get(pos);
                reportInfo(com, childID);

            }
        });

        recyclerView = getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        //Get the SMAccountCredential ID for the Child to get the comments
        SMAccountCredentialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials smAccountCredentials = messageSnapshot.getValue(SMAccountCredentials.class);
                    //The comment not bully and same as the child email
                    if (smAccountCredentials.getChild_id().equals(childID)){
                        SMAccountCredentialID=smAccountCredentials.getId();

                        //Now we get the comments with flag = true (bully)
                        commentRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    commentList.clear();

                                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                        Comment com = messageSnapshot.getValue(Comment.class);
                                        String comment_id=com.getComment_id();
                                        //The comment not bully and same as the child's SMAccountCredential ID add it to the comment list
                                        if (com.getFlag().equals(true) && com.getSMAccountCredentials_id().equals(SMAccountCredentialID)){
                                            commentList.add(com);
                                            commentsRef.child(comment_id).child("notification").setValue("old");
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d("===", "No Data Was Found");

                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //If no comment found print no existing comments
                        commentsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotrf) {
                                for (DataSnapshot childrf : snapshotrf.getChildren()) {
                                    Comment findCom = childrf.getValue(Comment.class);
                                    //String acu = findCom.getSMAccountCredentials_Account();
                                    if (findCom.getFlag().equals(true) && findCom.getSMAccountCredentials_id().equals(SMAccountCredentialID)) {
                                        textView.setText("");
                                        break;
                                    } else {
                                        textView.setText("No existing bully comments");

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void reportInfo(Comment comment, String child_ID){

        String comment_id=comment.getComment_id();
        String SMA_id = comment.getSMAccountCredentials_id();
        String sender = comment.getSender();
        String body = comment.getBody();

        //Go to Report info page
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = new ReportToFragment();
        Bundle bundleReport = new Bundle();

        bundleReport.putString("Child_id", child_ID);
        bundleReport.putString("Comment_id", comment_id);
        bundleReport.putString("SMA_id", SMA_id);
        bundleReport.putString("sender", sender);
        bundleReport.putString("body", body);
        fragment.setArguments(bundleReport);
        fragmentManager.beginTransaction().replace(R.id.addFragmentLayout, fragment).addToBackStack(null).commit();




    }
}
