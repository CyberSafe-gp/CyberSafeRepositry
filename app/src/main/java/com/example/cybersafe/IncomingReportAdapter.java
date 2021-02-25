package com.example.cybersafe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class IncomingReportAdapter extends RecyclerView.Adapter<IncomingReportAdapter.ReportHolder> {
// لازم اتاكد انه سكول مانجر عشان سالفة التاكيد
    private Context context;
    private List<Report> reportsList;
    private OnItemClickListener listener;
    private DatabaseReference reportsRef, userRef, SMAccountCredentialsRef;
    View view;


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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
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

        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");// مدري وش احط
        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");

        Collections.sort(reportsList, Collections.reverseOrder());

        final String comment_id = reportsList.get(position).getComment_id();
        final String report_id = reportsList.get(position).getReport_id();
         String status = reportsList.get(position).getStatus(); //        final String user_id = reportsList.get(position).getUser_id();


        // مو اكيد




        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                holder.WriteRepNo.setText("Report("+(position+1)+")");

                if (status.equals("Confirm"))
                    holder.button.setBackgroundColor(Color.LTGRAY);
                else
                    holder.button.setBackgroundColor(Color.parseColor("#263965"));



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
        TextView WriteRepNo;
        LinearLayout button ;


        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            WriteRepNo = (TextView) itemView.findViewById(R.id.WriteRepNo);
            button = (LinearLayout) itemView.findViewById(R.id.writeChildPRe);


        }
    }

}
