package com.example.cybersafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cybersafe.Objects.Comment;
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
    private ArrayList<Report> reportList = new ArrayList();;
    private IncomingReportAdapter adapter;
    private FirebaseUser user;
    private String userID;

// اتوقع لازم اعرف اذا بارنت او سكول
// لازم اتاكد انه سكول مانجر عشان سالفة التاكيد
    //getExtra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_reports);

// اذا احتاج شيء يجيبونه وهم داخلين الصفحه
//        Intent iin= getIntent();
//        Bundle bun = iin.getExtras();
//
//        if(bun!=null)
//        {
//            childEmail =(String) bun.get("childEmail");
//            childId =(String) bun.get("childId");
//        }

        textView = (TextView) findViewById(R.id.noReport);

        reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportRef.keepSynced(true);


        //usersRef = FirebaseDatabase.getInstance().getReference().child("users"); // اتوقع لازم اعرف اذا بارنت او سكول

        adapter = new IncomingReportAdapter(this, reportList, new OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int pos) {
                System.out.println("OnItemClick");

                Report lr = reportList.get(pos);
                String com_id = lr.getComment_id();
                System.out.println("com_id"+com_id);
                String rep_id = lr.getReport_id();
                String sender_id = lr.getSender_id();
                String receiver_id = lr.getReceiver_id();
                System.out.println("receiver_id"+receiver_id);


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


/*                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void OnItemClick(View v, int pos) {

                        // لازم اتاكد انه سكول مانجر عشان سالفة التاكيد
                        System.out.println("OnItemClick");


                        Report lr = reportList.get(pos);
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

                    } // end on item click listener
                });*/

        recyclerView = findViewById(R.id.recyclerViewIncoming);
        //adapter = new IncomingReportAdapter(this, reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        System.out.println("adapter");



       userID = "";
       userID="-MTz4FV6I8eik51jwoJ1";


       /*
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            userID = user.getUid();
        } else {
            // No user is signed in
        }*/

        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reportList.clear();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Report rep = messageSnapshot.getValue(Report.class);
                        //The income report
                        if (rep.getReceiver_id().equals(userID)){ //if (rep.getUser_id().equals(userID))
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