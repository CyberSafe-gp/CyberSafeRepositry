package com.example.cybersafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        /*final String id = commentsList.get(position).getComment_id();
        final Boolean flag = commentsList.get(position).getFlag();*/

        String sender = commentsList.get(position).getSender();
        String body = commentsList.get(position).getBody();


        holder.WriteBullyComment.setText(body);
        holder.WriteBullyName.setText(sender);

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        TextView WriteBullyName, WriteBullyComment;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            WriteBullyName = (TextView) itemView.findViewById(R.id.WriteBullyName);
            WriteBullyComment = (TextView) itemView.findViewById(R.id.WriteBullyComment);


        }
    }
}