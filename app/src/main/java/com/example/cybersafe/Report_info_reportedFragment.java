package com.example.cybersafe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report_info_reportedFragment extends Fragment {

    String Comment_id, Report_id, sender_id, Status,receiver_id, childID, userType;
    DatabaseReference commentRef, SMARef, childRef;
    String childAccount, childName, application, commentText, bullyAccount,SMAccountCredentials_id;

    public Report_info_reportedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.report_info_reported, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = this.getArguments();

        Comment_id =bundle.getString("Comment_id");
        Report_id =bundle.getString("Report_id");
        sender_id =bundle.getString("sender_id");
        receiver_id =bundle.getString("receiver_id");
        Status =bundle.getString("Status");
        userType =bundle.getString("userType");


        // get the comment to retrieve the info
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                    Comment com = messageSnapshot.getValue(Comment.class);

                    if (com.getComment_id().equals(Comment_id)){

                        // the info that we can reach from the comment
                        commentText = com.getBody();
                        bullyAccount = com.getSender();
                        SMAccountCredentials_id = com.getSMAccountCredentials_id();

                        //Write the bully comment
                        TextView BullyCommentText = (TextView)getActivity().findViewById(R.id.BullyCommentText);
                        BullyCommentText.setText(commentText);

                        //Write the bully(sender) username
                        TextView WriteBullyAccount = (TextView)getActivity().findViewById(R.id.WriteBullyAccount);
                        WriteBullyAccount.setText(bullyAccount);


                        break;}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // get the smAccountCredentials to retrieve the info
        SMARef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");
        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    SMAccountCredentials sma = messageSnapshot.getValue(SMAccountCredentials.class);

                    if (sma.getId().equals(SMAccountCredentials_id)){

                        // the info that we can reach from the SMAccountCredentials

                        application = sma.getSocialMediaPlatform();
                        childAccount=sma.getAccount();
                        childID= sma.getChild_id();


                        //Write the child username
                        TextView WriteChildAccount = (TextView)getActivity().findViewById(R.id.WriteChildAccount);
                        WriteChildAccount.setText(childAccount);

                        // get the smAccountCredentials to retrieve the info
                        childRef = FirebaseDatabase.getInstance().getReference().child("Children");
                        childRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                    Child ch = messageSnapshot.getValue(Child.class);
                                    //The comment not bully and same as the child id
                                    if (ch.getChild_id().equals(childID)){
                                        childName = ch.getFirstName() +" "+ ch.getLastName();
                                        TextView WriteChildName = (TextView)getActivity().findViewById(R.id.WriteChildName);
                                        WriteChildName.setText(childName);

                                        break;}
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;}

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
