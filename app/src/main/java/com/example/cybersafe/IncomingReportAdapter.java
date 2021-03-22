package com.example.cybersafe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class IncomingReportAdapter extends RecyclerView.Adapter<IncomingReportAdapter.ReportHolder> {
    // يمكن نغير كلام الريبورت
    private Context context;
    private List<Report> reportsList;
    private OnItemClickListener listener;
    private DatabaseReference reportsRef, userRef, SMAccountCredentialsRef;
    View view;
    String CommentSender, child_id, childFName;


    public IncomingReportAdapter(Context context, List<Report> reportsList) {
        this.context = context;
        this.reportsList = reportsList;
    }

    //for the main to click
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public IncomingReportAdapter(Context context, List<Report> reportsList, OnItemClickListener listener) {
        this.context = context;
        this.reportsList = reportsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item_n, parent, false);
        ReportHolder holder = new ReportHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        //sort status
        Collections.sort(reportsList, Collections.reverseOrder());

        String status = reportsList.get(position).getStatus();
        String comment_id = reportsList.get(position).getComment_id();

        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // set the text for the item يمكن يتغير
//                holder.WriteRepNo.setText("Report("+(position+1)+")");

                DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");


                //Get and set the child name
                commentsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ch : snapshot.getChildren()) {

                            Comment findComment =  ch.getValue(Comment.class);
                            String comID = findComment.getComment_id();

                            if(comID.equals(comment_id)){
                                CommentSender=findComment.getSender();

                                DatabaseReference smRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");

                                smRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ch : snapshot.getChildren()) {

                                            SMAccountCredentials findSM =  ch.getValue(SMAccountCredentials.class);
                                            String acc = findSM.getAccount();

                                            if(CommentSender.equals(acc)){
                                                child_id=findSM.getChild_id();

                                                DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Children");
                                                childRef.child(child_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Child child= dataSnapshot.getValue(Child.class);
                                                        childFName = child.getFirstName();
                                                        holder.WriteChildName.setText(childFName);



                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //set the report date
                holder.WriteDate.setText(reportsList.get(position).getDate());

                //Colour the status if Confirm gray, Not confirm blue
                if (status.equals("Confirm"))
                    holder.button.setBackgroundColor(Color.parseColor("#F8F8F8"));
                else
                    holder.button.setBackgroundColor(Color.WHITE );



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder{
        TextView WriteDate, WriteChildName;
        RelativeLayout button ;


        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            WriteDate = (TextView) itemView.findViewById(R.id.WriteDate);
            button = (RelativeLayout) itemView.findViewById(R.id.Incom);
            WriteChildName = (TextView) itemView.findViewById(R.id.WriteChildName);


        }
    }

}
