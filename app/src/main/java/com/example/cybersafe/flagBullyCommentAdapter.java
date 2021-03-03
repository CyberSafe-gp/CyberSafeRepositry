package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cybersafe.Objects.Comment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class flagBullyCommentAdapter extends RecyclerView.Adapter<flagBullyCommentAdapter.CommentHolder>{
    //getExtra

    private Context context;
    List<Comment> commentsList;
    private OnItemClickListener listener;
    private DatabaseReference commentsRef, childRef, SMAccountCredentialsRef;
    String ChildID;
    View view;


    //for the main to click
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    //!!!
    public flagBullyCommentAdapter(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    public flagBullyCommentAdapter(Context context, List<Comment> commentsList, String ChildID) {
        this.context = context;
        this.commentsList = commentsList;
        this.ChildID=ChildID;
    }

    public flagBullyCommentAdapter(Context context, List<Comment> commentsList, OnItemClickListener listener) {
        this.context = context;
        this.commentsList = commentsList;
        this.listener = listener;

    }



    @NonNull
    @Override
    public flagBullyCommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flag_item, parent, false);
        CommentHolder holder = new CommentHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

        childRef = FirebaseDatabase.getInstance().getReference().child("Children");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        SMAccountCredentialsRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");


        String id = commentsList.get(position).getComment_id();

        //set the body and the sender name
        holder.BullyComment.setText(commentsList.get(position).getBody());
        holder.BullyName.setText(commentsList.get(position).getSender());


        // if the user click the flag
        holder.flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ch : snapshot.getChildren()) {

                            Comment findComment =  ch.getValue(Comment.class);
                             String comID = findComment.getComment_id();
                            Boolean comFlag = findComment.getFlag();

                            if(comID.equals(id) & comFlag.equals(false)){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                // Setting Alert Dialog Title
                                alertDialogBuilder.setTitle("Flag Comment");
                                // Setting Alert Dialog Message
                                alertDialogBuilder.setMessage("Are you sure you want to flag this comment ?");
                                //alertDialogBuilder.setCancelable(false);
                                // confirm flagging
                                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //update the flag to true (bully)
                                        commentsRef.child(comID).child("flag").setValue(true);
                                        //feedback for flagging
                                        Snackbar.make(view, "Bullying comment has been flagged Successfully", Snackbar.LENGTH_LONG).setDuration(30000)
                                                .show();

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                        // Setting Alert Dialog Title
                                        alertDialogBuilder.setTitle("Report the Comment");
                                        // Setting Alert Dialog Message
                                        alertDialogBuilder.setMessage("Do you want to report the bully comment? ");
                                        // Report the comment
                                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                //go to report page
                                                reportInfo(findComment);
                                            }
                                        });
                                        // no need for report
                                        alertDialogBuilder.setNegativeButton("No", null).show();
                                    }
                                }).setNeutralButton("No", null).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });




    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        TextView BullyName, BullyComment;
        ImageView flag;


        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            BullyName = (TextView) itemView.findViewById(R.id.WriteBullyName);
            BullyComment = (TextView) itemView.findViewById(R.id.WriteBullyComment);
            flag = itemView.findViewById(R.id.flagComment);


        }
    }
    public void reportInfo(Comment comment){

        String comment_id=comment.getComment_id();
        String SMA_id = comment.getSMAccountCredentials_id();
        String sender = comment.getSender();
        String body = comment.getBody();

        Intent in = new Intent(context, ReportTo.class);
        in.putExtra("Comment_id", comment_id);
        in.putExtra("SMA_id", SMA_id);
        in.putExtra("sender", sender);
        in.putExtra("body", body);
        in.putExtra("Child_id", ChildID);

        context.startActivity(in);


    }


}

