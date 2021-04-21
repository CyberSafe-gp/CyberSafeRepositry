package com.example.cybersafe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class FlagMainFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Comment> commentList = new ArrayList();
    flagBullyCommentAdapter adapter;
    DatabaseReference commentRef, commentsRef, SMAccountCredentialRef;
    //OnItemClickListener listener;
    String childAccount, childId,SMAccountCredentialID;
    private TextView textView;
    String childID;

    public FlagMainFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_flag_bully_comment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        childID = bundle.getString("Child_id");


        textView = (TextView) getActivity().findViewById(R.id.noComments);
        SMAccountCredentialRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        commentRef.keepSynced(true);


        recyclerView = getActivity().findViewById(R.id.recyclerViewFlag);
        adapter = new flagBullyCommentAdapter(getActivity(), commentList, childID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //Get the SMAccountCredential ID for the Child so we can reach the comments
        SMAccountCredentialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials smAccountCredentials = messageSnapshot.getValue(SMAccountCredentials.class);

                    //The child od for social media credential equal the current child id
                    if (smAccountCredentials.getChild_id().equals(childID)){
                        SMAccountCredentialID=smAccountCredentials.getId(); //then we take the social media credential id

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
                                    if (findCom.getFlag().equals(false) && findCom.getSMAccountCredentials_id().equals(SMAccountCredentialID)) {
                                        textView.setText("");
                                        break;
                                    } else {
                                        textView.setText("no existing comments");

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


}
