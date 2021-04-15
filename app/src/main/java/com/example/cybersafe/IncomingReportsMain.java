package com.example.cybersafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cybersafe.Objects.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IncomingReportsMain extends AppCompatActivity {

    DatabaseReference reportRef, reportsRef, usersRef;
    private TextView textView;
    private RecyclerView recyclerView;
    private ArrayList<Report> reportList = new ArrayList();
    private IncomingReportAdapter adapter;
    private FirebaseUser user;
    private String userID, userType;
    ImageView back, home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_reports);

        //To get the user id and type if user exist
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            userType = getIntent().getStringExtra("userType");
            System.out.println("userType "+userType);
        } else {
            // if user not log in go to Interface page
            Intent in = new Intent(IncomingReportsMain.this, ParentLogin.class);
            Toast.makeText(IncomingReportsMain.this, "You have to login first", Toast.LENGTH_LONG).show();
            startActivity(in);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        textView = (TextView) findViewById(R.id.noReport);

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef.keepSynced(true);


        adapter = new IncomingReportAdapter(this, reportList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Report lr = reportList.get(pos);
                String status = lr.getStatus();
                //If the user type is School manager and the status of the report not confirm he should confirm the receiving first
                if(userType.equals("Schoolmanager") && status.equals("Not confirm")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncomingReportsMain.this);
                    // Setting Alert Dialog Title
                    alertDialogBuilder.setTitle("View report");
                    // Setting Alert Dialog Message
                    alertDialogBuilder.setMessage("To view the report please confirm receiving it?");
                    // Confirm receiving
                    alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //confirm
                            reportInfo(lr);
                        }
                    });
                    //not confirm
                    alertDialogBuilder.setNegativeButton("Cancel", null).show();

                } else { //Parent or School manager and confirm
                        reportInfo(lr);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerViewIncoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //To get the list of the incoming report
        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reportList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //The income report
                        if (rep.getReceiver_id().equals(userID)){
                            reportList.add(rep);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("===", "No Data Was Found");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //To write if their no report
        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childrf : snapshot.getChildren()) {
                    Report findRep = childrf.getValue(Report.class);
                    String user_id = findRep.getReceiver_id(); //String user_id = findRep.getUser_id();
                    if (user_id.equals(userID)) {
                        textView.setText("");
                        break;
                    } else {
                        textView.setText("No incoming report");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //For sending the information of the report to the report info
    public void reportInfo(Report report){
        String status = report.getStatus();
        String report_id = report.getReport_id();
        if (status.equals("Not confirm"))
        reportRef.child(report_id).child("status").setValue("Confirm");


        String com_id = report.getComment_id();
        String rep_id = report.getReport_id();
        String sender_id = report.getSender_id();
        String receiver_id = report.getReceiver_id();
        String sta = report.getStatus();


        Intent in = new Intent(IncomingReportsMain.this, Report_info_reported.class);
        in.putExtra("Comment_id", com_id);
        in.putExtra("Report_id", rep_id);
        in.putExtra("sender_id", sender_id);
        in.putExtra("receiver_id", receiver_id);
        in.putExtra("Status", sta);
        in.putExtra("userType", userType);
        startActivity(in);


    }
}