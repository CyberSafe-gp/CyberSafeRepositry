package com.example.cybersafe;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BullyCommentsAdapter extends RecyclerView.Adapter<BullyCommentsAdapter.CommentHolder> {
    private Context context;
    private List<Comment> commentsList;
    private OnItemClickListener listener;
    //private DatabaseReference commentRef;
    View view;


    public BullyCommentsAdapter(Context context, List<Comment> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
    }

    public BullyCommentsAdapter(Context context, List<Comment> commentsList, OnItemClickListener listener) {
        this.context = context;
        this.commentsList = commentsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bully_comment_item, parent, false);
        BullyCommentsAdapter.CommentHolder holder = new BullyCommentsAdapter.CommentHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        //commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        String id = commentsList.get(position).getComment_id();
//        final Boolean flag = commentsList.get(position).getFlag();

        String sender = commentsList.get(position).getSender();
        String body = commentsList.get(position).getBody();


        holder.WriteBullyComment.setText("\""+body+"\"");
        holder.WriteBullyName.setText("@"+sender);


        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        // if the user click the Not a bully comment
        holder.botBully.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ch : snapshot.getChildren()) {

                            Comment findComment =  ch.getValue(Comment.class);
                            String comID = findComment.getComment_id();
                            Boolean comFlag = findComment.getFlag();

                            if(comID.equals(id) & comFlag.equals(true)){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                // Setting Alert Dialog Title
                                alertDialogBuilder.setTitle("Not a bully comment?");
                                // Setting Alert Dialog Message
                                alertDialogBuilder.setMessage("Are you sure this comment not bully?");
                                //alertDialogBuilder.setCancelable(false);
                                // confirm flagging
                                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //update the flag to true (bully)
                                        commentsRef.child(comID).child("flag").setValue(false);
                                        //feedback for flagging
                                        Snackbar.make(view, "The comment has been updated Successfully", Snackbar.LENGTH_LONG).setDuration(30000)
                                                .show();

                                    }
                                }).setNegativeButton("No", null).show();
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
        TextView WriteBullyName, WriteBullyComment;
        ImageView botBully;


        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            WriteBullyName = (TextView) itemView.findViewById(R.id.WriteBullyName);
            WriteBullyComment = (TextView) itemView.findViewById(R.id.WriteBullyComment);
            botBully = itemView.findViewById(R.id.botBully);


        }
    }
}