package com.example.cybersafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Report;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reported_item, parent, false);
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


/*        final String comment_id = reportsList.get(position).getComment_id();
        final String report_id = reportsList.get(position).getReport_id();
        final String user_id = reportsList.get(position).getReceiver_id();*/ //        final String user_id = reportsList.get(position).getUser_id();

        String stat = reportsList.get(position).getStatus();
        // مو اكيد
        holder.WriteRepNo.setText("Report("+(position+1)+")");

        // اخليها اخضر واحمر
        holder.WriteStatus.setText(stat);

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder{
        TextView WriteRepNo, WriteStatus;


        public ReportHolder(@NonNull View itemView) {
            super(itemView);
            WriteRepNo = (TextView) itemView.findViewById(R.id.WriteRepNo);
            WriteStatus = (TextView) itemView.findViewById(R.id.WriteStatus);

        }
    }
}
