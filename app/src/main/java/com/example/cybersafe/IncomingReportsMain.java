package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cybersafe.Objects.Comment;
import com.example.cybersafe.Objects.Report;
import com.example.cybersafe.Objects.SchoolManager;
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



    //getExtra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_reports);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            userID = user.getUid();
            userType = getIntent().getStringExtra("userType");
        } else {
            // ابي احط الصفحة الاولى حقت البارنت او السكول مانجر بس ما عرفت وش اسمها
            Intent in = new Intent(IncomingReportsMain.this, ParentLogin.class);
            startActivity(in);
        }
        //For test
        /*userID = "-MTz4FV6I8eik51jwoJ1";
        userType = "SchoolManager";*/



        textView = (TextView) findViewById(R.id.noReport);
        System.out.println("textView "+textView);

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef.keepSynced(true);


        adapter = new IncomingReportAdapter(this, reportList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                Report lr = reportList.get(pos);
                String status = lr.getStatus();

                if(userType.equals("SchoolManager") && status.equals("Not confirm")){


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IncomingReportsMain.this);
                    // Setting Alert Dialog Title
                    alertDialogBuilder.setTitle("View report");
                    // Setting Alert Dialog Message
                    alertDialogBuilder.setMessage("To view the report please confirm receiving it?");
                    // Confirm receiving
                    alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // school manager and confirm

                            Report lr = reportList.get(pos);
                            String report_id = lr.getReport_id();
                            reportRef.child(report_id).child("status").setValue("Confirm");


                            String com_id = lr.getComment_id();
                            String rep_id = lr.getReport_id();
                            String sender_id = lr.getSender_id();
                            String receiver_id = lr.getReceiver_id();
                            String sta = lr.getStatus();


                            Intent in = new Intent(IncomingReportsMain.this, Report_info.class);
                            in.putExtra("Comment_id", com_id);
                            in.putExtra("Report_id", rep_id);
                            in.putExtra("sender_id", sender_id);
                            in.putExtra("receiver_id", receiver_id);
                            in.putExtra("Status", sta);
                            startActivity(in);


                        }
                    });
                    // not confirm
                    alertDialogBuilder.setNegativeButton("Cancel", null).show();

                } else {
                    //userType = Parent
                    System.out.println("###############");

                    String com_id = lr.getComment_id();
                    String rep_id = lr.getReport_id();
                    String sender_id = lr.getSender_id();
                    String receiver_id = lr.getReceiver_id();
                    String sta = lr.getStatus();


                    Intent in = new Intent(IncomingReportsMain.this, Report_info.class);
                    in.putExtra("Comment_id", com_id);
                    in.putExtra("Report_id", rep_id);
                    in.putExtra("sender_id", sender_id);
                    in.putExtra("receiver_id", receiver_id);
                    in.putExtra("Status", sta);
                    startActivity(in);
                }

            }
        });

        recyclerView = findViewById(R.id.recyclerViewIncoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




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
                        textView.setText("no incoming report");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}