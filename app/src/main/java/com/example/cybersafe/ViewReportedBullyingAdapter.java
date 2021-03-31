package com.example.cybersafe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Child;
import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SMAccountCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

// يمكن نغير كلام الريبورت واغير الوان الستاتوس
public class ViewReportedBullyingAdapter extends RecyclerView.Adapter<ViewReportedBullyingAdapter.ReportHolder> {
    private Context context;
    private List<Report> reportsList;
    private OnItemClickListener listener;
    private DatabaseReference reportsRef;
    View view;



    public ViewReportedBullyingAdapter(Context context, List<Report> reportsList) {
        this.context = context;
        this.reportsList = reportsList;
    }

    public ViewReportedBullyingAdapter(Context context, List<Report> reportsList, OnItemClickListener listener) {
        this.context = context;
        this.reportsList = reportsList;
        this.listener = listener;
    }

    //for the main to click
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reported_item_new, parent, false);
        ViewReportedBullyingAdapter.ReportHolder holder = new ViewReportedBullyingAdapter.ReportHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(view, holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");

        String stat = reportsList.get(position).getStatus();
        String comment_id=reportsList.get(position).getComment_id();
        // set the text for the item يمكن يتغير
        //holder.WriteRepNo.setText("Report("+(position+1)+")");

        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");


        //Get and set the child name
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ch : snapshot.getChildren()) {

                    Comment findComment =  ch.getValue(Comment.class);
                    String comID = findComment.getComment_id();

                    if(comID.equals(comment_id)){
                        String smID=findComment.getSMAccountCredentials_id();

                        DatabaseReference smRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");

                        smRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ch : snapshot.getChildren()) {

                                    SMAccountCredentials findSM =  ch.getValue(SMAccountCredentials.class);
                                    String findSMId = findSM.getId();

                                    if(smID.equals(findSMId)){
                                        String child_id=findSM.getChild_id();

                                        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Children");
                                        childRef.child(child_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Child child= dataSnapshot.getValue(Child.class);
                                                String childFName = child.getFirstName();
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

        // اخليها اخضر واحمر
        if(stat.equals("Confirm")){
            holder.WriteStatus.setText(stat);
            holder.dot.setTextColor(Color.parseColor("#ff669900")); //Green
        }
        else{
            holder.WriteStatus.setText("Pending");
            holder.dot.setTextColor(Color.parseColor("#ffff8800")); //Orange
        }
    }



    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder{
        TextView WriteChildName, WriteDate,WriteStatus, dot;


        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            WriteChildName = (TextView) itemView.findViewById(R.id.WriteChildName);
            WriteStatus = (TextView) itemView.findViewById(R.id.WriteStatus);
            WriteDate = (TextView) itemView.findViewById(R.id.WriteDate);
            dot=(TextView) itemView.findViewById(R.id.dot);

        }
    }
}
