package com.example.cybersafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {

        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");// مدري وش احط
        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        SMAccountCredentialsRef = FirebaseDatabase.getInstance().getReference().child("SMAccountCredentials");

        final String comment_id = reportsList.get(position).getComment_id();
        final String report_id = reportsList.get(position).getReport_id();
        final String user_id = reportsList.get(position).getReceiver_id(); //        final String user_id = reportsList.get(position).getUser_id();


        // مو اكيد
        holder.writeReport.setText("Report("+(position+1)+")");
        //holder.BullyName.setText(commentsList.get(position).getSender());

        //if(commentsList.get(position).getSMAccountCredentials_email().trim().equals(userEmail))////////////////

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder{
        TextView writeReport;


        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            writeReport = (TextView) itemView.findViewById(R.id.writeReport);

        }
    }

}
