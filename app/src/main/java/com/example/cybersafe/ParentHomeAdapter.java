package com.example.cybersafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ParentHomeAdapter extends RecyclerView.Adapter<ParentHomeAdapter.ChildHolder> {
    View view;
    private Context context;
    private List<Child> childList;
    private OnItemClickListener listener;
    DatabaseReference commentsRef, ChildRef,SMARef;
    String SMA_id;



    public ParentHomeAdapter(List<Child> childList, OnItemClickListener listener) {
        this.childList = childList;
        this.listener = listener;
    }

    public ParentHomeAdapter(Context context, List<Child> childList, OnItemClickListener listener) {
        this.context = context;
        this.childList = childList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_home_item, parent, false);
        ParentHomeAdapter.ChildHolder holder = new ParentHomeAdapter.ChildHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.OnItemClick(view, holder.getAdapterPosition());

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
        String name = childList.get(position).getFirstName();
        String gender=childList.get(position).getGender();
        String child_id=childList.get(position).getChild_id();
        holder.writeChildP.setText(name);
        holder.red.setVisibility(View.INVISIBLE);

        //Change the avatar depend on gender
        if (gender.equals("Male")) {
            holder.icon.setBackground(ContextCompat.getDrawable(context, R.drawable.people_avatar_boy_child_person_icon_131264));//Boy Avatar
        }
        else{
            holder.icon.setBackground(ContextCompat.getDrawable(context, R.drawable.avatar_woman_female_girl_people_icon_131282));//Girl Avatar
        }

        SMARef = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );
        commentsRef = FirebaseDatabase.getInstance().getReference().child( "Comments" );

////Notification

        //Get the social media id for the child
        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        SMAccountCredentials checkSMA = messageSnapshot.getValue(SMAccountCredentials.class);

                        if (checkSMA.getChild_id().equals(child_id)) {
                            SMA_id = checkSMA.getId();

                            //Check if the comment reference in database add new check if it belongs to  the child and the comment is bully then show the notification
                            commentsRef.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                    Comment lastComment = snapshot.getValue(Comment.class);
                                    String commentSMA =lastComment.getSMAccountCredentials_id();

                                    if (SMA_id.equals(commentSMA)) {
                                        System.out.println("PHA Equals");
                                        if (lastComment.getFlag().equals(true)) {
                                            System.out.println("PHA True");
                                            holder.red.setVisibility(View.VISIBLE);
                                        }
                                    }




                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

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
        });



////


    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public class ChildHolder extends RecyclerView.ViewHolder{
        TextView writeChildP;
        ImageView icon , red;


        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            writeChildP = (TextView) itemView.findViewById(R.id.writeChildP);
            icon=itemView.findViewById(R.id.Avatar);
            red=itemView.findViewById(R.id.red);


        }
    }

}
