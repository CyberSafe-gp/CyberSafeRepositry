package com.example.cybersafe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.SMAccountCredentials;
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
    DatabaseReference commentsRef,SMARef;
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

        holder.dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dots:

                        PopupMenu popup = new PopupMenu(context.getApplicationContext(), v);
                        popup.getMenuInflater().inflate(R.menu.dots_menu,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.edit:

                                        //Go to Edit page
                                        Intent intent = new Intent(context,Edit_Child_Profile.class);
                                        intent.putExtra("Child_id",child_id);
                                        context.startActivity(intent);

                                        break;
                                    case R.id.delete:
                                        //Show confirm message

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                        // Setting Alert Dialog Title
                                        alertDialogBuilder.setTitle("Delete Child");
                                        // Setting Alert Dialog Message
                                        alertDialogBuilder.setMessage("Are you sure you want to delete this child?");
                                        //Confirm the delete
                                        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DatabaseReference childrenReference = FirebaseDatabase.getInstance().getReference().child( "Children" );
                                                DatabaseReference SMAReference = FirebaseDatabase.getInstance().getReference().child( "SMAccountCredentials" );
                                                childrenReference.addValueEventListener( new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot childrf : snapshot.getChildren()) {
                                                            Child child = childrf.getValue( Child.class );
                                                            if (child.getChild_id().equals( child_id )) {
                                                                SMAReference.addValueEventListener( new ValueEventListener() {


                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        for (DataSnapshot SMARe : snapshot.getChildren()) {
                                                                            SMAccountCredentials SMA = SMARe.getValue( SMAccountCredentials.class );
                                                                            if (SMA.getChild_id().equals( child_id )) {
                                                                                String SMAId = SMA.getId();
                                                                                //Delete the social media credential for this child
                                                                                SMAReference.child( SMAId ).removeValue();
                                                                                //Delete the child
                                                                                childrenReference.child( child_id ).removeValue();
                                                                                Toast.makeText( context, "Deleted successfully", Toast.LENGTH_LONG ).show();

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

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                } );

                                            }
                                        });


                                        // not confirm
                                        alertDialogBuilder.setNegativeButton("Cancel", null).show();






                                        break;

                                    default:
                                        break;
                                }

                                return true;
                            }
                        });

                        break;

                    default:
                        break;
                }
            }
        });

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

        final boolean[] exist = {false};

        //Get the social media id for the child
        SMARef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exist[0] = false;
                if (snapshot.exists()) {

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        SMAccountCredentials checkSMA = messageSnapshot.getValue(SMAccountCredentials.class);

                        if (checkSMA.getChild_id().equals(child_id)) {
                            SMA_id = checkSMA.getId();



                            //Check if the comment reference in database add new check if it belongs to  the child and the comment is bully then show the notification
                            commentsRef.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                            Comment lastComment = messageSnapshot.getValue(Comment.class);
                                            String commentSMA = lastComment.getSMAccountCredentials_id();
                                            String notification = lastComment.getNotification();

                                            if (SMA_id.equals(commentSMA)) {
                                                if (lastComment.getFlag().equals(true) && notification.equals("new")) {
                                                    exist[0] = true;
                                                }
                                            }
                                        }
                                        //if there is a new comment and bully show notification
                                        if (exist[0]){
                                            holder.red.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.red.setVisibility(View.INVISIBLE);
                                        }
                                    }
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
        ImageView icon , red, dots;


        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            writeChildP = (TextView) itemView.findViewById(R.id.writeChildP);
            icon=itemView.findViewById(R.id.Avatar);
            red=itemView.findViewById(R.id.red);
            dots=itemView.findViewById(R.id.dots);


        }
    }

}
